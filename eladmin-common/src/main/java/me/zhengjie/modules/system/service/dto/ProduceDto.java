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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-07-09
**/
@Data
public class ProduceDto implements Serializable {

    private Integer id;

    /** 名称 */
    private String name;

    /** 价格(分) */
    private Integer price;

    /** 排序 */
    private Integer sort;

    /** 价格(元) */
    private BigDecimal priceOfYuan;

    /** 时间单位 */
    private Integer timeUnit;

    /** 时间长度 */
    private Integer timeLength;

    /** 状态 */
    private Boolean enabled;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}
