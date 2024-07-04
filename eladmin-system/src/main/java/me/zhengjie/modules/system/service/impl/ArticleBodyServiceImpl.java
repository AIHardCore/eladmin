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
import me.zhengjie.modules.system.service.dto.ArticleBodyDto;
import me.zhengjie.modules.system.service.dto.ArticleContentQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.ArticleBodyRepository;
import me.zhengjie.modules.system.service.ArticleBodyService;
import me.zhengjie.modules.system.service.mapstruct.ArticleBodyMapper;
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
public class ArticleBodyServiceImpl implements ArticleBodyService {

    private final ArticleBodyRepository articleBodyRepository;
    private final ArticleBodyMapper articleBodyMapper;

    @Override
    public PageResult<ArticleBodyDto> queryAll(ArticleContentQueryCriteria criteria, Pageable pageable){
        Page<ArticleBody> page = articleBodyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articleBodyMapper::toDto));
    }

    @Override
    public List<ArticleBodyDto> queryAll(ArticleContentQueryCriteria criteria){
        return articleBodyMapper.toDto(articleBodyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleBodyDto findById(Long id) {
        ArticleBody articleBody = articleBodyRepository.findById(id).orElseGet(me.zhengjie.modules.system.domain.ArticleBody::new);
        ValidationUtil.isNull(articleBody.getArticleId(),"ArticleBody","id",id);
        return articleBodyMapper.toDto(articleBody);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ArticleBody resources) {
        articleBodyRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ArticleBody resources) {
        ArticleBody articleBody = articleBodyRepository.findById(resources.getArticleId()).orElseGet(ArticleBody::new);
        ValidationUtil.isNull( articleBody.getArticleId(),"ArticleBody","id",resources.getArticleId());
        articleBody.copy(resources);
        articleBodyRepository.save(articleBody);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            articleBodyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ArticleBodyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleBodyDto articleBodyDto : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文章id", articleBodyDto.getArticleId());
            map.put("文章内容", articleBodyDto.getBody());
            map.put("状态", articleBodyDto.getEnabled());
            map.put("创建者", articleBodyDto.getCreateBy());
            map.put("更新者", articleBodyDto.getUpdateBy());
            map.put("创建日期", articleBodyDto.getCreateTime());
            map.put("更新时间", articleBodyDto.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
