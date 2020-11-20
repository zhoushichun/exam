package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 试题选项
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Data
@TableName("exam_library_option")
public class LibraryOptionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 选项ID
	 */
	@TableId
	private Long optionId;
	/**
	 * 题库id
	 */
	private Long libraryId;
	/**
	 * 选项内容
	 */
	private String optionContent;
	/**
	 * 选项类型，如，单选和多选1--A 2--B 3--C ,判断为1正确,2错误;填空为1,2,3,4
	 */
	private String optionType;


}
