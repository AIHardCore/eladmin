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
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.enums.LogTypeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "文章管理")
@RequestMapping("/app/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Log(type = LogTypeEnum.APP,module = "文章")
    @ApiOperation("查询文章")
    public ResponseEntity<PageResult<ArticleDto>> queryArticle(ArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @Log(type = LogTypeEnum.APP,module = "文章")
    @ApiOperation("文章详情")
    public ResponseEntity<ArticleDto> detail(@PathVariable Long id, HttpServletRequest request){
        return new ResponseEntity<>(articleService.findById(id,request),HttpStatus.OK);
    }
}
