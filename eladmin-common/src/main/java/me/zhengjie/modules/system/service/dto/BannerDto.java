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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-20
**/
@Data
public class BannerDto implements Serializable {

    private Integer id;

    /** 图片 */
    private String img;

    /** 跳转地址 */
    private String url;

    /** 排序 */
    private Integer sort;

    /** 状态 */
    private Boolean enabled;

    /** 开始时间 */
    private Timestamp beginTime;

    /** 结束时间 */
    private Timestamp endTime;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 描述 */
    private String describe;
}