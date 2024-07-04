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

import me.zhengjie.modules.system.domain.CommentLike;
import me.zhengjie.modules.system.service.dto.CommentLikeDto;
import me.zhengjie.modules.system.service.dto.CommentLikeQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.CommentLikeRepository;
import me.zhengjie.modules.system.service.CommentLikeService;
import me.zhengjie.modules.system.service.mapstruct.CommentLikeMapper;
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
* @date 2024-07-17
**/
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeMapper commentLikeMapper;

    @Override
    public PageResult<CommentLikeDto> queryAll(CommentLikeQueryCriteria criteria, Pageable pageable){
        Page<CommentLike> page = commentLikeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(commentLikeMapper::toDto));
    }

    @Override
    public List<CommentLikeDto> queryAll(CommentLikeQueryCriteria criteria){
        return commentLikeMapper.toDto(commentLikeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CommentLikeDto findById(CommentLike.CompositeKey id) {
        CommentLike commentLike = commentLikeRepository.findById(id).orElseGet(CommentLike::new);
        ValidationUtil.isNull(commentLike.getKey().getId(),"CommentLike","id",id);
        return commentLikeMapper.toDto(commentLike);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CommentLike resources) {
        commentLikeRepository.save(resources);
    }

    @Override
    public void download(List<CommentLikeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CommentLikeDto commentLike : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("留言id", commentLike.getKey().getId());
            map.put("发表用户的openId", commentLike.getKey().getTo());
            map.put("点赞用户的openId", commentLike.getKey().getFrom());
            map.put("创建者", commentLike.getCreateBy());
            map.put("更新者", commentLike.getUpdateBy());
            map.put("创建日期", commentLike.getCreateTime());
            map.put("更新时间", commentLike.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
