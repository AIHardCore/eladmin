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
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Table(name="app_member")
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`appid`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "appid")
    private String appid;

    @Column(name = "`nick_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Column(name = "`avater`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "头像")
    private String avater;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户类型")
    private Boolean type;

    @Column(name = "`vip_expiration`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "VIP到期时间")
    private Timestamp vipExpiration;

    @Column(name = "`enabled`")
    @ApiModelProperty(value = "状态")
    private String enabled;

    public void copy(Member source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
