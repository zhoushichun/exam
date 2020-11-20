package com.garm.modules.exam.dao;

import com.garm.modules.exam.entity.LibraryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题库管理
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Mapper
public interface LibraryDao extends BaseMapper<LibraryEntity> {
	
}
