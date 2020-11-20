package com.garm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付配置类
 * @author EDZ
 */
@Configuration
@Data
public class WechatConfig {

    /**
     * 公众号appid
     */
    @Value("${wechat.APPID}")
    private String APPID;

    /**
     * 商户号
     */
    @Value("${wechat.MCH_ID}")
    private String MCH_ID;

    /**
     * 商户号密钥
     */
    @Value("${wechat.KEY}")
    private String KEY;

    /**
     * 地址
     */
    @Value("${wechat.SPBILL_CREATE_IP}")
    private String SPBILL_CREATE_IP;

    /**
     * 回调路径
     */
    @Value("${wechat.NOTIFY_URL}")
    private String NOTIFY_URL;

    /**
     * 交易类型
     */
    @Value("${wechat.TRADE_TYPE}")
    private String TRADE_TYPE;

    /**
     * 同一支付路径
     */
    @Value("${wechat.PLACEANORDER_URL}")
    private String PLACEANORDER_URL;
}
