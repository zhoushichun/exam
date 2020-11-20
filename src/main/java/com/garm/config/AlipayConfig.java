package com.garm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * 支付宝支付配置类
 * @author EDZ
 */
@Configuration
@Data
public class AlipayConfig {

    /**
     * 支付宝appid
     */
    @Value("${alipay.APPID}")
    private String APPID;

    /**
     * 私钥
     */
    @Value("${alipay.MERCHANTPRIVATEKEY}")
    private String MERCHANTPRIVATEKEY;
    /**
     * 公钥
     */
    @Value("${alipay.ALIPAYPUBLICKEY}")
    private String ALIPAYPUBLICKEY;
    /**
     * 异步回调路径
     */
    @Value("${alipay.NOTIFYURL}")
    private String NOTIFYURL;
    /**
     * 同步回调路径
     */
    @Value("${alipay.RETURNURL}")
    private String RETURNURL;
    /**
     * 签名类型
     */
    @Value("${alipay.SIGNTYPE}")
    private String SIGNTYPE;
    /**
     * 编码类型
     */
    @Value("${alipay.CHARSET}")
    private String CHARSET;
    /**
     * 统一下单路径
     */
    @Value("${alipay.GATEWAYURL}")
    private String GATEWAYURL;

}
