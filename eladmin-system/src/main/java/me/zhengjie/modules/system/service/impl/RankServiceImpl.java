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

import me.zhengjie.modules.system.domain.Rank;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.RankRepository;
import me.zhengjie.modules.system.service.RankService;
import me.zhengjie.modules.system.service.dto.RankDto;
import me.zhengjie.modules.system.service.dto.RankQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.RankMapper;
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
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;
    private final RankMapper rankMapper;

    @Override
    public PageResult<RankDto> queryAll(RankQueryCriteria criteria, Pageable pageable){
        Page<Rank> page = rankRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rankMapper::toDto));
    }

    @Override
    public List<RankDto> queryAll(RankQueryCriteria criteria){
        return rankMapper.toDto(rankRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public RankDto findById(Integer id) {
        Rank rank = rankRepository.findById(id).orElseGet(Rank::new);
        ValidationUtil.isNull(rank.getId(),"Rank","id",id);
        return rankMapper.toDto(rank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Rank resources) {
        rankRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Rank resources) {
        Rank rank = rankRepository.findById(resources.getId()).orElseGet(Rank::new);
        ValidationUtil.isNull( rank.getId(),"Rank","id",resources.getId());
        rank.copy(resources);
        rankRepository.save(rank);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            rankRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RankDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RankDto rank : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("榜单类型", rank.getType());
            map.put("文章id", rank.getArticle());
            map.put("排名", rank.getSort());
            map.put("创建者", rank.getCreateBy());
            map.put("更新者", rank.getUpdateBy());
            map.put("创建日期", rank.getCreateTime());
            map.put("更新时间", rank.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}