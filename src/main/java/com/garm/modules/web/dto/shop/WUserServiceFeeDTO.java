package com.garm.modules.web.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel("服务价格")
public class WUserServiceFeeDTO {

    /**
     * 服务名称
     */
    @ApiModelProperty("主键id，类型id，会传回后台的")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dictItemId;
    /**
     * 服务类型
     */
    @ApiModelProperty("名称 ，会传回后台的")
    private String itemName;

    /**
     * 付款类型名称
     */
    @ApiModelProperty("支付类型")
    private String mark;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal fee;


}
