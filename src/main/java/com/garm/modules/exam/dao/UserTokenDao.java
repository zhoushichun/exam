package com.garm.modules.exam.dao;

import com.garm.modules.web.entity.UserTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-13 21:21:58
 */
@Mapper
public interface UserTokenDao extends BaseMapper<UserTokenEntity> {

}
