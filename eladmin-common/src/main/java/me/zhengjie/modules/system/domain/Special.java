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

import com.alibaba.fastjson.annotation.JSONField;
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

import java.io.Serializable;
import java.util.Set;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-25
**/
@Entity
@Getter
@Setter
@NamedEntityGraph(name = "Special.withArticles",attributeNodes = {@NamedAttributeNode("articles")})
@Table(name="app_special")
public class Special extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`desc`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String desc;

    @Column(name = "`cover`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "封面")
    private String cover;

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Column(name = "`enabled`")
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    public void copy(Special source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
