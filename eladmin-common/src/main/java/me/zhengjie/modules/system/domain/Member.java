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
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

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
    @Column(name = "`open_id`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "openId")
    private String openId;

    @Column(name = "`nick_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Column(name = "`head_img_url`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @Column(name = "`phone`")
    @ApiModelProperty(value = "手机号")
    private String phone;

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
    private Boolean enabled;

    public Boolean getType() {
        if (vipExpiration == null) return null;
        if (vipExpiration.compareTo(DateUtil.date()) >= 0){
            return true;
        }else {
            return false;
        }
    }

    public void setVipExpiration(Timestamp vipExpiration) {
        this.vipExpiration = vipExpiration;
        if (vipExpiration.compareTo(DateUtil.date()) >= 0){
            type = true;
        }else {
            type = false;
        }
    }

    public void copy(Member source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
