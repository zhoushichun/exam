package com.garm.modules.web.dto.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("支付")
public class WPayServiceDTO implements Serializable {

    @NotNull(message = "请选择购买类型")
    @ApiModelProperty("购买类型，1-月付 2-学期付")
    private Integer type;

    @NotNull(message = "请选择服务类型")
    @ApiModelProperty("服务类型")
    private Long serviceType;
    /**
     * H5支付时，前端传入获取openId使用
     */
    @ApiModelProperty("H5支付时，前端传入获取openId使用")
    private String code;
}
