package com.garm.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 码表副表
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@Data
@TableName("exam_dict_item")
public class DictItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long dictItemId;
	/**
	 * 码表主表id
	 */
	private Long dictId;
	/**
	 * 名称
	 */
	private String itemName;
	/**
	 * 状态 1:禁用，2:正常
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime ;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 键值
	 */
	private String value;

}
