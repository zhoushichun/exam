

package com.garm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.garm.common.utils.R;
import com.garm.common.utils.Result;
import com.garm.modules.sys.entity.SysUserTokenEntity;

import java.util.Map;

/**
 * 用户Token
 *
 * @author
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Result<Map> createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
