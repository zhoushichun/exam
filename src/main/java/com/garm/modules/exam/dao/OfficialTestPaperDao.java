package com.garm.modules.exam.dao;

import com.garm.modules.exam.entity.OfficialTestPaperEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 官方考试试卷关联
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 13:27:29
 */
@Mapper
public interface OfficialTestPaperDao extends BaseMapper<OfficialTestPaperEntity> {
	
}
