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
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-07-03
**/
@Entity
@Getter
@Setter
@NamedEntityGraph(
        name = "Rank.Graph",
        attributeNodes = {
                @NamedAttributeNode(value = "article")
        }
)
@Table(name="app_rank")
public class Rank extends BaseEntity implements Serializable {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "榜单类型")
    private Integer type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article")
    @ApiModelProperty(value = "文章")
    private Article article;

    @Column(name = "`sort`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "排名")
    private Integer sort;

    public void copy(Rank source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
