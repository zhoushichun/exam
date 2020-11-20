

package com.garm.modules.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 前台登录统计信息
 *
 * @author
 */
@Data
@ApiModel(value = "前台登录统计信息")
public class UserCountInfoDTO {

    @ApiModelProperty("参加考试次数")
    private Integer joinNum;

    @ApiModelProperty("不及格次数")
    private Integer noPassNum;

    @ApiModelProperty("自测次数")
    private Integer ownNum;

}
