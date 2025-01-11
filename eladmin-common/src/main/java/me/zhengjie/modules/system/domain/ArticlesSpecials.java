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
import me.zhengjie.base.BaseEntity;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2025-01-09
**/
@Entity
@Data
@Table(name="app_articles_specials")
public class ArticlesSpecials extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "文章")
    @JoinColumn(name = "article_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Article article;

    @Column(name = "`special_id`")
    @ApiModelProperty(value = "内丹学")
    private Long specialId;

    @Column(name = "`sort`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "排序")
    private Integer sort;

    public void copy(ArticlesSpecials source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
