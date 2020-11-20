//
//
//package com.garm.modules.sys.redis;
//
//
//import com.garm.common.utils.RedisKeys;
//import com.garm.common.utils.RedisUtils;
//import com.garm.modules.sys.entity.SysConfigEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 系统配置Redis
// *
// * @author
// */
//@Component
//public class SysConfigRedis {
//    @Autowired
//    private RedisUtils redisUtils;
//
//    public void saveOrUpdate(SysConfigEntity config) {
//        if(config == null){
//            return ;
//        }
//        String key = RedisKeys.getSysConfigKey(config.getParamKey());
//        redisUtils.set(key, config);
//    }
//
//    public void delete(String configKey) {
//        String key = RedisKeys.getSysConfigKey(configKey);
//        redisUtils.delete(key);
//    }
//
//    public SysConfigEntity get(String configKey){
//        String key = RedisKeys.getSysConfigKey(configKey);
//        return redisUtils.get(key, SysConfigEntity.class);
//    }
//}
