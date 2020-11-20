package com.garm.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.modules.sys.entity.DictItemEntity;

import java.util.Map;

/**
 * 码表副表
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
public interface DictItemService extends IService<DictItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

