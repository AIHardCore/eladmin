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
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.modules.system.service.MemberService;
import me.zhengjie.modules.system.service.dto.MemberDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "用户管理")
@RequestMapping("/app/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping()
    @Log("修改手机号")
    @ApiOperation("查询专栏")
        public ResponseEntity<Object> updatePhone(@Validated @RequestBody Member member){
        memberService.updatePhone(member.getPhone());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<MemberDto> getUserInfo() {
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        MemberDto memberDto = memberService.findByOpenId(jwtUserDto.getUser().getOpenId());
        memberDto.setOpenId(null);
        return ResponseEntity.ok(memberDto);
    }
}
