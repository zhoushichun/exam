package com.garm.modules.exam.scheduling;


import com.garm.common.utils.DateUtils;
import com.garm.common.utils.SRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动调用类
 * @author rkg
 */
@Component
@Slf4j
public class TaskApplicationRunner implements ApplicationRunner {

    @Autowired
    private ExamOfficialTestScheduling officialTestScheduling;

    @Autowired
    private UserServiceScheduling userServiceScheduling;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        try{
            String testend_keys = "scheduling::official::testend::";

            String paper_keys = "scheduling::official::paper::";

            String service_keys = "scheduling::user::service::";

            String date = DateUtils.getCurrentDayByDayPattern();
            if(SRedisUtil.get(testend_keys+date) == null){
                officialTestScheduling.execute();
            }
            if(SRedisUtil.get(paper_keys+date) == null){
                officialTestScheduling.paperStatistical();
            }
            if(SRedisUtil.get(service_keys+date) == null){
                userServiceScheduling.execute();
            }
        }catch (Exception e){
            log.error("全局异常：" + e.getMessage());
        }
    }
}
