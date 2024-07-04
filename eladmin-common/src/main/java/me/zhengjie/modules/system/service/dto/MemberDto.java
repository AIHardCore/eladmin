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

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseDTO;

import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-19
**/
@Data
public class MemberDto extends BaseDTO implements Serializable {

    /** openid */
    private String openId;

    /** 昵称 */
    private String nickName;

    /** 头像 */
    private String headImgUrl;

    /** 手机号 */
    private String phone;

    /** 用户类型 */
    private Boolean type;

    /** VIP到期时间 */
    private Timestamp vipExpiration;

    /** 状态 */
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
        if (vipExpiration == null) return;
        this.vipExpiration = vipExpiration;
        if (vipExpiration.compareTo(DateUtil.date()) >= 0){
            type = true;
        }else {
            type = false;
        }
    }
}
