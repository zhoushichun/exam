package com.garm.modules.exam.dto.test.official;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import com.garm.modules.exam.dto.paper.PaperDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@Data
@ApiModel(value = "考试信息")
public class OfficialTestInfoDTO extends OfficialTestBaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("服务类型名称")
	private String serviceTypeName;

	@ApiModelProperty(value = "试卷信息")
	List<PaperDTO> paperDatas;

	@ApiModelProperty(value = "用户信息")
	List<OfficialTestUserInfoDTO> userDatas;



}
