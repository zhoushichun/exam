package com.garm.config;


import cn.hutool.core.util.StrUtil;
import com.garm.common.utils.message.MessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({MessageConfigProperties.class})
@ConditionalOnProperty(name = "garm.boot.web.config.enabled", matchIfMissing = true)
public class MessageConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageConfigProperties messageConfigProperties;
    @Bean
    public Boolean settingJMessage(){
        if(StrUtil.isNotEmpty(messageConfigProperties.getProvider())){
            MessageConstant.provider = messageConfigProperties.getProvider();
        }
        if(StrUtil.isNotEmpty(messageConfigProperties.getId())){
            MessageConstant.id = messageConfigProperties.getId();
        }
        if(StrUtil.isNotEmpty(messageConfigProperties.getPwd())){
            MessageConstant.pwd = messageConfigProperties.getPwd();
        }
        if(messageConfigProperties.getIsSend() != null){
            MessageConstant.isSend = messageConfigProperties.getIsSend();
        }

        logger.info("init aes success ");
        return true;
    }
}
