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

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-19
**/
@Data
public class SectionDto extends BaseDTO implements Serializable {

    private Integer id;

    /** 名称 */
    private String name;

    /** 跳转页面 */
    private String url;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    /** 创建时间
 */
    private Timestamp createTime;

    /** 创建人 */
    private String createBy;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 更新人 */
    private String updateBy;

    /** 状态 */
    private Boolean enabled;
}
