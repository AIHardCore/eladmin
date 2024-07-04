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

import me.zhengjie.modules.system.domain.Banner;
import me.zhengjie.modules.system.domain.Special;
import me.zhengjie.modules.system.repository.SpecialRepository;
import me.zhengjie.modules.system.service.dto.BannerDto;
import me.zhengjie.modules.system.service.dto.BannerQueryCriteria;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.BannerRepository;
import me.zhengjie.modules.system.service.BannerService;
import me.zhengjie.modules.system.service.mapstruct.BannerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author hardcore
* @date 2024-06-20
**/
@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final SpecialRepository specialRepository;

    @Override
    public PageResult<BannerDto> queryAll(BannerQueryCriteria criteria, Pageable pageable){
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Banner resources) {
        if (StringUtils.isBlank(resources.getImg())){
            Special special = specialRepository.findById(resources.getSpecial()).orElseGet(Special::new);
            resources.setImg(special.getCover());
        }
        bannerRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Banner resources) {
        Banner banner = bannerRepository.findById(resources.getId()).orElseGet(Banner::new);
        ValidationUtil.isNull( banner.getId(),"Banner","id",resources.getId());
        banner.copy(resources);
        if (StringUtils.isBlank(banner.getImg())){
            Special special = specialRepository.findById(resources.getSpecial()).orElseGet(Special::new);
            banner.setImg(special.getCover());
        }
        bannerRepository.save(banner);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            bannerRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<BannerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BannerDto banner : all) {
            Special special = specialRepository.findById(banner.getSpecial()).orElseGet(Special::new);
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("图片", banner.getImg());
            map.put("专栏", special.getName());
            map.put("排序", banner.getSort());
            map.put("状态", banner.getEnabled());
            map.put("开始时间", banner.getBeginTime());
            map.put("结束时间", banner.getEndTime());
            map.put("创建者", banner.getCreateBy());
            map.put("更新者", banner.getUpdateBy());
            map.put("创建日期", banner.getCreateTime());
            map.put("更新时间", banner.getUpdateTime());
            map.put("描述", banner.getDescribe());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
