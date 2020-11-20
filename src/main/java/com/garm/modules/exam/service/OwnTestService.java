package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.entity.OwnTestEntity;
import com.garm.modules.web.dto.error.WErrorTestQueryRequestDTO;
import com.garm.modules.web.dto.owntest.request.WOwnTestQueryDTO;
import com.garm.modules.web.dto.owntest.request.WOwnTestRequestDTO;
import com.garm.modules.web.dto.owntest.request.OwnTestDetailReqDTO;
import com.garm.modules.web.model.UserDTO;

import java.util.Map;

/**
 * 自测考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:40:56
 */
public interface OwnTestService extends IService<OwnTestEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 生成试卷试题
     * @param dto
     * @return
     */
    Result generatePaper(WOwnTestRequestDTO dto, UserDTO user);


    /**
     * 预览
     */
    Result preview(Long ownTestId);

    /**
     * 开始考试
     * @return
     */
    Result start(Long ownTestId);

    /**
     * 保存自测试卷数据
     */
    Result saveData(OwnTestDetailReqDTO dto);

    /**
     * 统计状态数量
     * @param userId
     * @return
     */
    Result count(Long userId);


    /**
     * 清除
     */
    Result clear(Long ownTestId);


    /**
     * 获取数据详情
     */
    Result detail(Long ownTestId);

    /**
     * 获取数据集合
     */
    Result list(WOwnTestQueryDTO dto,Long userId);

    /**
     * 获取自测错题数据集合
     */
    Result listErrorTest(WErrorTestQueryRequestDTO dto,Long userId);


    /**
     * 获取自测错题数据
     */
    Result getErrorTestDetail(Integer type,Long testId,Long userId) ;


}

