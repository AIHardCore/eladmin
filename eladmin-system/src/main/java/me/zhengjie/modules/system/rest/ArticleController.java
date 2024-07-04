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
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-06-29
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "文章管理")
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('article:list')")
    public void exportArticle(HttpServletResponse response, ArticleQueryCriteria criteria) throws IOException {
        articleService.download(articleService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询文章")
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<PageResult<ArticleDto>> queryArticle(ArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/find")
    @ApiOperation("查询文章")
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<List<ArticleDto>> find(ArticleQueryCriteria criteria){
        return new ResponseEntity<>(articleService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增文章")
    @ApiOperation("新增文章")
    @PreAuthorize("@el.check('article:add')")
    public ResponseEntity<Object> createArticle(@Validated @RequestBody Article resources){
        articleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改文章")
    @ApiOperation("修改文章")
    @PreAuthorize("@el.check('article:edit')")
    public ResponseEntity<Object> updateArticle(@Validated @RequestBody Article resources){
        articleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/enabled")
    @Log("启用/禁用")
    @ApiOperation("修改文章")
    @PreAuthorize("@el.check('article:edit')")
    public ResponseEntity<Object> enabled(@Validated @RequestBody Article resources){
        articleService.enabled(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除文章")
    @ApiOperation("删除文章")
    @PreAuthorize("@el.check('article:del')")
    public ResponseEntity<Object> deleteArticle(@RequestBody Long[] ids) {
        articleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
