package com.garm.modules.exam.scheduling;


import com.garm.common.utils.DateUtils;
import com.garm.common.utils.SRedisUtil;
import com.garm.modules.exam.service.SysUserServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户vip定时器
 * @author rkg
 */
@Component
@Slf4j
public class UserServiceScheduling {

    @Autowired
    private SysUserServiceService userServiceService;


    /**
     * 用户服务类型状态该变
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void execute(){
        try{
            log.info("-------每日凌晨 改变用户服务状态----------");
            userServiceService.updateServiceType();
            String date = "scheduling::user::service::"+ DateUtils.getCurrentDayByDayPattern();
            SRedisUtil.set(date, true, 25*60*60L);
        }catch (Exception e){
            log.error("-------每日凌晨 改变用户服务状态----------异常：" + e.getMessage());
        }

    }

}
