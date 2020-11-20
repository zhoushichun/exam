

package com.garm.modules.web.controller;



import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.constants.SystemEnableType;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.UserService;


import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.shop.WUserServiceDTO;
import com.garm.modules.web.dto.shop.WUserServiceFeeDTO;
import com.garm.modules.web.dto.shop.WUserServiceQueryDTO;

import com.garm.modules.web.model.UserDTO;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping
@Api(tags = "前台-已购服务")
public class WShopController extends AbstractController {
    @Autowired
    private SysUserServiceService userServiceService;

    @Login
    @PostMapping("api/web/user/service/list")
    @ApiOperation("购买历史")
    public Result<List<WUserServiceDTO>> queryServiceList(@LoginUser @ApiIgnore UserDTO user, @RequestBody WUserServiceQueryDTO dto){
        dto.setUserId(user.getUserId());
        return userServiceService.queryList(dto);
    }

    /**
     * 服务价格
     */
    @PostMapping("api/web/user/service/dict/list")
    @ApiOperation("服务价格")
    public Result<List<WUserServiceFeeDTO>> free(){
        List<DictItemEntity> lsit = getItems().stream().filter(s->DictConstant.FEE_TYPE==s.getDictId()).collect(Collectors.toList());
        List<WUserServiceFeeDTO> result = lsit.stream().map(s->{
            WUserServiceFeeDTO dto = new WUserServiceFeeDTO();
            dto.setDictItemId(s.getDictItemId());
            dto.setItemName(s.getItemName());
            dto.setMark(s.getRemarks());
            dto.setFee(new BigDecimal(s.getValue()));
            return dto;
        }).collect(Collectors.toList());

        return Result.ok(result);
    }


}
