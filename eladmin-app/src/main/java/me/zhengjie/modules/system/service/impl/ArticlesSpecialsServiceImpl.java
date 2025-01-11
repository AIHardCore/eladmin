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

import me.zhengjie.modules.system.domain.ArticlesSpecials;
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
* @date 2025-01-11
**/
@Service
@RequiredArgsConstructor
public class ArticlesSpecialsServiceImpl implements ArticlesSpecialsService {

    private final ArticlesSpecialsRepository articlesSpecialsRepository;
    private final ArticlesSpecialsMapper articlesSpecialsMapper;

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
    public ArticlesSpecialsDto findById(Long id) {
        ArticlesSpecials articlesSpecials = articlesSpecialsRepository.findById(id).orElseGet(ArticlesSpecials::new);
        ValidationUtil.isNull(articlesSpecials.getId(),"ArticlesSpecials","id",id);
        return articlesSpecialsMapper.toDto(articlesSpecials);
    }
}
