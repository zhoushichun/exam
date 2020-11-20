package com.garm.common.redis.redissession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RedissonProperties.class})
@ConditionalOnProperty(
        name = {"abapi.cloud.redisson.lock.enabled"},
        matchIfMissing = true
)
public class RedissonAutoConfiguration {
    @Autowired
    private RedissonProperties redissonProperties;

    public RedissonAutoConfiguration() {
    }

    @Bean
    public RedissonUtil redissonUtil() {
        if (!this.redissonProperties.getEnabled()) {
            return null;
        } else {
            RedissonUtil.setHost(this.redissonProperties.getHost());
            RedissonUtil.setPort(this.redissonProperties.getPort());
            RedissonUtil.setAuth(this.redissonProperties.getPassword());
            RedissonUtil.setClusterNodes(this.redissonProperties.getClusterNodes());
            RedissonUtil instance = RedissonUtil.getInstance();
            RedissonUtil.initRedission();
            return instance;
        }
    }
}
