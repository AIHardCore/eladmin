package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.repository.ArticleReadingLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @website https://eladmin.vip
 * @author hardcoer
 * @date 2024-07-20
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "阅读记录统计管理")
@RequestMapping("/api/statistics/article")
public class ArticleStatisticsController {
    private ArticleReadingLogRepository articleReadingLogRepository;

    @GetMapping
    @Log("查询阅读记录统计")
    @ApiOperation("查询阅读记录统计")
    public ResponseEntity<Map<String,Long>> queryArticleReadingLog(){
        Map<String,Long> map = new HashMap<>();
        map.put("vip",articleReadingLogRepository.countByType(true));
        map.put("unvip",articleReadingLogRepository.countByType(false));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
