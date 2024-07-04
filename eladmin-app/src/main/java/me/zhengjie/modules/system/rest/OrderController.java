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
package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Order;
import me.zhengjie.modules.system.service.OrderService;
import me.zhengjie.modules.system.service.dto.OrderDto;
import me.zhengjie.modules.system.service.dto.OrderQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-06
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "订单管理")
@RequestMapping("/app/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Log("查询订单")
    @ApiOperation("查询订单")
    public ResponseEntity<List<OrderDto>> queryOrder(OrderQueryCriteria orderQueryCriteria){
        orderQueryCriteria.setOpenId(((JwtUserDto)SecurityUtils.getCurrentUser()).getUser().getOpenId());
        return new ResponseEntity<>(orderService.queryAll(orderQueryCriteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单")
    @ApiOperation("新增订单")
    public ResponseEntity<Object> createOrder(@Validated @RequestBody Order resources){
        resources.setRemark("开通会员");
        return new ResponseEntity<>(orderService.create(resources),HttpStatus.OK);
    }

    @PutMapping
    @Log("修改订单")
    @ApiOperation("修改订单")
    public ResponseEntity<Object> updateOrder(@Validated @RequestBody Order resources){
        orderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
