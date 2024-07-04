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
import me.zhengjie.modules.system.service.SpecialService;
import me.zhengjie.modules.system.service.dto.SpecialDto;
import me.zhengjie.modules.system.service.dto.SpecialQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(tags = "专栏管理")
@RequestMapping("/app/special")
public class SpecialController {

    private final SpecialService specialService;

    @GetMapping
    @Log("查询专栏")
    @ApiOperation("查询专栏")
        public ResponseEntity<List<SpecialDto>> querySpecial(SpecialQueryCriteria criteria){
        return new ResponseEntity<>(specialService.queryAll(criteria),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Log("查询专栏")
    @ApiOperation("查询专栏")
        public ResponseEntity<SpecialDto> querySpecial(@PathVariable Long id){
        return new ResponseEntity<>(specialService.findById(id),HttpStatus.OK);
    }
}
