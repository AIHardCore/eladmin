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
import me.zhengjie.modules.system.domain.ArticlesSpecials;
import me.zhengjie.modules.system.service.ArticlesSpecialsService;
import me.zhengjie.modules.system.service.dto.ArticlesSpecialsQueryCriteria;
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
import me.zhengjie.modules.system.service.dto.ArticlesSpecialsDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2025-01-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "内丹学-文章管理")
@RequestMapping("/api/articlesSpecials")
public class ArticlesSpecialsController {

    private final ArticlesSpecialsService articlesSpecialsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('articlesSpecials:list')")
    public void exportArticlesSpecials(HttpServletResponse response, ArticlesSpecialsQueryCriteria criteria) throws IOException {
        articlesSpecialsService.download(articlesSpecialsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询内丹学-文章")
    @ApiOperation("查询内丹学-文章")
    @PreAuthorize("@el.check('articlesSpecials:list')")
    public ResponseEntity<PageResult<ArticlesSpecialsDto>> queryArticlesSpecials(ArticlesSpecialsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articlesSpecialsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增内丹学-文章")
    @ApiOperation("新增内丹学-文章")
    @PreAuthorize("@el.check('articlesSpecials:add')")
    public ResponseEntity<Object> createArticlesSpecials(@Validated @RequestBody ArticlesSpecials resources){
        articlesSpecialsService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改内丹学-文章")
    @ApiOperation("修改内丹学-文章")
    @PreAuthorize("@el.check('articlesSpecials:edit')")
    public ResponseEntity<Object> updateArticlesSpecials(@Validated @RequestBody ArticlesSpecials resources){
        articlesSpecialsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除内丹学-文章")
    @ApiOperation("删除内丹学-文章")
    @PreAuthorize("@el.check('articlesSpecials:del')")
    public ResponseEntity<Object> deleteArticlesSpecials(@RequestBody Long[] ids) {
        articlesSpecialsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}