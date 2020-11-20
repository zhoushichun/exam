

package com.garm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 * @author
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
