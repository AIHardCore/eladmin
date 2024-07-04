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

import me.zhengjie.modules.system.domain.Produce;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.ProduceRepository;
import me.zhengjie.modules.system.service.ProduceService;
import me.zhengjie.modules.system.service.dto.ProduceDto;
import me.zhengjie.modules.system.service.dto.ProduceQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ProduceMapper;
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
* @date 2024-07-09
**/
@Service
@RequiredArgsConstructor
public class ProduceServiceImpl implements ProduceService {

    private final ProduceRepository produceRepository;
    private final ProduceMapper produceMapper;

    @Override
    public PageResult<ProduceDto> queryAll(ProduceQueryCriteria criteria, Pageable pageable){
        Page<Produce> page = produceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(produceMapper::toDto));
    }

    @Override
    public List<ProduceDto> queryAll(ProduceQueryCriteria criteria){
        return produceMapper.toDto(produceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ProduceDto findById(Integer id) {
        Produce produce = produceRepository.findById(id).orElseGet(Produce::new);
        ValidationUtil.isNull(produce.getId(),"Produce","id",id);
        return produceMapper.toDto(produce);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Produce resources) {
        produceRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Produce resources) {
        Produce produce = produceRepository.findById(resources.getId()).orElseGet(Produce::new);
        ValidationUtil.isNull( produce.getId(),"Produce","id",resources.getId());
        produce.copy(resources);
        produceRepository.save(produce);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            produceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ProduceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProduceDto produce : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", produce.getName());
            map.put("价格", produce.getPrice());
            map.put("时间单位", produce.getTimeUnit());
            map.put("时间长度", produce.getTimeLength());
            map.put("状态", produce.getEnabled());
            map.put("创建者", produce.getCreateBy());
            map.put("更新者", produce.getUpdateBy());
            map.put("创建日期", produce.getCreateTime());
            map.put("更新时间", produce.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}