package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.dto.analyze.TestAnalyzeDTO;
import com.garm.modules.exam.dto.errorowntest.SelfTestUserListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorStatisticsDTO;
import com.garm.modules.exam.entity.SysUserErrorEyeTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户考试错题题眼信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
@Mapper
public interface SysUserErrorEyeTypeDao extends BaseMapper<SysUserErrorEyeTypeEntity> {
    //活跃排行榜数据
    List<TestAnalyzeDTO.EyeAnalyze> queryAnalyzeInfo(int type);

    //考试错题集统计-错题集统计列表
    IPage<OfficialTestErrorStatisticsDTO> queryOfficialTestErrorStatistics(@Param("page") Page page , @Param("type")int type, @Param("testId")String testId,@Param("eyeType") String eyeType);

    //用户自测列表
    IPage<SelfTestUserListDTO> querySelfTestUserList(@Param("page")Page page,@Param("username")String username,@Param("nickname")String nickname);
}
