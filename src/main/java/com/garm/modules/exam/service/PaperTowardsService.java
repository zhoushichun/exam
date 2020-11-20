package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.modules.exam.dto.PaperTowardsDTO;
import com.garm.modules.exam.entity.PaperTowardsEntity;

import java.util.Map;

/**
 * 成绩分析
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:53
 */
public interface PaperTowardsService extends IService<PaperTowardsEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取问题走向分页数据
     */
    PageUtils<PaperTowardsDTO> queryList(Map<String, Object> params);



    void paperStatistical();
}

