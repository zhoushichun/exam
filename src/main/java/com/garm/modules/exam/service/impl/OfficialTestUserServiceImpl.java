package com.garm.modules.exam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.PageList;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.dto.ranke.RankInfoDTO;
import com.garm.modules.exam.dto.ranke.RankingDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestInfoDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreListDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreUserListDTO;
import com.garm.modules.exam.dto.usertestcount.*;
import com.garm.modules.exam.entity.OfficialTestEntity;
import com.garm.modules.exam.enums.DoTestType;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.web.dto.test.OfficialTestAnalysisRespDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListReqDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListRespDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.OfficialTestUserDao;
import com.garm.modules.exam.entity.OfficialTestUserEntity;
import com.garm.modules.exam.service.OfficialTestUserService;

import javax.servlet.http.HttpServletRequest;


@Service("officialTestUserService")
@Slf4j
public class OfficialTestUserServiceImpl extends ServiceImpl<OfficialTestUserDao, OfficialTestUserEntity> implements OfficialTestUserService {

    @Autowired
    private OfficialTestService officialTestService;

    @Autowired
    private DictItemService dictItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OfficialTestUserEntity> page = this.page(
                new Query<OfficialTestUserEntity>().getPage(params),
                new QueryWrapper<OfficialTestUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Result<RankInfoDTO> ranke(String date) {
        log.info("开始获取活跃排行榜数据>>>>>>");
        List<RankingDTO> tanking = baseMapper.findTanking(date);

        Stream<RankingDTO> sorted = tanking.stream().sorted(Comparator.comparing(RankingDTO::getScore).reversed()).limit(10);

        RankInfoDTO dto = new RankInfoDTO();
        dto.setSocreRanking(sorted.collect(Collectors.toList()));
        dto.setTestRanking(tanking);
        return Result.ok(dto);
    }

    @Override
    public Result<PageUtils<OfficialTestStudentListDTO>> userScoreAnalyze(Map<String, Object> params) {
        log.info("考生成绩统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String username = (String)params.get("username");
        String nickname = (String)params.get("nickname");
        IPage<OfficialTestStudentListDTO> result = baseMapper.queryOfficialTestStudentList(new Page(page,limit),username,nickname);
        return Result.successPage(new PageUtils<OfficialTestStudentListDTO>(result));
    }

    @Override
    public OfficialTestUserInfoDTO queryOfficialTestStudentInfo(Long userId) {
        return baseMapper.queryOfficialTestStudentInfo(userId);
    }

    @Override
    public PageUtils<OfficialTestUserHisInfoDTO> queryOfficialTestStudentHisList(Map<String, Object> params) {
        log.info("考生成绩统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String testName = (String)params.get("testName");
        String serviceType = (String)params.get("serviceType");
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");
        String userId = (String)params.get("userId");
        return new PageUtils<OfficialTestUserHisInfoDTO>(baseMapper.queryOfficialTestUserHisInfo(new Page(page,limit),testName,serviceType,startTime,endTime,userId));
    }

    @Override
    public Result<List<OfficialTestAnalysisInfoDTO>> queryOfficialTestAnalysis(Long id) {
        List<OfficialTestAnalysisInfoDTO> result = baseMapper.queryScoreAnalysis(id);
        return Result.ok(result);
    }

    @Override
    public Result<PaperAnswerInfoDTO> queryPaper(Long id) {
        PaperAnswerInfoDTO result = new PaperAnswerInfoDTO();
        log.info("查看问卷 入参 id {}: ", id);
        if (id == null) {
            return Result.error("id 记录id参数不能为空");
        }
        OfficialTestUserEntity examUserTestAnswer = baseMapper.selectById(id);
        if (examUserTestAnswer == null) {
            return Result.error("答题数据不存在");
        }
        String testDetail = examUserTestAnswer.getTestDetail();
        if (StringUtils.isBlank(testDetail)) {
            return Result.error("答题数据不存在");
        }

        OfficialTestEntity officialTest = officialTestService.getById(examUserTestAnswer.getOfficialTestId());
        DictItemEntity item = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictItemId,officialTest.getServiceType()));
        result.setServiceTypeName(item.getItemName());
        result.setServiceType(officialTest.getServiceType());
        result.setTestName(officialTest.getOfficialTestName());
        result.setTestDetail(JSON.parseObject(testDetail, PaperDetailDTO.class));
        return Result.ok(result);
    }

    @Override
    public PageUtils<OfficialTestScoreListDTO> queryOfficialTestScoreList(Map<String, Object> params) {
        log.info("考试成绩统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String serviceType = (String)params.get("serviceType");
        String testName = (String)params.get("testName");
        return new PageUtils<OfficialTestScoreListDTO>(baseMapper.queryOfficialTestScoreList(new Page(page,limit),serviceType,testName));
    }

    @Override
    public Result<OfficialTestInfoDTO> testInfo(Long officialTestId) {
        log.info("考试成绩分析-基本信息 入参 officialTestId {}: ", officialTestId);
        if (officialTestId == null) {
            return Result.error("officialTestId 考试id参数不能为空");
        }
        OfficialTestEntity examOfficialTest = officialTestService.getById(officialTestId);
        if (examOfficialTest == null) {
            return Result.error("考试信息不存在");
        }
        OfficialTestInfoDTO data = new OfficialTestInfoDTO();
        BeanUtils.copyProperties(examOfficialTest, data);
        List<DictItemEntity> entitys = dictItemService.list();
        if(entitys.stream().filter(m->m.getDictItemId().equals(data.getServiceType())).findFirst().isPresent()){
            data.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(data.getServiceType())).findFirst().get().getItemName());
        }
        return Result.ok(data);
    }

