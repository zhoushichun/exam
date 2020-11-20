package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.modules.exam.entity.OfficialTestPaperEntity;

import java.util.Map;

/**
 * 官方考试试卷关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
public interface OfficialTestPaperService extends IService<OfficialTestPaperEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);
}

