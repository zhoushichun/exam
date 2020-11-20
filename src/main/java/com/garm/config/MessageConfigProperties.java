package com.garm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author EDZ
 */
@Data
@ConfigurationProperties(MessageConfigProperties.PREFIX)
public class MessageConfigProperties {

    public static final String PREFIX = "garm.boot.message";

    /**
     *  使用类型   lk-凌凯短信  aliyun-阿里云
     */
    private String provider = "lk";
    /**
     * 账号
     */
    private String id;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 是否发送 true-接入短信  false-不发短信
     */
    private Boolean isSend = true;

}
