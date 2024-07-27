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
package me.zhengjie.service.impl;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.WxConfig;
import me.zhengjie.domain.wx.AccessTokenInfo;
import me.zhengjie.domain.wx.WxUserInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.WxRepository;
import me.zhengjie.service.WxService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "wx")
@Slf4j
public class WxServiceImpl implements WxService {

    private final WxRepository wxRepository;

    @Override
    @Cacheable(key = "'config'")
    public WxConfig find() {
        Optional<WxConfig> alipayConfig = wxRepository.findById(1L);
        return alipayConfig.orElseGet(WxConfig::new);
    }

    @Override
    @CachePut(key = "'config'")
    @Transactional(rollbackFor = Exception.class)
    public WxConfig config(WxConfig wxConfig) {
        wxConfig.setId(1L);
        return wxRepository.save(wxConfig);
    }

    /**
     * 根据code获取openId
     *
     * @param code
     * @return
     */
    @Override
    public AccessTokenInfo getAccessTokenInfo(String code) {
        log.info("开始获取Token:{}",code);
        WxConfig wxConfig = find();
        String url = wxConfig.getAccessTokenUrl().replace("{CODE}",code).replace("{APPID}",wxConfig.getAppId()).replace("{APP_SECRET}",wxConfig.getAppSecret());
        String result = HttpUtil.get(url);
        log.info("url:{}",url);
        log.info("result:{}",result);
        AccessTokenInfo accessTokenInfo = JSON.parseObject(result,AccessTokenInfo.class);
        log.info("accessTokenInfo:{}",accessTokenInfo);
        if (accessTokenInfo.getErrcode() != null){
            throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR,"获取AccessToken信息失败:" + result);
        }
        return accessTokenInfo;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     *
     * @param accessToken
     * @param openId
     * @return
     */
    @Override
    public WxUserInfo getUserInfo(String accessToken, String openId) {
        WxConfig wxConfig = find();
        String url = wxConfig.getUserInfoUrl().replace("{ACCESS_TOKEN}",accessToken).replace("{OPENID}",openId);
        String result = HttpUtil.get(url);
        WxUserInfo userInfo = JSON.parseObject(result,WxUserInfo.class);
        if (userInfo.getErrcode() != null){
            throw new BadRequestException("获取用户信息失败！");
        }
        return userInfo;
    }

    /**
     * 微信支付预下单
     *
     * @param openId    下单人openid
     * @param orderNo   订单编号
     * @param orderName 订单名称
     * @param total     订单金额，单位：分
     * @return
     */
    public PrepayWithRequestPaymentResponse prepay(String openId, String orderNo, String orderName, Integer total) {
        WxConfig wxConfig = find();

        // 订单金额
        Amount amount = new Amount();
        amount.setTotal(total);

        Payer payer = new Payer();
        payer.setOpenid(openId);

        log.debug("wxConfig:{}",JSON.toJSONString(wxConfig));

        // 初始化商户配置
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(wxConfig.getMchId())
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKeyFromPath(wxConfig.getPrivateKeyPath())
                        .merchantSerialNumber(wxConfig.getMerchantSerialNumber())
                        .apiV3Key(wxConfig.getApiV3Key())
                        .build();

        log.debug("商户配置:{}",JSON.toJSONString(config));

        // 初始化服务
        JsapiServiceExtension service =
                new JsapiServiceExtension.Builder()
                        .config(config)
                        .signType("RSA") // 不填默认为RSA
                        .build();

        //支付参数
        PrepayRequest request = new PrepayRequest();
        request.setAmount(amount);
        request.setAppid(wxConfig.getAppId());
        request.setMchid(wxConfig.getMchId());
        request.setDescription(orderName);
        request.setNotifyUrl(wxConfig.getNotifyUrl());
        request.setPayer(payer);
        request.setOutTradeNo(orderNo);

        PrepayWithRequestPaymentResponse response = null;

        log.debug("支付参数:{}",JSON.toJSONString(request));

        try {
            // ... 调用接口
            response = service.prepayWithRequestPayment(request);
            log.debug("支付结果:{}",JSON.toJSONString(response));
        } catch (HttpException e) { // 发送HTTP请求失败
            throw new BadRequestException("微信预支付，发送HTTP请求失败");
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            throw new BadRequestException("微信预支付，发送HTTP请求状态异常");
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            throw new BadRequestException("微信预支付，解析返回体失败");
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
        }
        return response;
    }
}
