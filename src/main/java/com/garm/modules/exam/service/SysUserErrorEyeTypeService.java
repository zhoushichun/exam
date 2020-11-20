package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.analyze.TestAnalyzeDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorStatisticsDTO;
import com.garm.modules.exam.entity.SysUserErrorEyeTypeEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户考试错题题眼信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
public interface SysUserErrorEyeTypeService extends IService<SysUserErrorEyeTypeEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils<OfficialTestErrorListDTO> queryPage(Map<String, Object> params);

    /**
     * 获取题眼统计数据
     */
    List<TestAnalyzeDTO.EyeAnalyze> queryAnalyzeInfo(int type);


    /**
     * 考试错题集统计-错题集统计列表
     *
     * @return
     */
    Result<PageUtils<OfficialTestErrorStatisticsDTO>> queryOfficialTestErrorStatistics(Map<String, Object> params);


    /**
     * 自测错题集统计-前端自测用户列表
     *
     * @return
     */
    Result querySelfTestUserList(Map<String, Object> params);


    /**
     * 自测错题统计-考生信息
     *
     * @return
     */
    Result detail(Map<String, Object> params);


    /**
     * 考试错题集统计-错题集统计列表
     *
     * @return
     */
    Result<PageUtils<OfficialTestErrorStatisticsDTO>> queryOwnTestErrorStatistics(Map<String, Object> params);
}

