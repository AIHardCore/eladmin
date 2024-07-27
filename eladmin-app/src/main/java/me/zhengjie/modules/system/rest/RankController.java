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
import me.zhengjie.modules.system.service.DictDetailService;
import me.zhengjie.modules.system.service.RankService;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.system.service.dto.RankDto;
import me.zhengjie.modules.system.service.dto.RankQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.enums.LogTypeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "爆文榜管理")
@RequestMapping("/app/rank")
public class RankController {

    private final RankService rankService;
    private final DictDetailService dictDetailService;

    @GetMapping
    @Log(type = LogTypeEnum.APP,module = "爆文榜")
    @ApiOperation("查询爆文榜")
    public ResponseEntity<PageResult<RankDto>> queryRank(RankQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rankService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ApiOperation("查询类型列表")
    @GetMapping(value = "/types")
    public ResponseEntity<List<DictDetailDto>> getRankTypes(){
        return new ResponseEntity<>(dictDetailService.getDictByName("rank_type"), HttpStatus.OK);
    }
}
