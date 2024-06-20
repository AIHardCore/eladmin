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

import me.zhengjie.modules.system.domain.ArticleBody;
import me.zhengjie.modules.system.mapstruct.ArticleContentMapper;
import me.zhengjie.modules.system.repository.ArticleContentRepository;
import me.zhengjie.modules.system.service.dto.ArticleBodyDto;
import me.zhengjie.modules.system.service.dto.ArticleContentQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.service.ArticleContentService;
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
* @date 2024-06-19
**/
@Service
@RequiredArgsConstructor
public class ArticleContentServiceImpl implements ArticleContentService {

    private final ArticleContentRepository articleContentRepository;
    private final ArticleContentMapper articleContentMapper;

    @Override
    public PageResult<ArticleBodyDto> queryAll(ArticleContentQueryCriteria criteria, Pageable pageable){
        Page<ArticleBody> page = articleContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articleContentMapper::toDto));
    }

    @Override
    public List<ArticleBodyDto> queryAll(ArticleContentQueryCriteria criteria){
        return articleContentMapper.toDto(articleContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleBodyDto findById(Integer id) {
        ArticleBody articleBody = articleContentRepository.findById(id).orElseGet(me.zhengjie.modules.system.domain.ArticleBody::new);
        ValidationUtil.isNull(articleBody.getId(),"ArticleBody","id",id);
        return articleContentMapper.toDto(articleBody);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ArticleBody resources) {
        articleContentRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ArticleBody resources) {
        ArticleBody articleBody = articleContentRepository.findById(resources.getId()).orElseGet(ArticleBody::new);
        ValidationUtil.isNull( articleBody.getId(),"ArticleBody","id",resources.getId());
        articleBody.copy(resources);
        articleContentRepository.save(articleBody);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            articleContentRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ArticleBodyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleBodyDto articleContent : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文章id", articleContent.getArticleId());
            map.put("文章内容", articleContent.getContent());
            map.put("状态", articleContent.getEnabled());
            map.put("创建者", articleContent.getCreateBy());
            map.put("更新者", articleContent.getUpdateBy());
            map.put("创建日期", articleContent.getCreateTime());
            map.put("更新时间", articleContent.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
