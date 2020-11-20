package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.paper.PaperDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.entity.PaperEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
public interface PaperService extends IService<PaperEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils<PaperEntity> queryPage(Map<String, Object> params);

    /**
     * 添加试卷
     *
     * @param dto
     * @return
     */
    Result add(PaperDTO dto);

    /**
     * 编辑试卷
     * @param dto
     * @return
     */
    Result edit(PaperDTO dto);

    /**
     * 详情
     */
    PaperDetailDTO detail(Long paperId);

    /**
     * 复制
     * @param paperId
     * @return
     */
    void copy(Long paperId);

    /**
     * 导出
     * @param paperId
     */
    void export(Integer type,Long paperId, HttpServletResponse response);
}

