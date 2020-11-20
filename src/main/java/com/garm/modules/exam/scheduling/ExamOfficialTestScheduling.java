package com.garm.modules.exam.scheduling;


import com.garm.common.utils.DateUtils;
import com.garm.common.utils.SRedisUtil;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.PaperTowardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author ldx
 * @Date 2020/3/27 17:03
 * @Description
 * @Version 1.0.0
 */
@Component
@Slf4j
public class ExamOfficialTestScheduling {

    @Autowired
    OfficialTestService iExamOfficialTestService;

    @Autowired
    PaperTowardsService iExamPaperTowardsService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute(){
        try{
            log.info("-------每日凌晨 验证是否存在考试结束----------");
            iExamOfficialTestService.officialTestEndOperation();
            String date = "scheduling::official::testend::"+ DateUtils.getCurrentDayByDayPattern();
            SRedisUtil.set(date, true, 25*60*60L);
        }catch (Exception e){
            e.printStackTrace();
            log.error("-------每日凌晨 验证是否存在考试结束----------异常：" + e.getMessage());
        }


    }

    @Scheduled(cron = "0 29 11 * * ?")
    public void paperStatistical(){
        try{
            log.info("-------每日凌晨 新增试题统计数据----------");
            iExamPaperTowardsService.paperStatistical();
            String date = "scheduling::official::paper::"+ DateUtils.getCurrentDayByDayPattern();
            SRedisUtil.set(date, true, 25*60*60L);
        }catch (Exception e){
            log.error("-------每日凌晨 新增试题统计数据----------异常：" + e.getMessage());
        }
    }


}
