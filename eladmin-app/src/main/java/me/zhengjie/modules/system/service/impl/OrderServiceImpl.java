/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.system.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.WxConfig;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.modules.system.domain.Order;
import me.zhengjie.modules.system.domain.Produce;
import me.zhengjie.modules.system.repository.MemberRepository;
import me.zhengjie.modules.system.repository.OrderRepository;
import me.zhengjie.modules.system.service.OrderService;
import me.zhengjie.modules.system.service.ProduceService;
import me.zhengjie.modules.system.service.dto.OrderDto;
import me.zhengjie.modules.system.service.dto.OrderQueryCriteria;
import me.zhengjie.modules.system.service.dto.ProduceDto;
import me.zhengjie.modules.system.service.mapstruct.OrderMapper;
import me.zhengjie.service.WxService;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-07-06
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WxService wxService;
    private final AlipayUtils alipayUtils;
    private final MemberRepository memberRepository;
    private final ProduceService produceService;

    @Override
    public PageResult<OrderDto> queryAll(OrderQueryCriteria criteria, Pageable pageable){
        Page<Order> page = orderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(orderMapper::toDto));
    }

    @Override
    public List<OrderDto> queryAll(OrderQueryCriteria criteria){
        return orderMapper.toDto(orderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public OrderDto findById(String id) {
        Order order = orderRepository.findById(id).orElseGet(Order::new);
        ValidationUtil.isNull(order.getOutTradeNo(),"Order","id",id);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayWithRequestPaymentResponse create(Order resources) {
        ProduceDto produce = produceService.findById(resources.getProduceId());
        if (produce == null || produce.getId() == null){
            throw new BadRequestException("商品不存在");
        }
        Member member = memberRepository.findById(((JwtUserDto)SecurityUtils.getCurrentUser()).getUser().getOpenId()).orElseGet(Member::new);
        resources.setMember(member);
        resources.setAmount(produce.getPrice());
        resources.setOutTradeNo(alipayUtils.getOrderCode());
        PrepayWithRequestPaymentResponse response = wxService.prepay(resources.getMember().getOpenId(),resources.getOutTradeNo(),resources.getRemark(),resources.getAmount());
        resources.setStatus(Transaction.TradeStateEnum.NOTPAY.ordinal());
        orderRepository.save(resources);
        return response;
    }

    @Override
    public ResponseEntity.BodyBuilder notify(HttpServletRequest request) {
        WxConfig wxConfig = wxService.find();
        log.debug("支付回调-开始处理支付回调");

        StringBuffer stringBuffer = new StringBuffer();
        ServletInputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            //读取回调请求体
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = stringBuffer.toString();

        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(request.getHeader("Wechatpay-Serial"))
                .nonce(request.getHeader("Wechatpay-Nonce"))
                .signature(request.getHeader("Wechatpay-Signature"))
                .timestamp(request.getHeader("Wechatpay-Timestamp"))
                .body(body)
                .build();

        log.debug("支付回调-回调参数:{}",JSON.toJSONString(requestParam));

        // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
        // 没有的话，则构造一个
        NotificationConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxConfig.getMchId())
                .privateKeyFromPath(wxConfig.getPrivateKeyPath())
                .merchantSerialNumber(wxConfig.getMerchantSerialNumber())
                .apiV3Key(wxConfig.getApiV3Key())
                .build();

        log.debug("支付回调-SAAutoCertificateConfig:{}",JSON.toJSONString(config));

        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config);

        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            String outTradeNo = transaction.getOutTradeNo();

            log.debug("支付回调-参数解密结果:{}",JSON.toJSONString(transaction));

            Order order = orderRepository.findById(outTradeNo).orElseGet(Order::new);

            // 如果处理失败，应返回 4xx/5xx 的状态码，例如 500 INTERNAL_SERVER_ERROR
            if (order == null || order.getOutTradeNo() == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (order.getStatus() == Transaction.TradeStateEnum.SUCCESS.ordinal()){
                // 处理成功，返回 200 OK 状态码
                return ResponseEntity.status(HttpStatus.OK);
            }
            order.setStatus(transaction.getTradeState().ordinal());
            order.setTransactionId(transaction.getTransactionId());
            orderRepository.save(order);
            log.debug("支付回调-更新订单：{}",JSON.toJSONString(order));
            Member member = memberRepository.findById(order.getMember().getOpenId()).orElseGet(Member::new);
            member.setVipExpiration(DateUtil.offset(new Date(), DateField.YEAR, 1).toTimestamp());
            memberRepository.save(member);
            log.debug("支付回调-更新会员：{}",JSON.toJSONString(member));
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("sign verification failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        log.debug("支付回调-结束");
        // 处理成功，返回 200 OK 状态码
        return ResponseEntity.status(HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        Order order = orderRepository.findById(resources.getOutTradeNo()).orElseGet(Order::new);
        ValidationUtil.isNull( order.getOutTradeNo(),"Order","id",resources.getOutTradeNo());
        order.copy(resources);
        orderRepository.save(order);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            orderRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<OrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrderDto order : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("微信订单号", order.getTransactionId());
            map.put("商户订单号", order.getOutTradeNo());
            map.put("价格", order.getAmount());
            map.put("状态", order.getStatus());
            map.put("消息或异常", order.getMsg());
            map.put("创建者", order.getCreateBy());
            map.put("更新者", order.getUpdateBy());
            map.put("创建日期", order.getCreateTime());
            map.put("更新时间", order.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
