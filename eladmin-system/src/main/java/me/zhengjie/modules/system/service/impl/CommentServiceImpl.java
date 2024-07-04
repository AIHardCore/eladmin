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
package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.Comment;
import me.zhengjie.modules.system.service.dto.CommentDto;
import me.zhengjie.modules.system.service.dto.CommentQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.CommentMapper;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.CommentRepository;
import me.zhengjie.modules.system.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcoer
* @date 2024-07-16
**/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public PageResult<CommentDto> queryAll(CommentQueryCriteria criteria, Pageable pageable){
        Page<Comment> page = commentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(commentMapper::toDto));
    }

    @Override
    public List<CommentDto> queryAll(CommentQueryCriteria criteria){
        return commentMapper.toDto(commentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseGet(Comment::new);
        ValidationUtil.isNull(comment.getId(),"Comment","id",id);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Comment resources) {
        commentRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Comment resources) {
        Comment comment = commentRepository.findById(resources.getId()).orElseGet(Comment::new);
        ValidationUtil.isNull( comment.getId(),"Comment","id",resources.getId());
        comment.copy(resources);
        commentRepository.save(comment);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            commentRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CommentDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CommentDto comment : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("内容", comment.getMessage());
            map.put("用户", String.format("{0}[{1}]",comment.getMember().getNickName(),comment.getMember().getOpenId()));
            map.put("点赞数", comment.getLikes());
            map.put("状态", comment.getEnabled());
            map.put("创建者", comment.getCreateBy());
            map.put("更新者", comment.getUpdateBy());
            map.put("创建日期", comment.getCreateTime());
            map.put("更新时间", comment.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
