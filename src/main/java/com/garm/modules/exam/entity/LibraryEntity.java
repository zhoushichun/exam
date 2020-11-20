package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Data
@TableName("exam_library")
public class LibraryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 题库id
	 */
	@TableId(value = "library_id",type = IdType.INPUT)
	private Long libraryId;
	/**
	 * 父题目ID,没有父题时为0
	 */
	private Long parentLibraryId;
	/**
	 * 试题题目
	 */
	private String libraryName;
	/**
	 * 试题题目，带标签
	 */
	private String libraryNameContent;
	/**
	 * 试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解
	 */
	private Integer type;
	/**
	 * 服务类型id
	 */
	private Long serviceType;
	/**
	 * 难度类型,1-简单,2-中等,3-困难
	 */
	private Integer difficultyType;
	/**
	 * 题眼类型id
	 */
	private Long eyeType;
	/**
	 * 使用范围,1-考试,2-自测,3，都有
	 */
	private Integer rangeType;
	/**
	 * 音频/视频路径
	 */
	private String url;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 正确答案,当类型为1单选题时,1--A 2--B 3--C 4--D;当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，无答案,选项即答案
	 */
	private String answer;
	/**
	 * 答题解析
	 */
	private String answerAnalysis;
	/**
	 * 删除时间
	 */
	private Date delTime;
	/**
	 * 是否删除 1.已删除 2.正常
	 */
	private Integer isDel;
	/**
	 * 是否已被考试  1-未被使用过  2-已被使用
	 */
	private Integer isTest;

}
