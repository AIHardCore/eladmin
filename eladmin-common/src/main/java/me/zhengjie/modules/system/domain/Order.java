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
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-06-30
**/
@Entity
@Data
@Table(name="app_order")
public class Order extends BaseEntity implements Serializable {

    @Id
    @Column(name = "`out_trade_no`")
    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @Column(name = "`transaction_id`")
    @ApiModelProperty(value = "微信订单号")
    private String transactionId;

    @Column(name = "`produce_id`")
    @ApiModelProperty(value = "商品id")
    private Integer produceId;

    @ManyToOne
    @JoinColumn(name = "open_id")
    @NotBlank
    @ApiModelProperty(value = "用户")
    Member member;

    @Column(name = "`amount`")
    @ApiModelProperty(value = "价格")
    private Integer amount;

    @Column(name = "`status`")
    @ApiModelProperty(value = "状态")
    private int status;

    @Column(name = "`msg`")
    @ApiModelProperty(value = "消息或异常")
    private String msg;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "订单备注")
    private String remark;

    public void copy(Order source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
