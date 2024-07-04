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
import me.zhengjie.modules.system.service.ProduceService;
import me.zhengjie.modules.system.service.dto.ProduceDto;
import me.zhengjie.modules.system.service.dto.ProduceQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-10
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "商品管理")
@RequestMapping("/app/produce")
public class ProduceController {

    private final ProduceService produceService;

    @GetMapping
    @Log("查询商品")
    @ApiOperation("查询商品")
    public ResponseEntity<PageResult<ProduceDto>> queryProduce(ProduceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(produceService.queryAll(criteria,pageable),HttpStatus.OK);
    }
}
