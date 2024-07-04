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
import me.zhengjie.modules.system.domain.Produce;
import me.zhengjie.modules.system.repository.ProduceRepository;
import me.zhengjie.modules.system.service.ProduceService;
import me.zhengjie.modules.system.service.dto.ProduceDto;
import me.zhengjie.modules.system.service.dto.ProduceQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ProduceMapper;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-07-10
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
}
