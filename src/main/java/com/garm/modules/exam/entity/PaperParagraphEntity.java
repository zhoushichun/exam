package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 试卷段落
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
@TableName("exam_paper_paragraph")
public class PaperParagraphEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 段落id
	 */
	@TableId
	private Long paragraphId;
	/**
	 * 试卷id
	 */
	private Long paperId;
	/**
	 * 段落名称
	 */
	private String paragraphName;
	/**
	 * 段落分数
	 */
	private Integer paragraphScore;
	/**
	 * 段落题数
	 */
	private Integer paragraphNum;
	/**
	 * 排序
	 */
	private Integer seq;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
