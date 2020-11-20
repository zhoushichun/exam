

package com.garm.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 *
 * @author
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
