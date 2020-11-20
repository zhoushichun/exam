package com.garm.modules.web.dto.shop;


import com.garm.common.utils.PagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@Data
@ApiModel("购买历史参数")
public class WUserServiceQueryDTO extends PagerModel {

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型id")
    private Integer serviceId;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;
    /**
     * 是否有效 1-无效 2-有效
     */
    @ApiModelProperty(hidden = true)
    private Integer status;
}
