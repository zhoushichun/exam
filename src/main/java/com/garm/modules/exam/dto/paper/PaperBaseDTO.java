package com.garm.modules.exam.dto.paper;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
public class PaperBaseDTO implements Serializable {

	/**
	 * 试卷id
	 */
	@ApiModelProperty("试卷id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long paperId;
	/**
	 * 试卷名称
	 */
	@ApiModelProperty("试卷名称")
	@NotBlank(message = "试卷名称不能为空")
	private String paperName ;
	/**
	 * 服务类型id
	 */
	@ApiModelProperty("服务类型id")
	@NotNull(message = "服务类型id不能为空")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long serviceType;

	/**
	 * 总分数
	 */
	@ApiModelProperty("总分数")
	@NotNull(message = "总分数不能为空")
	private BigDecimal totalScore;
	/**
	 * 总题数
	 */
	@ApiModelProperty("总题数")
	private Integer totalNum;


}
