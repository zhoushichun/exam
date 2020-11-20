package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 试卷试题段落关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
@TableName("exam_paper_paragraph_library")
public class PaperParagraphLibraryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 试卷id
	 */
	private Long paperId;
	/**
	 * 段落id
	 */
	private Long paragraphId;
	/**
	 * 试题id
	 */
	private Long libraryId;
	/**
	 * 类型 1.固定试题 2.随机试题
	 */
	private Integer libraryType;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 试题分数
	 */
	private BigDecimal score;

}
