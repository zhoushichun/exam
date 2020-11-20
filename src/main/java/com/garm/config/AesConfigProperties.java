package com.garm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author ldx
 * @Date 2020/4/1 14:15
 * @Description
 * @Version 1.0.0
 */
@Data
@ConfigurationProperties(AesConfigProperties.PREFIX)
public class AesConfigProperties {

    public static final String PREFIX = "garm.boot.aes";

    /**aes key**/
    private String aesKey;

    /**aes iv**/
    private String aesIv;

    /**aes model **/
    private String aesMode;

    /**aes Padding**/
    private String aesPadding;

}
