

package com.garm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 *
 * @author
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
