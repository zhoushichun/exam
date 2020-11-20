package com.garm.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.OwnTestTypeDao;
import com.garm.modules.exam.entity.OwnTestTypeEntity;
import com.garm.modules.exam.service.OwnTestTypeService;


@Service("ownTestTypeService")
public class OwnTestTypeServiceImpl extends ServiceImpl<OwnTestTypeDao, OwnTestTypeEntity> implements OwnTestTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OwnTestTypeEntity> page = this.page(
                new Query<OwnTestTypeEntity>().getPage(params),
                new QueryWrapper<OwnTestTypeEntity>()
        );

        return new PageUtils(page);
    }

}