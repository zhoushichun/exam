package com.garm.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import com.garm.common.utils.crypto.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author ldx
 * @Date 2020/4/1 14:13
 * @Description
 * @Version 1.0.0
 */
@Configuration
@EnableConfigurationProperties({AesConfigProperties.class})
@ConditionalOnProperty(name = "garm.boot.web.config.enabled", matchIfMissing = true)
public class AesAutoConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AesConfigProperties aesConfigProperties;

    @Bean
    public Boolean settingAes(){
        if(StrUtil.isNotEmpty(aesConfigProperties.getAesKey())){
            AES.KEY = aesConfigProperties.getAesKey();
        }

        if(StrUtil.isNotEmpty(aesConfigProperties.getAesIv())){
            AES.IV = aesConfigProperties.getAesIv();
        }

        if(StrUtil.isNotEmpty(aesConfigProperties.getAesMode())){
            Optional<Mode> first = Arrays.asList(Mode.values()).stream().filter(e -> e.toString().equals(aesConfigProperties.getAesMode())).findFirst();
            if(first.isPresent()){
                AES.MODE = aesConfigProperties.getAesMode();
            }
        }

        if(StrUtil.isNotEmpty(aesConfigProperties.getAesPadding())){
            Optional<Padding> first = Arrays.asList(Padding.values()).stream().filter(e -> e.toString().equals(aesConfigProperties.getAesPadding())).findFirst();
            if(first.isPresent()){
                AES.PADDING = aesConfigProperties.getAesPadding();
            }
        }

        logger.info("init aes success ");
        return true;
    }

}
