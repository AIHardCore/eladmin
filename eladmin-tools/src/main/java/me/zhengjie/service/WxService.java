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
package me.zhengjie.service;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import me.zhengjie.domain.WxConfig;
import me.zhengjie.domain.wx.AccessTokenInfo;
import me.zhengjie.domain.wx.WxUserInfo;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
public interface WxService {

    /**
     * 查询配置
     * @return WxConfig
     */
    WxConfig find();

    /**
     * 更新配置
     * @param wxConfig 支付宝配置
     * @return WxConfig
     */
    WxConfig config(WxConfig wxConfig);

    /**
     * 通过code换取网页授权access_token
     * @param code
     * @return
     */
    AccessTokenInfo getAccessTokenInfo(String code);

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * @param accessToken
     * @param openId
     * @return
     */
    WxUserInfo getUserInfo(String accessToken, String openId);

    /**
     * 微信支付预下单
     *
     * @param openId    下单人openid
     * @param orderNo   订单编号
     * @param orderName 订单名称
     * @param total     订单金额，单位：分
     * @return
     */
    PrepayWithRequestPaymentResponse prepay(String openId, String orderNo, String orderName, Integer total);
}
