package com.garm.modules.exam.dto.paper;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 试卷段落
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
public class PaperParagraphDTO {

	/**
	 * 段落id
	 */
	@ApiModelProperty("段落id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long paragraphId;
	/**
	 * 试卷id
	 */
	@ApiModelProperty("试卷id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long paperId;
	/**
	 * 段落名称
	 */
	@NotBlank(message = "段落名称不能为空")
	@ApiModelProperty("段落名称")
	private String paragraphName;
	/**
	 * 段落分数
	 */
	@NotNull(message = "段落分数不能为空")
	@ApiModelProperty("段落分数")
	private Integer paragraphScore;
	/**
	 * 段落题数
	 */
	@ApiModelProperty("段落题数")
	@NotNull(message = "段落题数不能为空")
	private Integer paragraphNum;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@NotNull(message = "排序不能为空")
	private Integer seq;

	/**
	 * 试题数据集合
	 */
	@ApiModelProperty("试题数据结合")
	private List<PaperParagraphLibraryDTO> paragraphLibraryDatas;

}
