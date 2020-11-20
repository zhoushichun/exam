package com.garm.modules.sys.service.impl;

import com.garm.modules.sys.service.DictItemService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.sys.dao.DictItemDao;
import com.garm.modules.sys.entity.DictItemEntity;


@Service("dictItemService")
public class DictItemServiceImpl extends ServiceImpl<DictItemDao, DictItemEntity> implements DictItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DictItemEntity> page = this.page(
                new Query<DictItemEntity>().getPage(params),
                new QueryWrapper<DictItemEntity>()
        );

        return new PageUtils(page);
    }

}
