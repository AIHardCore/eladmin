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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://eladmin.vip
* @description /
* @author hardcoer
* @date 2024-07-20
**/
@Entity
@Data
@Table(name="app_article_reading_log")
public class ArticleReadingLog extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`article`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "文章id")
    private Long article;

    @Column(name = "`open_id`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "用户openId")
    private String openId;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "类型")
    private Boolean type;

    @Column(name = "`ip`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "源ip")
    private String ip;

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

    public void copy(ArticleReadingLog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
