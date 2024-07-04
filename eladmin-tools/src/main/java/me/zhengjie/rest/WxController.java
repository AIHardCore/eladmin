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
package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.WxConfig;
import me.zhengjie.domain.wx.OrderCallBackReq;
import me.zhengjie.service.WxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wx")
@Api(tags = "工具：微信管理")
public class WxController {

    private final WxService wxService;

    @GetMapping
    public ResponseEntity<WxConfig> queryAliConfig() {
        return new ResponseEntity<>(wxService.find(), HttpStatus.OK);
    }

    @Log("配置微信")
    @ApiOperation("配置微信")
    @PutMapping
    public ResponseEntity<Object> updateAliPayConfig(@Validated @RequestBody WxConfig wxConfig) {
        wxService.config(wxConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
