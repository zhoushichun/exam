package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 自测考试题
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
@Data
@TableName("exam_own_test_type")
public class OwnTestTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 自测ID
	 */
	private Long ownTestId;
	/**
	 * 自测名称
	 */
	private String ownTestName;
	/**
	 * 试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解
	 */
	private Integer type;
	/**
	 * 难度类型id，来自码表
	 */
	private Integer difficultyType;
	/**
	 * 自测题数量
	 */
	private Integer ownNum;
	/**
	 * 题眼类型id，来自码表
	 */
	private Long eyeType;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
