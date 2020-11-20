package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.garm.modules.web.dto.shop.WUserServiceDTO;
import com.garm.modules.web.dto.shop.WUserServiceQueryDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户服务VIP信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 14:59:42
 */
public interface SysUserServiceService extends IService<SysUserServiceEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询列表
     * @param dto
     * @return
     */
    Result<List<WUserServiceDTO>> queryList(WUserServiceQueryDTO dto);

    /**
     * 定时修改用户的服务状态，到期的用户需要修改用户的服务状态
     */
    void updateServiceType();


}

