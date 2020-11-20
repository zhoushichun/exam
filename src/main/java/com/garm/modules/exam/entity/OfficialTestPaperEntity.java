package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 官方考试试卷关联
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 13:27:29
 */
@Data
@TableName("exam_official_test_paper")
public class OfficialTestPaperEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 考试id
	 */
	private Long officialTestId;
	/**
	 * 试卷id
	 */
	private Long paperId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
