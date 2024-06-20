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

import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.domain.ArticleBody;
import me.zhengjie.modules.system.repository.ArticleBodyRepository;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    @Override
    public PageResult<ArticleDto> queryAll(ArticleQueryCriteria criteria, Pageable pageable){
        Page<Article> page = articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articleMapper::toDto));
    }

    @Override
    public List<ArticleDto> queryAll(ArticleQueryCriteria criteria){
        return articleMapper.toDto(articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleDto findById(Integer id) {
        Article article = articleRepository.findById(id).orElseGet(Article::new);
        ValidationUtil.isNull(article.getId(),"Article","id",id);
        return articleMapper.toDto(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Article resources) {
        articleRepository.save(resources);
        ArticleBody body = new ArticleBody();
        body.setArticleId(resources.getId());
        body.setContent(resources.getContent());
        articleBodyRepository.save(body);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Article resources) {
        Article article = articleRepository.findById(resources.getId()).orElseGet(Article::new);
        ValidationUtil.isNull( article.getId(),"Article","id",resources.getId());
        article.copy(resources);
        articleRepository.save(article);
        ArticleBody articleBody = articleBodyRepository.getById(article.getId());
        articleBody.setContent(article.getContent());
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            articleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ArticleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleDto article : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", article.getTitle());
            map.put("封面", article.getCover());
            map.put("版块", article.getSection().getName());
            map.put("预览内容", article.getPreview());
            //map.put("内容", article.getContent());
            map.put("状态", article.getEnabled());
            map.put("排序", article.getSort());
            map.put("阅读量", article.getReading());
            map.put("创建人", article.getCreateBy());
            map.put("修改人", article.getUpdateBy());
            map.put("创建时间", article.getCreateTime());
            map.put("更新时间", article.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
