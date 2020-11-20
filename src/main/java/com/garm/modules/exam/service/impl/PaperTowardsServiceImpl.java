package com.garm.modules.exam.service.impl;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.DateUtils;
import com.garm.modules.exam.dto.PaperTowardsDTO;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.PaperTowardsDao;
import com.garm.modules.exam.entity.PaperTowardsEntity;
import com.garm.modules.exam.service.PaperTowardsService;


@Service("paperTowardsService")
public class PaperTowardsServiceImpl extends ServiceImpl<PaperTowardsDao, PaperTowardsEntity> implements PaperTowardsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PaperTowardsEntity> page = this.page(
                new Query<PaperTowardsEntity>().getPage(params),
                new QueryWrapper<PaperTowardsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils<PaperTowardsDTO> queryList(Map<String, Object> params) {
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");
        Long page  = Long.valueOf((String)params.get("page"));
        Long limit  = Long.valueOf((String)params.get("limit"));
        Page p = new Page();
        p.setCurrent(page);
        p.setSize(limit);
        return new PageUtils(baseMapper.queryList(p,startTime,endTime));
    }

    @Override
    public void paperStatistical() {
        Date date = DateUtils.addDateDays(new Date(),-1);
        PaperTowardsEntity examPaperTowards = baseMapper.queryTowardsData(date);
        examPaperTowards.setCreateTime(date);
        examPaperTowards.setTime(new Date());
        if(!save(examPaperTowards)){throw new ResultException("新增试卷走向失败"); }
    }

}
