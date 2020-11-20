package com.garm.modules.web.dto.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("阿里支付返回结果")
public class AliPayOrderDTO implements Serializable {

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("form数据")
    private String payUrl;

}
