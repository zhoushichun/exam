package com.garm.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 映射信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@Data
@TableName("exam_dict")
public class DictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 码表id
	 */
	@TableId
	private Long dictId;
	/**
	 * 名称
	 */
	private String dictName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
