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

import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.service.dto.*;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author hardcore
* @date 2024-06-29
**/
public interface ArticleService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<ArticleDto> queryAll(ArticleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ArticleDto>
    */
    List<ArticleDto> queryAll(ArticleQueryCriteria criteria);

    /**
     * 查询所有不在内丹学中的数据不分页
     * @param criteria 条件参数
     * @return List<ArticleDto>
     */
    List<ArticleDto> queryAllUnSelectedWithSpecial(ArticlesSpecialsQueryCriteria criteria);

    /**
     * 查询所有不在古科学中的数据不分页
     * @param criteria 条件参数
     * @return List<ArticleDto>
     */
    List<ArticleDto> queryAllUnSelectedWithRank(RankQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ArticleDto
     */
    ArticleDto findById(Long id);

    /**
    * 创建
    * @param resources /
    */
    void create(Article resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Article resources);

    /**
     * 启用/禁用
     * @param resources /
     */
    void enabled(Article resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ArticleDto> all, HttpServletResponse response) throws IOException;
}
