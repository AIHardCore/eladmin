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
package me.zhengjie.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-20
**/
@Entity
@Data
@Table(name="app_banner")
public class Banner implements Serializable {

    @Id
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`img`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图片")
    private String img;

    @Column(name = "`url`")
    @ApiModelProperty(value = "跳转地址")
    private String url;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @Column(name = "`begin_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Timestamp beginTime;

    @Column(name = "`end_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Timestamp endTime;

    @Column(name = "`create_by`")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "`update_by`")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "`describe`")
    @ApiModelProperty(value = "描述")
    private String describe;

    public void copy(Banner source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
