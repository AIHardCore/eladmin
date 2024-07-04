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
import me.zhengjie.modules.system.domain.Special;
import me.zhengjie.modules.system.service.SpecialService;
import me.zhengjie.modules.system.service.dto.RoleDto;
import me.zhengjie.modules.system.service.dto.SpecialQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.dto.SpecialDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-25
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "专栏管理")
@RequestMapping("/api/special")
public class SpecialController {

    private final SpecialService specialService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('special:list')")
    public void exportSpecial(HttpServletResponse response, SpecialQueryCriteria criteria) throws IOException {
        specialService.download(specialService.queryAll(criteria), response);
    }

    @ApiOperation("返回全部的专栏")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('special:list','article:add','article:edit')")
    public ResponseEntity<List<SpecialDto>> queryAllRole(){
        return new ResponseEntity<>(specialService.queryAll(),HttpStatus.OK);
    }

    @GetMapping
    @Log("查询专栏")
    @ApiOperation("查询专栏")
    @PreAuthorize("@el.check('special:list')")
    public ResponseEntity<PageResult<SpecialDto>> querySpecial(SpecialQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(specialService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增专栏")
    @ApiOperation("新增专栏")
    @PreAuthorize("@el.check('special:add')")
    public ResponseEntity<Object> createSpecial(@Validated @RequestBody Special resources){
        specialService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改专栏")
    @ApiOperation("修改专栏")
    @PreAuthorize("@el.check('special:edit')")
    public ResponseEntity<Object> updateSpecial(@Validated @RequestBody Special resources){
        specialService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除专栏")
    @ApiOperation("删除专栏")
    @PreAuthorize("@el.check('special:del')")
    public ResponseEntity<Object> deleteSpecial(@RequestBody Long[] ids) {
        specialService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
