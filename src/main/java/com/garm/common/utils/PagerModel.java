package com.garm.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 *
 * @author Auser
 *
 */
@Data
public class PagerModel implements Serializable {

	private static final long serialVersionUID = -3397246794891369611L;

	/** 每页多少条数据 */
	@ApiModelProperty("每页多少条数据")
	private int pageSize = 10;

	/** 第几页 */
	@ApiModelProperty("第几页")
	private int currentPage = 1;

}
