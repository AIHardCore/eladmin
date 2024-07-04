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
import me.zhengjie.modules.system.domain.Produce;
import me.zhengjie.modules.system.service.ProduceService;
import me.zhengjie.modules.system.service.dto.ProduceQueryCriteria;
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
import me.zhengjie.modules.system.service.dto.ProduceDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "商品管理")
@RequestMapping("/api/produce")
public class ProduceController {

    private final ProduceService produceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('produce:list')")
    public void exportProduce(HttpServletResponse response, ProduceQueryCriteria criteria) throws IOException {
        produceService.download(produceService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询商品")
    @PreAuthorize("@el.check('produce:list')")
    public ResponseEntity<PageResult<ProduceDto>> queryProduce(ProduceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(produceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增商品")
    @ApiOperation("新增商品")
    @PreAuthorize("@el.check('produce:add')")
    public ResponseEntity<Object> createProduce(@Validated @RequestBody Produce resources){
        produceService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改商品")
    @ApiOperation("修改商品")
    @PreAuthorize("@el.check('produce:edit')")
    public ResponseEntity<Object> updateProduce(@Validated @RequestBody Produce resources){
        produceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除商品")
    @ApiOperation("删除商品")
    @PreAuthorize("@el.check('produce:del')")
    public ResponseEntity<Object> deleteProduce(@RequestBody Integer[] ids) {
        produceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
