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

import me.zhengjie.modules.system.domain.Special;
import me.zhengjie.modules.system.service.dto.SpecialDto;
import me.zhengjie.modules.system.service.dto.SpecialQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.SpecialRepository;
import me.zhengjie.modules.system.service.SpecialService;
import me.zhengjie.modules.system.service.mapstruct.SpecialMapper;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Sort;
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
* @date 2024-07-03
**/
@Service
@RequiredArgsConstructor
public class SpecialServiceImpl implements SpecialService {

    private final SpecialRepository specialRepository;
    private final SpecialMapper specialMapper;

    @Override
    public PageResult<SpecialDto> queryAll(SpecialQueryCriteria criteria, Pageable pageable){
        Page<Special> page = specialRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(specialMapper::toDto));
    }

    @Override
    public List<SpecialDto> queryAll(SpecialQueryCriteria criteria){
        return specialMapper.toDto(specialRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),Sort.by("sort")));
    }

    @Override
    @Transactional
    public SpecialDto findById(Long id) {
        Special special = specialRepository.findById(id).orElseGet(Special::new);
        ValidationUtil.isNull(special.getId(),"Special","id",id);
        return specialMapper.toDto(special);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Special resources) {
        specialRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Special resources) {
        Special special = specialRepository.findById(resources.getId()).orElseGet(Special::new);
        ValidationUtil.isNull( special.getId(),"Special","id",resources.getId());
        special.copy(resources);
        specialRepository.save(special);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            specialRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SpecialDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SpecialDto special : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("专栏名称", special.getName());
            map.put("封面", special.getCover());
            map.put("描述", special.getDesc());
            map.put("排序", special.getSort());
            map.put("状态：1启用、0禁用", special.getEnabled());
            map.put("创建者", special.getCreateBy());
            map.put("更新者", special.getUpdateBy());
            map.put("创建日期", special.getCreateTime());
            map.put("更新时间", special.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
