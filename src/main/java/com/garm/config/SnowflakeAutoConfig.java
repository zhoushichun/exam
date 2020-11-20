package com.garm.config;

import com.garm.common.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ldx
 * @Date 2020/4/1 14:13
 * @Description
 * @Version 1.0.0
 */
@Configuration
@EnableConfigurationProperties({SnowflakeConfigProperties.class})
@ConditionalOnProperty(name = "garm.boot.web.config.enabled", matchIfMissing = true)
public class SnowflakeAutoConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SnowflakeConfigProperties snowflakeConfigProperties;

    @Bean
    public Boolean settingSnowflake(){
        IdUtil.WORKERID = snowflakeConfigProperties.getSnowflakeWorkerid();
        IdUtil.DATACENTERID = snowflakeConfigProperties.getSnowflakeDatacenterid();
        logger.info("init snowflake id success ");
        return true;
    }

}
