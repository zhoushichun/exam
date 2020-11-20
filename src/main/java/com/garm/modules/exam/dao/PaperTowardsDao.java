package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.dto.PaperTowardsDTO;
import com.garm.modules.exam.entity.PaperTowardsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 成绩分析
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:53
 */
@Mapper
public interface PaperTowardsDao extends BaseMapper<PaperTowardsEntity> {


    /**
     * 获取试题问卷走线分页数据
     * @param page
     * @param startTime
     * @param endTime
     * @return
     */
    IPage<PaperTowardsDTO> queryList(@Param("page") Page page, @Param("startTime") String startTime, @Param("endTime") String endTime);



    PaperTowardsEntity queryTowardsData(Date date);
}
