package com.garm.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.modules.exam.dto.ranke.RankingDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreListDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreUserListDTO;
import com.garm.modules.exam.dto.usertestcount.OfficialTestAnalysisInfoDTO;
import com.garm.modules.exam.dto.usertestcount.OfficialTestStudentListDTO;
import com.garm.modules.exam.dto.usertestcount.OfficialTestUserHisInfoDTO;
import com.garm.modules.exam.entity.OfficialTestUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garm.modules.web.dto.shop.UserTestQueryDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListReqDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListRespDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.garm.modules.exam.dto.usertestcount.OfficialTestUserInfoDTO;
import java.util.List;

/**
 * 官方考试用户关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 13:27:29
 */
@Mapper
public interface OfficialTestUserDao extends BaseMapper<OfficialTestUserEntity> {
    /**
     * 活跃排行榜数据
     * @param date
     * @return
     */
    List<RankingDTO> findTanking(String date);

    /**
     * 考生成绩统计列表
     * @param page
     * @param username
     * @param nickname
     * @return
     */
    IPage<OfficialTestStudentListDTO> queryOfficialTestStudentList(@Param("page") Page page,@Param("username")String username,@Param("nickname")String nickname);

    /**
     * 考生成绩统计--基本信息
     * @param userId
     * @return
     */
    OfficialTestUserInfoDTO queryOfficialTestStudentInfo(Long userId);

    /**
     * 考试--考试记录
     * @return
     */
    IPage<OfficialTestUserHisInfoDTO> queryOfficialTestUserHisInfo(@Param("page") Page page,
                                                                  @Param("testName") String testName,
                                                                  @Param("serviceType") String serviceType,
                                                                  @Param("startTime") String startTime,
                                                                  @Param("endTime") String endTime,
                                                                  @Param("userId") String userId);

    /**
     * 考试成绩分析--成绩分析
     *
     * @return
     */
    List<OfficialTestAnalysisInfoDTO> queryScoreAnalysis(Long id);

    /**
     * 考试称及统计-列表
     * @param page
     * @param serviceType
     * @param officialTestName
     * @return
     */
    IPage<OfficialTestScoreListDTO> queryOfficialTestScoreList(@Param("page") Page page,
                                                              @Param("serviceType") String serviceType,
                                                              @Param("officialTestName") String officialTestName);

    /**
     * 考试成绩统计-参考人员信息
     */
    IPage<OfficialTestScoreUserListDTO> queryOfficialTestScoreUserList(@Param("page") Page page,
                                                                       @Param("officialTestId") String officialTestId,
                                                                       @Param("isPass") String isPass,
                                                                       @Param("username") String username,
                                                                       @Param("nickname") String nickname);

    /**
     * 查询用户在时间范围官方考试的数量
     * @param userTestAnswerQueryDTO
     * @return
     */
    int queryOfficeCount(UserTestQueryDTO userTestAnswerQueryDTO);


    /**
     * 成绩曲线
     *
     * @param dto
     * @return
     */
    List<ScoreDiagramListRespDTO> queryUserAllScoreDiagramList(@Param("dto") WOfficialTestQueryRequestDTO dto);


    /**
     * 成绩曲线--分页列表
     *
     * @param dto
     * @return
     */
    IPage<ScoreDiagramListRespDTO> queryUserAllScoreDiagramList(@Param("page") Page page, @Param("dto") WOfficialTestQueryRequestDTO dto);


    /**
     * 前端-首页成绩曲线图
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ScoreDiagramListRespDTO> queryScoreDiagramList(@Param("page") Page<ScoreDiagramListRespDTO> page,
                                                        @Param("dto") ScoreDiagramListReqDTO dto);
}
