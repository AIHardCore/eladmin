package me.zhengjie.modules.system.rest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.service.SysLogService;
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
@Api(tags = "APP用户登录记录统计管理")
@RequestMapping("/api/statistics/login")
public class MemberLoginStatisticsController {
    private final SysLogService sysLogService;

    @GetMapping("/count")
    @Log("查询APP用户登录记录统计")
    @ApiOperation("查询APP用户登录记录统计")
    @AnonymousPostMapping(value = "/count")
    public ResponseEntity<Map<String,Long>> loginCount(){
        Map<String,Long> map = new HashMap<>();
        map.put("all",sysLogService.countLogin(null));
        map.put("all-success",sysLogService.countLogin(true));
        map.put("all-fail",sysLogService.countLogin(false));
        map.put("today",sysLogService.countLoginToday(null));
        map.put("today-success",sysLogService.countLoginToday(true));
        map.put("today-fail",sysLogService.countLoginToday(false));
        map.put("week",sysLogService.countLoginCycle(DateUtil.beginOfWeek(new Date(),true).toTimestamp(),DateUtil.endOfWeek(new Date(),true).toTimestamp(),null));
        map.put("week-success",sysLogService.countLoginCycle(DateUtil.beginOfWeek(new Date(),true).toTimestamp(),DateUtil.endOfWeek(new Date(),true).toTimestamp(),true));
        map.put("week-fail",sysLogService.countLoginCycle(DateUtil.beginOfWeek(new Date(),true).toTimestamp(),DateUtil.endOfWeek(new Date(),true).toTimestamp(),false));
        map.put("month",sysLogService.countLoginCycle(DateUtil.beginOfMonth(new Date()).toTimestamp(),DateUtil.endOfMonth(new Date()).toTimestamp(),null));
        map.put("month-success",sysLogService.countLoginCycle(DateUtil.beginOfMonth(new Date()).toTimestamp(),DateUtil.endOfMonth(new Date()).toTimestamp(),true));
        map.put("month-fail",sysLogService.countLoginCycle(DateUtil.beginOfMonth(new Date()).toTimestamp(),DateUtil.endOfMonth(new Date()).toTimestamp(),false));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/logs/{success}")
    @Log("查询APP用户登录记录")
    @ApiOperation("查询APP用户登录记录")
    @AnonymousPostMapping(value = "/logs")
    public ResponseEntity<List<Map<String, Object>>> loginLog(@PathVariable Boolean success){
        DateTime begin = DateUtil.beginOfWeek(new Date(),true);
        DateTime end = DateUtil.endOfWeek(new Date(),true);
        long howLong = DateUtil.between(begin,end, DateUnit.DAY) + 1;;
        return new ResponseEntity<>(sysLogService.loginLog(begin.toString(),end.toString(),howLong,success), HttpStatus.OK);
    }
}
