package com.garm.modules.exam.dto.test.official;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@Data
@ApiModel(value = "考试信息")
public class OfficialTestDTO extends  OfficialTestBaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试卷id
	 */
	@ApiModelProperty("试卷id")
	@NotNull(groups = {AddGroup.class},message = "请选择试卷")
	@Size(min=1,message = "请选择试卷")
	private List<Long> paperIds;

	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	@NotNull(groups = {AddGroup.class},message = "请选择考试用户")
	@Size(min=1,message = "请选择考试用户")
	private List<Long> userIds;


}
