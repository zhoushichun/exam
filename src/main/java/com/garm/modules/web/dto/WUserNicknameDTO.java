package com.garm.modules.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("修改用户昵称")
public class WUserNicknameDTO implements Serializable {
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
}
