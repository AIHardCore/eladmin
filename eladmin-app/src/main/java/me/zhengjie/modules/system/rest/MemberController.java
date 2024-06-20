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
import me.zhengjie.modules.system.service.MemberService;
import me.zhengjie.modules.system.service.dto.MemberDto;
import me.zhengjie.modules.system.service.dto.MemberQueryCriteria;
import me.zhengjie.modules.system.domain.Member;
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

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "APP用户管理")
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('member:list')")
    public void exportMember(HttpServletResponse response, MemberQueryCriteria criteria) throws IOException {
        memberService.download(memberService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询APP用户")
    @ApiOperation("查询APP用户")
    @PreAuthorize("@el.check('member:list')")
    public ResponseEntity<PageResult<MemberDto>> queryMember(MemberQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(memberService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增APP用户")
    @ApiOperation("新增APP用户")
    @PreAuthorize("@el.check('member:add')")
    public ResponseEntity<Object> createMember(@Validated @RequestBody Member resources){
        memberService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改APP用户")
    @ApiOperation("修改APP用户")
    @PreAuthorize("@el.check('member:edit')")
    public ResponseEntity<Object> updateMember(@Validated @RequestBody Member resources){
        memberService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