    @Override
    public Result<PageUtils<OfficialTestScoreUserListDTO>> queryOfficialTestScoreUserList(Map<String, Object> params) {
        log.info("考试参考人员信息列表 入参 OfficialTestScoreUserListReqDTO {}: ", params);

        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String username = (String)params.get("username");
        String isPass = (String)params.get("isPass");
        String nickname = (String)params.get("nickname");
        String officialTestId = (String)params.get("officialTestId");
        if (officialTestId == null) {
            return Result.error("officialTestId 考试Id参数不能为空");
        }

        IPage<OfficialTestScoreUserListDTO> result = baseMapper.queryOfficialTestScoreUserList(new Page(page,limit), officialTestId,isPass,username,nickname);
        return Result.ok(new PageUtils(result));
    }

    @Override
    public Result<List<ScoreDiagramListRespDTO>> webApiReport(WOfficialTestQueryRequestDTO dto) {
        List<ScoreDiagramListRespDTO> reuslt = baseMapper.queryUserAllScoreDiagramList(dto);

        return Result.ok(reuslt);
    }

    @Override
    public Result<PageUtils<ScoreDiagramListRespDTO>> webApiReportPage(WOfficialTestQueryRequestDTO dto) {
        IPage<ScoreDiagramListRespDTO> reuslt = baseMapper.queryUserAllScoreDiagramList(new Page(dto.getCurrentPage(),dto.getPageSize()),dto);
        return Result.ok(new PageUtils(reuslt));
    }

    @Override
    public Result<OfficialTestAnalysisRespDTO> queryWebOfficialTestAnalysis(Long officialTestId,Long userId) {
        OfficialTestUserEntity entity = baseMapper.selectOne(Wrappers.<OfficialTestUserEntity>lambdaQuery().eq(OfficialTestUserEntity::getOfficialTestId,officialTestId).eq(OfficialTestUserEntity::getUserId,userId));

        List<OfficialTestAnalysisInfoDTO> result = baseMapper.queryScoreAnalysis(entity.getId());
        OfficialTestAnalysisRespDTO respDTO = new OfficialTestAnalysisRespDTO();
        BeanUtils.copyProperties(entity,respDTO);
        respDTO.setDatas(result);
        return Result.ok(respDTO);
    }

    @Override
    public Result<PageUtils<ScoreDiagramListRespDTO>> queryScoreDiagramList(ScoreDiagramListReqDTO dto) {
        log.info("前端-首页成绩曲线图 入参 ScoreDiagramListReqDTO {}: ", dto);
        dto.setDoTest(DoTestType.EXAM_OVER.getCode());

        Page<ScoreDiagramListRespDTO> page = new Page();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        IPage<ScoreDiagramListRespDTO> result = baseMapper.queryScoreDiagramList(page, dto);
        return Result.ok(new PageUtils(result));
    }

}
