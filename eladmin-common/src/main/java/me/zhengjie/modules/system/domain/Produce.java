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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
* @website https://eladmin.vip
* @description /
* @author hardcore
* @date 2024-07-09
**/
@Entity
@Getter
@Setter
@Table(name="app_produce")
public class Produce extends BaseEntity implements Serializable {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`price`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "价格(分)")
    private Long price;

    @Column(name = "`sort`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Transient
    @ApiModelProperty(value = "价格(元)")
    private BigDecimal priceOfYuan;

    @Column(name = "`time_unit`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "时间单位")
    private Integer timeUnit;

    @Column(name = "`time_length`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "时间长度")
    private Integer timeLength;

    @Column(name = "`enabled`")
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    public BigDecimal getPriceOfYuan() {
        return BigDecimal.valueOf(this.price == null ? 0 : this.price).setScale(2, RoundingMode.HALF_UP).divide(new BigDecimal(100));
    }

    public void copy(Produce source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
