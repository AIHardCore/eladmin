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
import me.zhengjie.modules.system.domain.Section;
import me.zhengjie.modules.system.service.SectionService;
import me.zhengjie.modules.system.service.dto.SectionDto;
import me.zhengjie.modules.system.service.dto.SectionQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "版块管理")
@RequestMapping("/app/section")
public class SectionController {

    private final SectionService sectionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportSection(HttpServletResponse response, SectionQueryCriteria criteria) throws IOException {
        sectionService.download(sectionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询版块")
    @ApiOperation("查询版块")
        public ResponseEntity<PageResult<SectionDto>> querySection(SectionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sectionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增版块")
    @ApiOperation("新增版块")
        public ResponseEntity<Object> createSection(@Validated @RequestBody Section resources){
        sectionService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改版块")
    @ApiOperation("修改版块")
        public ResponseEntity<Object> updateSection(@Validated @RequestBody Section resources){
        sectionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除版块")
    @ApiOperation("删除版块")
        public ResponseEntity<Object> deleteSection(@RequestBody Long[] ids) {
        sectionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
