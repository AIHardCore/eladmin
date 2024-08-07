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
import me.zhengjie.modules.system.domain.Banner;
import me.zhengjie.modules.system.service.BannerService;
import me.zhengjie.modules.system.service.dto.BannerDto;
import me.zhengjie.modules.system.service.dto.BannerQueryCriteria;
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
* @date 2024-06-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "轮播图管理")
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('banner:list')")
    public void exportBanner(HttpServletResponse response, BannerQueryCriteria criteria) throws IOException {
        bannerService.download(bannerService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询轮播图")
    @PreAuthorize("@el.check('banner:list')")
    public ResponseEntity<PageResult<BannerDto>> queryBanner(BannerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bannerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增轮播图")
    @PreAuthorize("@el.check('banner:add')")
    public ResponseEntity<Object> createBanner(@Validated @RequestBody Banner resources){
        bannerService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改轮播图")
    @ApiOperation("修改轮播图")
    @PreAuthorize("@el.check('banner:edit')")
    public ResponseEntity<Object> updateBanner(@Validated @RequestBody Banner resources){
        bannerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除轮播图")
    @ApiOperation("删除轮播图")
    @PreAuthorize("@el.check('banner:del')")
    public ResponseEntity<Object> deleteBanner(@RequestBody Long[] ids) {
        bannerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
