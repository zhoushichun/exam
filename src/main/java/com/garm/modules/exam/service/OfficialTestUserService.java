package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.ranke.RankInfoDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestInfoDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreListDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreUserListDTO;
import com.garm.modules.exam.dto.usertestcount.*;
import com.garm.modules.exam.entity.OfficialTestUserEntity;
import com.garm.modules.web.dto.test.OfficialTestAnalysisRespDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListReqDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListRespDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 官方考试用户关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
public interface OfficialTestUserService extends IService<OfficialTestUserEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 活跃排行榜数据
     * @param date
     * @return
     */
    Result<RankInfoDTO> ranke(String date);

    /**
     * 考生成绩分析---列表
     */
    Result<PageUtils<OfficialTestStudentListDTO>> userScoreAnalyze(Map<String,Object> params);


    /**
     * 考生成绩分析---基本信息
     */
    OfficialTestUserInfoDTO queryOfficialTestStudentInfo(Long userId);

    /**
     * 考生成绩分析---考试记录
     */
    PageUtils<OfficialTestUserHisInfoDTO> queryOfficialTestStudentHisList(Map<String,Object> params);


    /**
     * 考生成绩分析--成绩分析
     *
     * @return
     */
    Result<List<OfficialTestAnalysisInfoDTO>> queryOfficialTestAnalysis(Long id);

    /**
     * 考生成绩分析--查看问卷
     *
     * @return
     */
    Result<PaperAnswerInfoDTO> queryPaper(Long id);

    /**
     * 考试成绩分析-列表
     * @param params
     * @return
     */
    PageUtils<OfficialTestScoreListDTO>  queryOfficialTestScoreList(Map<String,Object> params);

    /**
     * 考试成绩分析-基本信息
     *
     * @param officialTestId
     * @return
     */
    Result<OfficialTestInfoDTO> testInfo(Long officialTestId);

    /**
     * 考试成绩分析-
     *
     * @return
     */
    Result<PageUtils<OfficialTestScoreUserListDTO>> queryOfficialTestScoreUserList(Map<String,Object> params);


    /**
     * 成绩曲线
     *
     * @param dto
     * @return
     */
    Result<List<ScoreDiagramListRespDTO>> webApiReport(WOfficialTestQueryRequestDTO dto);

    /**
     * 成绩列表-分页记录
     * @param dto
     * @return
     */
    Result<PageUtils<ScoreDiagramListRespDTO>> webApiReportPage(WOfficialTestQueryRequestDTO dto);

    /**
     * 前端考生成绩分析--成绩分析
     *
     * @return
     */
    Result<OfficialTestAnalysisRespDTO> queryWebOfficialTestAnalysis(Long officialTestId, Long userId);


    /**
     * 前端-首页成绩曲线图
     *
     * @param dto
     * @return
     */
    Result<PageUtils<ScoreDiagramListRespDTO>> queryScoreDiagramList(ScoreDiagramListReqDTO dto);
}

