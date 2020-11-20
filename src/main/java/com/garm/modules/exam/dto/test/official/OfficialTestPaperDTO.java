package com.garm.modules.exam.dto.test.official;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 官方考试试卷关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@Data
@ApiModel(value = "考试试卷关联")
public class OfficialTestPaperDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty("主键id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;
	/**
	 * 考试id
	 */
	@ApiModelProperty("考试id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long officialTestId;
	/**
	 * 试卷id
	 */
	@ApiModelProperty("试卷id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long paperId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

}
