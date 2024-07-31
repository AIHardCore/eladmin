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

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.Rank;
import me.zhengjie.modules.system.service.RankService;
import me.zhengjie.modules.system.service.dto.RankQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.dto.RankDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "爆文榜管理")
@RequestMapping("/api/rank")
public class RankController {

    private final RankService rankService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rank:list')")
    public void exportRank(HttpServletResponse response, RankQueryCriteria criteria) throws IOException {
        rankService.download(rankService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询爆文榜")
    @PreAuthorize("@el.check('rank:list')")
    public ResponseEntity<PageResult<RankDto>> queryRank(RankQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rankService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增爆文榜")
    @ApiOperation("新增爆文榜")
    @PreAuthorize("@el.check('rank:add')")
    public ResponseEntity<Object> createRank(@Validated @RequestBody Rank resources){
        rankService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改爆文榜")
    @ApiOperation("修改爆文榜")
    @PreAuthorize("@el.check('rank:edit')")
    public ResponseEntity<Object> updateRank(@Validated @RequestBody Rank resources){
        rankService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除爆文榜")
    @ApiOperation("删除爆文榜")
    @PreAuthorize("@el.check('rank:del')")
    public ResponseEntity<Object> deleteRank(@RequestBody Integer[] ids) {
        rankService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
