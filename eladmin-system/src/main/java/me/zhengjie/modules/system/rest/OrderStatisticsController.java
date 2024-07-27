package me.zhengjie.modules.system.rest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.repository.OrderRepository;
import me.zhengjie.modules.system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @website https://eladmin.vip
 * @author hardcoer
 * @date 2024-07-20
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "阅读记录统计管理")
@RequestMapping("/api/statistics/order")
public class OrderStatisticsController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping
    @Log("查询订单记录统计")
    @ApiOperation("查询订单记录统计")
    public ResponseEntity<Map<String,Long>> statistics(){
        Map<String,Long> map = new HashMap<>();
        map.put("sum",orderRepository.sum());
        map.put("sumToday",orderRepository.sumToday());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/logs/{status}")
    @Log("查询订单周期每日统计")
    @ApiOperation("查询订单周期每日统计")
    public ResponseEntity<List<Map<String,Object>>> logs(@PathVariable int status){
        DateTime begin = DateUtil.beginOfWeek(new Date(),true);
        DateTime end = DateUtil.endOfWeek(new Date(),true);
        long howLong = DateUtil.between(begin,end, DateUnit.DAY) + 1;
        return new ResponseEntity<>(orderService.logs(begin.toString(),end.toString(),howLong,status), HttpStatus.OK);
    }
}
