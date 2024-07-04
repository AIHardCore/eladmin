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

import cn.hutool.core.date.DateUtil;
import me.zhengjie.modules.system.domain.Banner;
import me.zhengjie.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.BannerRepository;
import me.zhengjie.modules.system.service.BannerService;
import me.zhengjie.modules.system.service.dto.BannerDto;
import me.zhengjie.modules.system.service.dto.BannerQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.BannerMapper;
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
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public PageResult<BannerDto> queryAll(BannerQueryCriteria criteria, Pageable pageable){
        criteria.setBeginTime(DateUtil.date().toTimestamp());
        criteria.setEndTime(DateUtil.date().toTimestamp());
        Page<Banner> page = bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(bannerMapper::toDto));
    }

    @Override
    public List<BannerDto> queryAll(BannerQueryCriteria criteria){
        return bannerMapper.toDto(bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public BannerDto findById(Long id) {
        Banner banner = bannerRepository.findById(id).orElseGet(Banner::new);
        ValidationUtil.isNull(banner.getId(),"Banner","id",id);
        return bannerMapper.toDto(banner);
    }
}
