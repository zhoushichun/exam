package com.garm.modules.web.dto.index;

import com.garm.common.utils.PagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title OfficialTestPageListReqDTO
 * @description
 * @date 2020/4/30 11:13
 */
@Data
@ApiModel("考试请求参数")
public class OfficialTestPageListReqDTO extends PagerModel implements Serializable {

    /**
     * 考试用户ID
     */
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 是否删除 1.已删除 2.正常
     */
    @ApiModelProperty(hidden = true)
    private Integer isDel;

}
