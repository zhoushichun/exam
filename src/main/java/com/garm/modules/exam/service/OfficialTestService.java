package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestDTO;
import com.garm.modules.exam.entity.OfficialTestEntity;
import com.garm.modules.web.dto.index.OfficialTestPageListReqDTO;
import com.garm.modules.web.dto.test.OfficialTestPageListRespDTO;
import com.garm.modules.web.dto.test.OfficiatesTestDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;
import com.garm.modules.web.dto.test.WOfficialTestResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
public interface OfficialTestService extends IService<OfficialTestEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 发布考试
     * @param dto
     * @return
     */
    Result publish(OfficialTestDTO dto);
    /**
     * 编辑
     * @param form
     * @return
     */
    Result edit(OfficialTestDTO form);

    /**
     * 查询前端用户考试列表
     *
     * @param dto
     * @return
     */
    Result<PageUtils<OfficialTestPageListRespDTO>> queryOfficialTestList(OfficialTestPageListReqDTO dto);


    /**
     * 获取
     * @param dto
     * @return
     */
    Result webApiList(WOfficialTestQueryRequestDTO dto);


    /**
     * 获取考试须知数据
     * @param officialTestId
     * @return
     */
    WOfficialTestResponseDTO need(Long officialTestId, Long userId);


    /**
     * 考试状态统计
     *
     * @return
     */
    Result countStatus(Long userId);

    /**
     * 开始/继续 考试
     * @param officialTestId
     * @return
     */
    Result webApiExamStart(Long officialTestId, Long userId);


    /**
     * N 秒自动保存 答案
     *
     * @param officialTestId
     * @param dto
     * @return
     */
    Result webApiTimingSave(Long officialTestId,Long stateTime,Long userId, OfficiatesTestDTO dto);

    //查询考试详情信息
    Result webApiofficialInfo(Long officialTestId,Long userId);

    //提交试卷
    Result webApiTheirPapers(Long officialTestId, OfficiatesTestDTO info,Long userId,Long stateTime);


    ///验证是否存在考试结束
    void officialTestEndOperation();



}

