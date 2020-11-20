package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.modules.exam.entity.OwnTestTypeEntity;

import java.util.Map;

/**
 * 自测考试题
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
public interface OwnTestTypeService extends IService<OwnTestTypeEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);
}

