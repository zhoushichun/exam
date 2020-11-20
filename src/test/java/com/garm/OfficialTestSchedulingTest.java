package com.garm;


import com.garm.common.utils.DateUtils;
import com.garm.common.utils.SRedisUtil;
import com.garm.modules.exam.service.OfficialTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficialTestSchedulingTest {

    @Autowired
    OfficialTestService iExamOfficialTestService;

    @Test
    public void execute(){

        iExamOfficialTestService.officialTestEndOperation();

    }
}
