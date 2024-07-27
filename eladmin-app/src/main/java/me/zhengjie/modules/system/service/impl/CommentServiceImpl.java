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

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.repository.CommentRepository;
import me.zhengjie.modules.system.repository.MemberRepository;
import me.zhengjie.modules.system.service.CommentService;
import me.zhengjie.modules.system.service.dto.CommentDto;
import me.zhengjie.modules.system.service.dto.CommentQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.CommentMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @website https://eladmin.vip
 * @description 服务实现
 * @author hardcoer
 * @date 2024-07-16
 **/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final JdbcTemplate jdbcTemplate;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MemberRepository memberRepository;

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
    public PageResult<CommentDto> page(CommentQueryCriteria criteria, Pageable pageable){
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        String sql = String.format("select t1.id,t1.message,t1.reply,t1.open_id,t1.likes,t1.enabled,t1.version,t1.create_by,t1.update_by,t1.create_time,t1.update_time,m.nick_name , " +
                        "case when exists ( select 1 from app_comment_like t2 where t2.id = t1.id and t2.from = '%s' ) then 1 else 0 end as active from app_comment t1 " +
                        "left join app_article a on a.id = t1.article_id left join app_member m on t1.open_id = m.open_id where t1.article_id = %s and (t1.enabled = 1 or t1.open_id = '%s') " +
                        "order by t1.likes desc,t1.create_time desc limit %s,%s"
                ,jwtUserDto.getUser().getOpenId(),criteria.getArticleId(),jwtUserDto.getUser().getOpenId(),pageable.getOffset(),pageable.getPageSize());
        List<Comment> comments = jdbcTemplate.query(sql,new CommentRowMapper());
        String countSql = String.format("select count(id) from app_comment where article_id = %s and (enabled = 1 or open_id = '%s') ",criteria.getArticleId(),jwtUserDto.getUser().getOpenId());
        int count = jdbcTemplate.queryForObject(countSql,Integer.class);
        Page<Comment> page = new PageImpl<>(comments,pageable,count);
        return PageUtil.toPage(page.map(commentMapper::toDto));
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
    public CommentDto create(Comment resources) {
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        Member member = memberRepository.findByOpenId(jwtUserDto.getUsername());
        resources.setMember(member);
        resources.setEnabled(false);
        resources.setLikes(0);
        commentRepository.save(resources);
        return commentMapper.toDto(resources);
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
    public void like(Long id) throws InterruptedException {
        Comment resources = commentRepository.findById(id).orElseGet(Comment::new);
        ValidationUtil.isNull( resources.getId(),"Comment","id",resources.getId());
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        Member member = memberRepository.findByOpenId(jwtUserDto.getUsername());
        CommentLike.CompositeKey key = new CommentLike.CompositeKey(resources.getId(),member.getOpenId(),resources.getMember().getOpenId());
        LikeParam likeParam = new LikeParam(key,true);
        BoundedBuffer.produce(likeParam);
    }

    @Override
    public void unlike(Long id) throws InterruptedException {
        Comment resources = commentRepository.findById(id).orElseGet(Comment::new);
        ValidationUtil.isNull( resources.getId(),"Comment","id",resources.getId());
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        Member member = memberRepository.findByOpenId(jwtUserDto.getUsername());
        CommentLike.CompositeKey key = new CommentLike.CompositeKey(resources.getId(),member.getOpenId(),resources.getMember().getOpenId());
        LikeParam likeParam = new LikeParam(key,false);
        BoundedBuffer.produce(likeParam);
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
            map.put("用户openId", comment.getMember());
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
