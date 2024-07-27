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
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.domain.ArticleBody;
import me.zhengjie.modules.system.domain.Member;
import me.zhengjie.modules.system.repository.ArticleBodyRepository;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.modules.system.repository.MemberRepository;
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.async.AsyncArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ArticleMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-06-19
**/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ArticleBodyRepository articleBodyRepository;
    private final MemberRepository memberRepository;
    private final AsyncArticleService asyncArticleService;

    @Override
    public PageResult<ArticleDto> queryAll(ArticleQueryCriteria criteria, Pageable pageable){
        Page<Article> page = articleRepository.findAll(((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)),pageable);
        return PageUtil.toPage(page.map(articleMapper::toDto));
    }

    @Override
    public List<ArticleDto> queryAll(ArticleQueryCriteria criteria){
        return articleMapper.toDto(articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleDto findById(Long id, HttpServletRequest request) {
        String ip = StringUtils.getIp(request);
        Article article = articleRepository.findByIdAndEnabled(id,true).orElseGet(Article::new);
        ValidationUtil.isNull(article.getId(),"Article","id",id);
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityUtils.getCurrentUser();
        Member member = memberRepository.findById(jwtUserDto.getUser().getOpenId()).orElseGet(Member::new);
        ValidationUtil.isNull(member.getOpenId(),"Member","openId",jwtUserDto.getUser().getOpenId());
        if (member.getType()){
            ArticleBody articleBody = articleBodyRepository.findById(article.getId()).orElseGet(ArticleBody::new);
            article.setBody(articleBody.getBody());
            asyncArticleService.read(article.getId(),member.getOpenId(), true, ip);
        }else {
            asyncArticleService.read(article.getId(),member.getOpenId(), false, ip);
        }
        return articleMapper.toDto(article);
    }
}
