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
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.dto.ArticlesSpecialsDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2025-01-11
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "内丹学-文章管理")
@RequestMapping("/app/articlesSpecials")
public class ArticlesSpecialsController {

    private final ArticlesSpecialsService articlesSpecialsService;

    @GetMapping
    @Log("查询内丹学-文章")
    @ApiOperation("查询内丹学-文章")
    public ResponseEntity<PageResult<ArticlesSpecialsDto>> queryArticlesSpecials(ArticlesSpecialsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articlesSpecialsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/all")
    @Log("查询内丹学-文章")
    @ApiOperation("查询内丹学-文章")
    public ResponseEntity<List<ArticlesSpecialsDto>> all(ArticlesSpecialsQueryCriteria criteria){
        return new ResponseEntity<>(articlesSpecialsService.queryAll(criteria),HttpStatus.OK);
    }
}
