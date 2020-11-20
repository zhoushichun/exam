package com.garm.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author liwt
 * @title FreeSettingForm
 * @description
 * @date 2020/5/12 11:08
 */
@Data
@ApiModel(value = "费用设置表单")
public class FreeSettingForm implements Serializable {

    /**
     * 月付
     */
    @Pattern(regexp = "^[0-9]+\\.{0,1}[0-9]{0,2}$",message = "只能是整数或者小数")
    @ApiModelProperty("月付金额")
    private Double monthPay;

    /**
     * 学期付
     */
    @Pattern(regexp = "^[0-9]+\\.{0,1}[0-9]{0,2}$",message = "只能是整数或者小数")
    @ApiModelProperty("学期付金额")
    private Double semesterPay;


}
