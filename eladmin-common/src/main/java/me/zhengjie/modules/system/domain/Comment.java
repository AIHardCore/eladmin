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

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcoer
* @date 2024-07-16
**/
@Entity
@Data
@Table(name="app_comment")
public class Comment extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch=FetchType.LAZY)
    @ApiModelProperty(value = "文章")
    private Article article;

    @Column(name = "`message`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内容")
    private String message;

    @Column(name = "`reply`")
    @ApiModelProperty(value = "回复内容")
    private String reply;

    @ManyToOne
    @JoinColumn(name = "open_id")
    @NotBlank
    @ApiModelProperty(value = "用户")
    Member member;

    @Column(name = "`likes`")
    @NotNull
    @ApiModelProperty(value = "点赞数")
    private int likes = 0;

    @Version
    @Column(name = "`version`")
    @ApiModelProperty(value = "乐观锁")
    private int version;

    @Column(name = "`real`")
    @ApiModelProperty(value = "是否真是留言")
    private Boolean real = true;

    @Column(name = "`enabled`")
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @Transient
    private int active;

    public void copy(Comment source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
