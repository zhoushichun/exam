

package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.modules.exam.constants.SystemEnableType;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.entity.SysUserEntity;
import com.garm.modules.sys.service.DictItemService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Controller公共组件
 *
 * @author
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DictItemService dictItemService;

	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected List<DictItemEntity> getItems() {
		return dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getStatus, SystemEnableType.ENABLE));
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
}
