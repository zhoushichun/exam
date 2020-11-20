package com.garm.modules.web.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 已购服务查询
 */
@Data
@ApiModel("已购服务")
public class WUserServiceDTO {

    /**
     * 服务名称
     */
    @ApiModelProperty("服务名称")
    private String serviceName;
    /**
     * 服务类型
     */
    @JsonIgnore
    private Integer serviceType;

    /**
     * 付款类型名称
     */
    @ApiModelProperty("支付类型")
    private String typeName;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal fee;

    /**
     * 购买时间
     */
    @ApiModelProperty("购买时间")
    private Date buyTime;

    /**
     * 到期时间
     */
    @ApiModelProperty("到期时间")
    private Date maturityTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态 1-无效 2-有效")
    private String status;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 考试次数
     */
    @ApiModelProperty("考试次数")
    private int officeCount;
    /**
     * 自测次数
     */
    @ApiModelProperty("自测次数")
    private int ownCount;
}
