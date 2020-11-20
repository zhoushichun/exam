package com.garm.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author rkg
 */
@Api("用户已购服务")
@Data
public class UserServiceDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("服务ID")
    private Long serviceId;

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型ID")
    private Long serviceType;

    /**
     * 服务类型名称
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

}
