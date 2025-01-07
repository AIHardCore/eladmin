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
import me.zhengjie.modules.system.domain.CommentReply;
import me.zhengjie.modules.system.service.CommentReplyService;
import me.zhengjie.modules.system.service.dto.CommentReplyQueryCriteria;
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
import me.zhengjie.modules.system.service.dto.CommentReplyDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-22
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "留言回复管理")
@RequestMapping("/api/commentReply")
public class CommentReplyController {

    private final CommentReplyService commentReplyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('commentReply:list')")
    public void exportCommentReply(HttpServletResponse response, CommentReplyQueryCriteria criteria) throws IOException {
        commentReplyService.download(commentReplyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询留言回复")
    @ApiOperation("查询留言回复")
    @PreAuthorize("@el.check('commentReply:list')")
    public ResponseEntity<PageResult<CommentReplyDto>> queryCommentReply(CommentReplyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(commentReplyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增留言回复")
    @ApiOperation("新增留言回复")
    @PreAuthorize("@el.check('commentReply:add')")
    public ResponseEntity<Object> createCommentReply(@Validated @RequestBody CommentReply resources){
        commentReplyService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改留言回复")
    @ApiOperation("修改留言回复")
    @PreAuthorize("@el.check('commentReply:edit')")
    public ResponseEntity<Object> updateCommentReply(@Validated @RequestBody CommentReply resources){
        commentReplyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除留言回复")
    @ApiOperation("删除留言回复")
    @PreAuthorize("@el.check('commentReply:del')")
    public ResponseEntity<Object> deleteCommentReply(@RequestBody Long[] ids) {
        commentReplyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
