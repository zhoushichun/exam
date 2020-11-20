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
@ConfigurationProperties(SnowflakeConfigProperties.PREFIX)
public class SnowflakeConfigProperties {

    public static final String PREFIX = "garm.boot.snowflake";

    /**work id  default 1L **/
    private Long snowflakeWorkerid = 1L;

    /**data id default 1L **/
    private Long snowflakeDatacenterid = 1L;

}
