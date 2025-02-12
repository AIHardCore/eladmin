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
package me.zhengjie.modules.system.service.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.zhengjie.annotation.Query;

/**
* @website https://eladmin.vip
* @author hardcore
* @date 2025-01-09
**/
@Data
public class ArticlesSpecialsQueryCriteria{
    /** 精确 */
    @Query
    private Long specialId;

    @Query(propName = "title", type = Query.Type.INNER_LIKE, joinName = "article")
    private String title;

}
