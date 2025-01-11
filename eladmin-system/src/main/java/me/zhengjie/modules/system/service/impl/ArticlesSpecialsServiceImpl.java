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
import me.zhengjie.modules.system.domain.ArticlesSpecials;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.ArticlesSpecialsRepository;
import me.zhengjie.modules.system.service.ArticlesSpecialsService;
import me.zhengjie.modules.system.service.dto.ArticlesSpecialsDto;
import me.zhengjie.modules.system.service.dto.ArticlesSpecialsQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ArticlesSpecialsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
* @date 2025-01-09
**/
@Service
@RequiredArgsConstructor
public class ArticlesSpecialsServiceImpl implements ArticlesSpecialsService {

    private final ArticlesSpecialsRepository articlesSpecialsRepository;
    private final ArticlesSpecialsMapper articlesSpecialsMapper;
    private final ArticleRepository articleRepository;

    @Override
    public PageResult<ArticlesSpecialsDto> queryAll(ArticlesSpecialsQueryCriteria criteria, Pageable pageable){
        Page<ArticlesSpecials> page = articlesSpecialsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articlesSpecialsMapper::toDto));
    }

    @Override
    public List<ArticlesSpecialsDto> queryAll(ArticlesSpecialsQueryCriteria criteria){
        return articlesSpecialsMapper.toDto(articlesSpecialsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticlesSpecialsDto findById(Long specialId) {
        ArticlesSpecials articlesSpecials = articlesSpecialsRepository.findById(specialId).orElseGet(ArticlesSpecials::new);
        ValidationUtil.isNull(articlesSpecials.getSpecialId(),"ArticlesSpecials","specialId",specialId);
        return articlesSpecialsMapper.toDto(articlesSpecials);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ArticlesSpecials resources) {
        articlesSpecialsRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ArticlesSpecials resources) {
        ArticlesSpecials articlesSpecials = articlesSpecialsRepository.findById(resources.getId()).orElseGet(ArticlesSpecials::new);
        ValidationUtil.isNull( articlesSpecials.getSpecialId(),"ArticlesSpecials","id",resources.getSpecialId());
        articlesSpecials.copy(resources);
        articlesSpecialsRepository.save(articlesSpecials);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long specialId : ids) {
            articlesSpecialsRepository.deleteById(specialId);
        }
    }

    @Override
    public void download(List<ArticlesSpecialsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticlesSpecialsDto articlesSpecials : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("排序", articlesSpecials.getSort());
            map.put("创建者", articlesSpecials.getCreateBy());
            map.put("更新者", articlesSpecials.getUpdateBy());
            map.put("创建日期", articlesSpecials.getCreateTime());
            map.put("更新时间", articlesSpecials.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
