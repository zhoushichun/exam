package com.garm.modules.web.dto.test;

import com.garm.modules.exam.dto.paper.PaperBaseDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphDTO;
import com.garm.modules.web.dto.owntest.request.Library;
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
@ApiModel(value = "试卷保存参数详情")
public class OfficiatesTestDTO {

	@ApiModelProperty(value= "试题回答数据")
	private List<Library> datas;

}
