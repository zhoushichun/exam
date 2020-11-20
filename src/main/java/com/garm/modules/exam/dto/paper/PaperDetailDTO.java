package com.garm.modules.exam.dto.paper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
@ApiModel(value = "试卷详情")
public class PaperDetailDTO  extends PaperBaseDTO {

	@ApiModelProperty("试卷段落数据")
	private List<PaperParagraphDTO> paragraphDatas;

}
