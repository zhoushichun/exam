package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.entity.OfficialTestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.web.dto.index.OfficialTestPageListReqDTO;
import com.garm.modules.web.dto.test.OfficialTestPageListRespDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;
import com.garm.modules.web.dto.test.WOfficialTestResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 13:27:29
 */
@Mapper
public interface OfficialTestDao extends BaseMapper<OfficialTestEntity> {

    /**
     * 查询前端用户考试列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<OfficialTestPageListRespDTO> queryOfficialTestList(@Param("page") Page<OfficialTestPageListRespDTO> page, @Param("dto") OfficialTestPageListReqDTO dto);

    /**
     * 前端，考试列表
     * @param page
     * @param dto
     * @return
     */
    IPage<WOfficialTestResponseDTO> selectOfficialTestToWebLimit(@Param("page") Page page, @Param("dto") WOfficialTestQueryRequestDTO dto);


    /**
     * 考试须知
     * @param userId
     * @param officialTestId
     * @return
     */
    WOfficialTestResponseDTO selectOfficialTestById(@Param("userId") Long userId, @Param("officialTestId") Long officialTestId);


    /**
     *  考试状态统计
     * @param dto
     * @return
     */
    int selectOfficialTestCount(@Param("dto") WOfficialTestQueryRequestDTO dto);


    /**
     * 获取到期试题， 并且存在 未完成的考试
     *
     * @return
     */
    List<OfficialTestEntity> selectExpireTest();
}
