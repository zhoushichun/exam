package com.garm.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.OfficialTestPaperDao;
import com.garm.modules.exam.entity.OfficialTestPaperEntity;
import com.garm.modules.exam.service.OfficialTestPaperService;


@Service("officialTestPaperService")
public class OfficialTestPaperServiceImpl extends ServiceImpl<OfficialTestPaperDao, OfficialTestPaperEntity> implements OfficialTestPaperService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OfficialTestPaperEntity> page = this.page(
                new Query<OfficialTestPaperEntity>().getPage(params),
                new QueryWrapper<OfficialTestPaperEntity>()
        );

        return new PageUtils(page);
    }

}