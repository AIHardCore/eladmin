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
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-19
**/
@Entity
@Getter
@Setter
@Table(name="app_article")
public class Article extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "section_id")
    @ApiModelProperty(value = "版块")
    private Section section;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`cover`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "封面")
    private String cover;

    @Column(name = "`preview`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "预览内容")
    private String preview;

    @Transient
    @ApiModelProperty(value = "文章正文")
    private String content;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`reading`")
    @ApiModelProperty(value = "阅读量")
    private Integer reading;

    public void copy(Article source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
