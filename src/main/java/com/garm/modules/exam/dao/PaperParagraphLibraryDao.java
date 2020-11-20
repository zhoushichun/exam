package com.garm.modules.exam.dao;

import com.garm.modules.exam.entity.PaperParagraphLibraryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试卷试题段落关联
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Mapper
public interface PaperParagraphLibraryDao extends BaseMapper<PaperParagraphLibraryEntity> {
	
}
