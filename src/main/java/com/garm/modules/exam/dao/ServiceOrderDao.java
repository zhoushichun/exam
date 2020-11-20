package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.entity.ServiceOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 服务订单
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:39
 */
@Mapper
public interface ServiceOrderDao extends BaseMapper<ServiceOrderEntity> {

    IPage<ServiceOrderDTO> selectList(@Param("Page") Page page, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
