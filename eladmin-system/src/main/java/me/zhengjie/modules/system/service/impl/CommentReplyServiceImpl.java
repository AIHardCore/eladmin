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

import me.zhengjie.modules.system.domain.CommentReply;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.CommentReplyRepository;
import me.zhengjie.modules.system.service.CommentReplyService;
import me.zhengjie.modules.system.service.dto.CommentReplyDto;
import me.zhengjie.modules.system.service.dto.CommentReplyQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.CommentReplyMapper;
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
* @author hardcore
* @date 2024-07-22
**/
@Service
@RequiredArgsConstructor
public class CommentReplyServiceImpl implements CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;
    private final CommentReplyMapper commentReplyMapper;

    @Override
    public PageResult<CommentReplyDto> queryAll(CommentReplyQueryCriteria criteria, Pageable pageable){
        Page<CommentReply> page = commentReplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(commentReplyMapper::toDto));
    }

    @Override
    public List<CommentReplyDto> queryAll(CommentReplyQueryCriteria criteria){
        return commentReplyMapper.toDto(commentReplyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CommentReplyDto findById(Long id) {
        CommentReply commentReply = commentReplyRepository.findById(id).orElseGet(CommentReply::new);
        ValidationUtil.isNull(commentReply.getId(),"CommentReply","id",id);
        return commentReplyMapper.toDto(commentReply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CommentReply resources) {
        commentReplyRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CommentReply resources) {
        CommentReply commentReply = commentReplyRepository.findById(resources.getId()).orElseGet(CommentReply::new);
        ValidationUtil.isNull( commentReply.getId(),"CommentReply","id",resources.getId());
        commentReply.copy(resources);
        commentReplyRepository.save(commentReply);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            commentReplyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CommentReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CommentReplyDto commentReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("留言id", commentReply.getCommentId());
            map.put("回复内容", commentReply.getContent());
            map.put("创建者", commentReply.getCreateBy());
            map.put("更新者", commentReply.getUpdateBy());
            map.put("创建日期", commentReply.getCreateTime());
            map.put("更新时间", commentReply.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
