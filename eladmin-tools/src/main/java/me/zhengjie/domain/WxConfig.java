package me.zhengjie.domain;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tool_wx_config")
public class WxConfig implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 公众号AppId
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 公众号appSecret
     */
    @Column(name = "app_secret")
    private String appSecret;

    /**
     * 直连商户号
     */
    @Column(name = "mch_id")
    private String mchId;

    /**
     * APIv3密钥
     */
    @Column(name = "api_v3_key")
    private String apiV3Key;

    /**
     * 商户API私钥路径
     */
    @Column(name = "private_key_path")
    private String privateKeyPath;

    /**
     * 商户证书序列号
     */
    @Column(name = "merchant_serial_number ")
    private String merchantSerialNumber ;

    /**
     * 支付Url
     */
    @Column(name = "pay_url")
    private String payUrl;

    /**
     * 支付通知的回调地址
     */
    @Column(name = "notify_url")
    private String notifyUrl;

    /**
     * 获取用户code的回调地址
     */
    @Column(name = "redirect_uri")
    private String redirectUri;
    /**
     * 获取用户code的url
     */
    @Column(name = "code_url")
    private String codeUrl;
    /**
     * 获取access_token的url
     */
    @Column(name = "access_token_url")
    private String accessTokenUrl;
    /**
     * 刷新access_token的url
     */
    @Column(name = "refresh_access_token_url")
    private String refreshAccessTokenUrl;
    /**
     * 获取用户信息的url
     */
    @Column(name = "user_info_url")
    private String userInfoUrl;
}
