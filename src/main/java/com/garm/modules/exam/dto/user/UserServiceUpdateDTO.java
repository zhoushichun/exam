package com.garm.modules.exam.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 调整用户服务时间/类型
 * @author RKG
 */
@Data
@ApiModel("调整用户服务时间/类型")
public class UserServiceUpdateDTO implements Serializable {

    /**
     * 批量调整服务时传格式 1,2,3,单个调整服务时传格式 1
     */
//    @ApiModelProperty("批量调整服务时传格式 1,2,3,单个调整服务时传格式 1")
//    private String serviceIds;

    /**
     * 1-批量调整   2-单个调整
     */
    @NotNull(message = "调整类型必传")
    @Min(value = 1,message = "调整类型参数错误")
    @Max(value = 2,message = "调整类型参数错误")
    @ApiModelProperty("1-批量调整   2-单个调整")
    private Integer type;

    /**
     * 服务到期时间，立即终止-传入当天时间 yyyy-MM-dd
     */
    @ApiModelProperty("服务到期时间，立即终止-传入当天时间 yyyy-MM-dd")
    private String timeStr;

    /**
     * 新的服务类型id
     */
    @ApiModelProperty("新的服务类型id")
    private Long serviceType;
    /**
     * 用户id
     */
    @ApiModelProperty("用户ID,多个用户ID用,连接")
    private String userIds;



}
