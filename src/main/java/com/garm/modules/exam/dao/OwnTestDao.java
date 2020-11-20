package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.entity.OwnTestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.web.dto.error.ErrorTestRespDTO;
import com.garm.modules.web.dto.error.WErrorTestQueryRequestDTO;
import com.garm.modules.web.dto.shop.UserTestQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 自测考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:40:56
 */
@Mapper
public interface OwnTestDao extends BaseMapper<OwnTestEntity> {

    /**
     * 查询用户在时间范围自测考试的数量
     * @param userTestAnswerQueryDTO
     * @return
     */
    int queryOwnCount(UserTestQueryDTO userTestAnswerQueryDTO);


    /**
     * 分页列表数据查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ErrorTestRespDTO> queryOwnPageList(@Param("page") Page<ErrorTestRespDTO> page, @Param("dto") WErrorTestQueryRequestDTO dto, @Param("userId")Long userId);

    /**
     * 分页列表数据查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ErrorTestRespDTO> queryOfficialPageList(@Param("page") Page<ErrorTestRespDTO> page, @Param("dto") WErrorTestQueryRequestDTO dto,@Param("userId")Long userId);

}
