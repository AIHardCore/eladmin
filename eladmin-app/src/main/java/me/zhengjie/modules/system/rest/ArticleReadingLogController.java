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
import me.zhengjie.modules.system.service.ArticleReadingLogService;
import me.zhengjie.modules.system.service.dto.ArticleReadingLogDto;
import me.zhengjie.modules.system.service.dto.ArticleReadingLogQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @website https://eladmin.vip
* @author hardcoer
* @date 2024-07-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "阅读记录管理")
@RequestMapping("/api/articleReadingLog")
public class ArticleReadingLogController {

    private final ArticleReadingLogService articleReadingLogService;

    @GetMapping
    @Log("查询阅读记录")
    @ApiOperation("查询阅读记录")
    @PreAuthorize("@el.check('articleReadingLog:list')")
    public ResponseEntity<PageResult<ArticleReadingLogDto>> queryArticleReadingLog(ArticleReadingLogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articleReadingLogService.queryAll(criteria,pageable),HttpStatus.OK);
    }
}
