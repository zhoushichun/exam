

package com.garm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统用户Token
 *
 * @author
 */
@Mapper
@Repository
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);

}
