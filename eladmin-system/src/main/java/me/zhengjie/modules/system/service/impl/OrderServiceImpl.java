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

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Order;
import me.zhengjie.modules.system.repository.OrderRepository;
import me.zhengjie.modules.system.service.OrderService;
import me.zhengjie.modules.system.service.dto.OrderDto;
import me.zhengjie.modules.system.service.dto.OrderQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.OrderMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-06-30
**/
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final JdbcTemplate jdbcTemplate;

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
        ValidationUtil.isNull(order.getOutTradeNo(),"Order","outTradeNo",id);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Order resources) {
        orderRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        Order order = orderRepository.findById(resources.getOutTradeNo()).orElseGet(Order::new);
        ValidationUtil.isNull( order.getOutTradeNo(),"Order","outTradeNo",resources.getMember().getOpenId());
        order.copy(resources);
        orderRepository.save(order);
    }

    @Override
    public List<Map<String, Object>> logs(String begin, String end, long howLong, int status) {
        String param =  String.format("and status = %s ",status);
        String sql =
                "select " +
                        " t3.days, " +
                        " max(t3.num) num " +
                        "from " +
                        " ( " +
                        " SELECT " +
                        "  @cdate := date_add(@cdate, interval -1 day) days, " +
                        "  0 as num " +
                        " from " +
                        "  ( " +
                        "  SELECT " +
                        "   @cdate := DATE_ADD( '" + end.split(" ")[0] + "', INTERVAL + 1 day) " +
                        "  from " +
                        "   app_order " +
                        "  limit " + howLong + " ) t1 " +
                        "UNION ALL (" +
                        //以下为你所需要查询得业务表根据日期统计数据-begin
                        "select DATE_FORMAT(create_time, '%Y-%m-%d') AS dateStr,sum(amount) div 100 as num from app_order " +
                        "where create_time BETWEEN '" + begin + "' and '" + end + "' " + param +
                        "group by DATE_FORMAT(create_time, '%Y-%m-%d') " +
                        //以下为你所需要查询得业务表根据日期统计数据-end
                        ")) t3 " +
                        "where " +
                        " t3.days between '"+ begin.split(" ")[0] +"' and '"+ end.split(" ")[0] +"' " +
                        "GROUP BY " +
                        " t3.days order by t3.days asc";
        return jdbcTemplate.queryForList(sql);
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
