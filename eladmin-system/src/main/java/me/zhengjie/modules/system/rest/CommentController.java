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
import me.zhengjie.modules.system.domain.Comment;
import me.zhengjie.modules.system.service.CommentService;
import me.zhengjie.modules.system.service.dto.CommentDto;
import me.zhengjie.modules.system.service.dto.CommentQueryCriteria;
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
* @author hardcoer
* @date 2024-07-16
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "留言管理")
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('comment:list')")
    public void exportComment(HttpServletResponse response, CommentQueryCriteria criteria) throws IOException {
        commentService.download(commentService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询留言")
    @ApiOperation("查询留言")
    @PreAuthorize("@el.check('comment:list')")
    public ResponseEntity<PageResult<CommentDto>> queryComment(CommentQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(commentService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增留言")
    @ApiOperation("新增留言")
    @PreAuthorize("@el.check('comment:add')")
    public ResponseEntity<Object> createComment(@Validated @RequestBody Comment resources){
        commentService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改留言")
    @ApiOperation("修改留言")
    @PreAuthorize("@el.check('comment:edit')")
    public ResponseEntity<Object> updateComment(@Validated @RequestBody Comment resources){
        commentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除留言")
    @ApiOperation("删除留言")
    @PreAuthorize("@el.check('comment:del')")
    public ResponseEntity<Object> deleteComment(@RequestBody Long[] ids) {
        commentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
