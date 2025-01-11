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

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.domain.Member;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-07-06
**/
@Getter
@Setter
public class OrderDto extends BaseDTO implements Serializable {

    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /** 微信订单号 */
    private String transactionId;

    /** 商品id */
    private Long produceId;

    /** 商户订单号 */
    private String outTradeNo;

    /** APP用户 */
    private Member member;

    /** 价格 */
    private BigDecimal amount;

    /** 状态 */
    private int status;

    /** 消息或异常 */
    private String msg;
}
