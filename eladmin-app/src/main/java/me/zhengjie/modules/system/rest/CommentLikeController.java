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
import me.zhengjie.modules.system.domain.CommentLike;
import me.zhengjie.modules.system.service.CommentLikeService;
import me.zhengjie.modules.system.service.dto.CommentLikeQueryCriteria;
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
import me.zhengjie.modules.system.service.dto.CommentLikeDto;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2024-07-17
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "点赞记录管理")
@RequestMapping("/api/commentLike")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportCommentLike(HttpServletResponse response, CommentLikeQueryCriteria criteria) throws IOException {
        commentLikeService.download(commentLikeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询点赞记录")
    @ApiOperation("查询点赞记录")
    public ResponseEntity<PageResult<CommentLikeDto>> queryCommentLike(CommentLikeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(commentLikeService.queryAll(criteria,pageable),HttpStatus.OK);
    }
}
