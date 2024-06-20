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
import me.zhengjie.modules.system.domain.ArticleBody;
import me.zhengjie.modules.system.service.ArticleContentService;
import me.zhengjie.modules.system.service.dto.ArticleBodyDto;
import me.zhengjie.modules.system.service.dto.ArticleContentQueryCriteria;
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
@Api(tags = " 文章正文管理")
@RequestMapping("/api/articleContent")
public class ArticleContentController {

    private final ArticleContentService articleContentService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('articleContent:list')")
    public void exportArticleContent(HttpServletResponse response, ArticleContentQueryCriteria criteria) throws IOException {
        articleContentService.download(articleContentService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询 文章正文")
    @ApiOperation("查询 文章正文")
    @PreAuthorize("@el.check('articleContent:list')")
    public ResponseEntity<PageResult<ArticleBodyDto>> queryArticleContent(ArticleContentQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articleContentService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增 文章正文")
    @ApiOperation("新增 文章正文")
    @PreAuthorize("@el.check('articleContent:add')")
    public ResponseEntity<Object> createArticleContent(@Validated @RequestBody ArticleBody resources){
        articleContentService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改 文章正文")
    @ApiOperation("修改 文章正文")
    @PreAuthorize("@el.check('articleContent:edit')")
    public ResponseEntity<Object> updateArticleContent(@Validated @RequestBody ArticleBody resources){
        articleContentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除 文章正文")
    @ApiOperation("删除 文章正文")
    @PreAuthorize("@el.check('articleContent:del')")
    public ResponseEntity<Object> deleteArticleContent(@RequestBody Integer[] ids) {
        articleContentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
