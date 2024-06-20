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

import me.zhengjie.modules.system.service.SectionService;
import me.zhengjie.modules.system.domain.Section;
import me.zhengjie.modules.system.mapstruct.SectionMapper;
import me.zhengjie.modules.system.repository.SectionRepository;
import me.zhengjie.modules.system.service.dto.SectionDto;
import me.zhengjie.modules.system.service.dto.SectionQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
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
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    @Override
    public PageResult<SectionDto> queryAll(SectionQueryCriteria criteria, Pageable pageable){
        Page<Section> page = sectionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sectionMapper::toDto));
    }

    @Override
    public List<SectionDto> queryAll(SectionQueryCriteria criteria){
        return sectionMapper.toDto(sectionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SectionDto findById(Integer id) {
        Section section = sectionRepository.findById(id).orElseGet(Section::new);
        ValidationUtil.isNull(section.getId(),"Section","id",id);
        return sectionMapper.toDto(section);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Section resources) {
        sectionRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Section resources) {
        Section section = sectionRepository.findById(resources.getId()).orElseGet(Section::new);
        ValidationUtil.isNull( section.getId(),"Section","id",resources.getId());
        section.copy(resources);
        sectionRepository.save(section);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            sectionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SectionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SectionDto section : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", section.getName());
            map.put("跳转页面", section.getUrl());
            map.put("图标", section.getIcon());
            map.put("排序", section.getSort());
            map.put("创建时间", section.getCreateTime());
            map.put("创建人", section.getCreateBy());
            map.put("更新时间", section.getUpdateTime());
            map.put("更新人", section.getUpdateBy());
            map.put("状态", section.getEnabled());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
