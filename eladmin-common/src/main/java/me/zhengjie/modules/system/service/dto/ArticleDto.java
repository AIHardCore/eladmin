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

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import me.zhengjie.modules.system.domain.Special;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-29
**/
@Data
public class ArticleDto implements Serializable {

    private Integer id;

    /** 专栏列表 */
    private Set<SpecialSmallDto> specials;

    /** 标题 */
    private String title;

    /** 封面 */
    private String cover;

    /** 预览内容 */
    private String preview;

    /** 内容 */
    private String body = "";

    /** 状态 */
    private Boolean enabled;

    /** 排序 */
    private Integer sort;

    /** 阅读量 */
    private Integer reading;

    /** 创建人 */
    private String createBy;

    /** 修改人 */
    private String updateBy;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}
