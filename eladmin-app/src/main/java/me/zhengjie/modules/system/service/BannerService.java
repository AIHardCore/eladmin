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
package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.service.dto.BannerDto;
import me.zhengjie.modules.system.service.dto.BannerQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.List;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author hardcore
* @date 2024-07-03
**/
public interface BannerService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<BannerDto> queryAll(BannerQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BannerDto>
    */
    List<BannerDto> queryAll(BannerQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BannerDto
     */
    BannerDto findById(Long id);
}