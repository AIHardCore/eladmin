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
import me.zhengjie.modules.system.domain.Comment;
import me.zhengjie.modules.system.service.CommentService;
import me.zhengjie.modules.system.service.dto.CommentDto;
import me.zhengjie.modules.system.service.dto.CommentQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.enums.LogTypeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @website https://eladmin.vip
* @author hardcoer
* @date 2024-07-16
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "留言管理")
@RequestMapping("/app/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Log(type = LogTypeEnum.APP,module = "留言")
    @ApiOperation("查询留言")
    public ResponseEntity<PageResult<CommentDto>> queryComment(CommentQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(commentService.page(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log(type = LogTypeEnum.APP,module = "留言")
    @ApiOperation("新增留言")
    public ResponseEntity<Object> createComment(@Validated @RequestBody Comment resources){
        CommentDto commentDto = commentService.create(resources);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    @PutMapping("/like/{id}")
    @Log(type = LogTypeEnum.APP,module = "留言")
    @ApiOperation("点赞留言")
    public ResponseEntity<Object> like(@PathVariable Long id) throws InterruptedException {
        commentService.like(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("unlike/{id}")
    @Log(type = LogTypeEnum.APP,module = "留言")
    @ApiOperation("取消留言点赞")
    public ResponseEntity<Object> unlike(@PathVariable Long id) throws InterruptedException {
        commentService.unlike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
