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
import me.zhengjie.base.BaseDTO;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-25
**/
@Data
public class SpecialDto extends BaseDTO implements Serializable {

    private Integer id;

    /** 文章列表 */
    private Set<ArticleDto> articles;

    /** 名称 */
    private String name;

    /** 描述 */
    private String desc;

    /** 封面 */
    private String cover;

    /** 排序 */
    private Integer sort;

    /** 状态 */
    private Boolean enabled;

}
