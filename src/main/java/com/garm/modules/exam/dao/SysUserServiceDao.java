package com.garm.modules.exam.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.web.dto.shop.WUserServiceDTO;
import com.garm.modules.web.dto.shop.WUserServiceQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * 用户服务VIP信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 14:59:42
 */
@Mapper
public interface SysUserServiceDao extends BaseMapper<SysUserServiceEntity> {

    //购买历史分页数据
    List<WUserServiceDTO> queryListByConditions(@Param("page") Page page, @Param("dto") WUserServiceQueryDTO dto);

    /**
     * 查询已过期
     * @return
     */
    List<SysUserServiceEntity> queryServiceByTime();


    int updateServiceTypeById(@Param("serviceId") Long serviceId);
}
