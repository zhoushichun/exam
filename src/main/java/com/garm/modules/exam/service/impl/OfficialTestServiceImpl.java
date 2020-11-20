package com.garm.modules.exam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.*;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.constants.SplitConstant;
import com.garm.modules.exam.dto.library.LibraryChildDTO;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.library.LibraryOptionDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphLibraryDTO;
import com.garm.modules.exam.dto.test.official.ExamOfficialTestPaperDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestDTO;
import com.garm.modules.exam.enums.*;
import com.garm.modules.exam.dao.*;
import com.garm.modules.exam.entity.*;
import com.garm.modules.exam.service.*;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.web.dto.index.OfficialTestPageListReqDTO;
import com.garm.modules.web.dto.test.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("officialTestService")
@Slf4j
public class OfficialTestServiceImpl extends ServiceImpl<OfficialTestDao, OfficialTestEntity> implements OfficialTestService {


    private final static String OFFICIAL_TEST_LOCK_KEY = "official:editor:";

    @Autowired
    private PaperService paperService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private OfficialTestPaperService officialTestPaperService;

    @Autowired
    private OfficialTestUserService officialTestUserService;

    @Autowired
    private PaperParagraphDao paragraphDao;

    @Autowired
    private PaperParagraphLibraryDao paragraphLibraryDao;

    @Autowired
    private SysUserErrorEyeTypeService sysUserErrorEyeTypeService;

    @Autowired
    private DictItemService dictItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String officialTestName = (String)params.get("officialTestName");
        String serviceType = (String)params.get("serviceType");
        IPage<OfficialTestEntity> page = this.page(
                new Query<OfficialTestEntity>().getPage(params),
                Wrappers.<OfficialTestEntity>lambdaQuery()
                        .eq(OfficialTestEntity::getIsDel, DeleteConstant.NOT_DELETE)
                        .like(!org.apache.commons.lang.StringUtils.isBlank(officialTestName),OfficialTestEntity::getOfficialTestName,officialTestName)
                        .eq(!org.apache.commons.lang.StringUtils.isBlank(serviceType),OfficialTestEntity::getServiceType,serviceType)
                        .orderByDesc(OfficialTestEntity::getCreateTime)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OFFICIAL_TEST_LOCK_KEY, key = "#{form.serviceType}#{form.officialTestName}")
    public Result publish(OfficialTestDTO form) {
        log.info("发布考试入参>>>{}", form);
        if (form.getTestStartTime().getTime()<new Date().getTime()) {
            return Result.error("考试开始时间不能小于当前时间");
        } else if (form.getTestStartTime().getTime()>(form.getTestEndTime().getTime())) {
            return Result.error("考试开始时间不能大于结束时间");
        }
        if(form.getPassScore().doubleValue()>form.getTotalScore().doubleValue()){
            return Result.error("及格分数不能大于总分数");
        }

        // 验证是否存在 相同的服务和考试名称
        final OfficialTestEntity checkOfficial = baseMapper.selectOne(Wrappers.<OfficialTestEntity>lambdaUpdate()
                .eq(OfficialTestEntity::getServiceType, form.getServiceType())
                .eq(OfficialTestEntity::getOfficialTestName, form.getOfficialTestName()));
        if (!StringUtils.isEmpty(checkOfficial)) {
            return Result.error("当前服务类型已存在相同的考试");
        }

        // 考试数据 入库
        OfficialTestEntity officialTest = new OfficialTestEntity();
        BeanUtils.copyProperties(form, officialTest);
        officialTest.setCreateTime(new Date());
        officialTest.setIsDel(DeleteConstant.NOT_DELETE);
        officialTest.setOfficialTestId(IdUtil.genId());


        // 考试试卷关联数据 入库
        Set<Long> paperIds = new HashSet<>(form.getPaperIds());

        Map<String, Object> ret = insertOfficialTestPaper(officialTest, paperIds);

        List<OfficialTestPaperEntity> testPapers = (List<OfficialTestPaperEntity>) ret.get("testPapers");
        List<ExamOfficialTestPaperDTO> paperDTOs = (List<ExamOfficialTestPaperDTO>) ret.get("paperDTOs");

        BigDecimal totalScore = (BigDecimal) ret.get("totalScore");
        officialTest.setTotalScore(totalScore);

        if (baseMapper.insert(officialTest) != 1) {
            throw new ResultException("发布考试失败");
        }

        // 考试Paper数据入库
        if (!officialTestPaperService.saveBatch(testPapers)) {
            throw new ResultException("发布考试失败");
        }

        // 设置试卷为已使用
        if (!paperService.update(Wrappers.<PaperEntity>lambdaUpdate().set(PaperEntity::getIsTest, PaperTestType.YES.getCode()).in(PaperEntity::getPaperId, paperIds))) {
            throw new ResultException("试卷已被使用,发布考试失败");
        }

        // 考试用户关联数据 入库
        Set<Long> userIds = new HashSet<>(form.getUserIds());
        insertOfficialTestUser(officialTest, paperDTOs, userIds);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OFFICIAL_TEST_LOCK_KEY, key = "#{dto.serviceType}#{dto.officialTestName}")
    public Result edit(OfficialTestDTO dto) {
        log.info("编辑试卷入参>>>{}", dto);
        if (dto.getTestStartTime().getTime()<new Date().getTime()) {
            return Result.error("考试开始时间不能小于当前时间");
        } else if (dto.getTestStartTime().getTime()>(dto.getTestEndTime().getTime())) {
            return Result.error("考试开始时间不能大于结束时间");
        }
        if(dto.getPassScore().doubleValue()>dto.getTotalScore().doubleValue()){
            return Result.error("及格分数不能大于总分数");
        }

        // 验证是否存在 相同的服务和考试名称
        final OfficialTestEntity checkOfficial = baseMapper.selectOne(Wrappers.<OfficialTestEntity>lambdaUpdate()
                .eq(OfficialTestEntity::getServiceType, dto.getServiceType())
                .eq(OfficialTestEntity::getOfficialTestName, dto.getOfficialTestName())
                .ne(OfficialTestEntity::getOfficialTestId,dto.getOfficialTestId())
        );
        if (!StringUtils.isEmpty(checkOfficial) && !checkOfficial.getOfficialTestId().equals(dto.getOfficialTestId())) {
            return Result.error("当前服务类型已存在相同的考试");
        }

        OfficialTestEntity officialTest = new OfficialTestEntity();
        BeanUtils.copyProperties(dto,officialTest);
        officialTest.setUpdateTime(new Date());

        if (baseMapper.updateById(officialTest) != 1) {
            throw new ResultException("编辑考试失败");
        }

        //试卷和人员有新增时，需要修改试卷的分配（未曾开始考试的用户要统一重新分配）
        //前端直传新加的试卷或者用户
        Set<Long> paperIds = new HashSet<>(dto.getPaperIds());
        Map<String, Object> ret = insertOfficialTestPaper(officialTest, paperIds);

        List<OfficialTestPaperEntity> testPapers = (List<OfficialTestPaperEntity>) ret.get("testPapers");
        List<ExamOfficialTestPaperDTO> paperDTOs = (List<ExamOfficialTestPaperDTO>) ret.get("paperDTOs");

        Long id =testPapers.stream().map(OfficialTestPaperEntity::getOfficialTestId).findFirst().get();

        officialTestPaperService.remove(Wrappers.<OfficialTestPaperEntity>lambdaQuery().eq(OfficialTestPaperEntity::getOfficialTestId,id));
        officialTestUserService.remove(Wrappers.<OfficialTestUserEntity>lambdaQuery().eq(OfficialTestUserEntity::getOfficialTestId,id));

        // 考试Paper数据入库
        if (!officialTestPaperService.saveBatch(testPapers)) {
            throw new ResultException("发布考试失败");
        }

        // 设置试卷为已使用
        if (!paperService.update(Wrappers.<PaperEntity>lambdaUpdate().set(PaperEntity::getIsTest, PaperTestType.YES.getCode()).in(PaperEntity::getPaperId, paperIds))) {
            throw new ResultException("试卷已被使用,发布考试失败");
        }

        // 考试用户关联数据 入库
        Set<Long> userIds = new HashSet<>(dto.getUserIds());
        insertOfficialTestUser(officialTest, paperDTOs, userIds);

        return Result.ok();
    }

    @Override
    public Result<PageUtils<OfficialTestPageListRespDTO>> queryOfficialTestList(OfficialTestPageListReqDTO dto) {
        log.info("查询前端用户考试列表 入参 OfficialTestPageListReqDTO {}: ", dto);
        Page<OfficialTestPageListRespDTO> page = new Page();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        IPage<OfficialTestPageListRespDTO> result = baseMapper.queryOfficialTestList(page, dto);
        return Result.successPage(new PageUtils(result));
    }

    @Override
    public Result webApiList(WOfficialTestQueryRequestDTO dto) {
        IPage<WOfficialTestResponseDTO> ret = baseMapper.selectOfficialTestToWebLimit(new Page(dto.getCurrentPage(), dto.getPageSize()), dto);
        return Result.successPage(new PageUtils(ret));
    }

    @Override
    public WOfficialTestResponseDTO need(Long officialTestId, Long userId) {
        return baseMapper.selectOfficialTestById(userId,officialTestId);
    }

    @Override
    public Result countStatus(Long userId) {
        WOfficialTestQueryRequestDTO dto = new WOfficialTestQueryRequestDTO();
        dto.setUserId(userId);

        //未开始
        dto.setDoTest(DoTestType.EXAM_NO.getCode());
        int examNotStart = baseMapper.selectOfficialTestCount(dto);
        //考试中
        dto.setDoTest(DoTestType.EXAM_ING.getCode());
        int eaxmIngCount = baseMapper.selectOfficialTestCount(dto);
        //已结束
        dto.setDoTest(DoTestType.EXAM_OVER.getCode());
        int examFinished = baseMapper.selectOfficialTestCount(dto);
        OfficialTestStatusCount officialTestStatusCount = new OfficialTestStatusCount(examNotStart, eaxmIngCount, examFinished);
        return Result.ok(officialTestStatusCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OFFICIAL_TEST_LOCK_KEY, key = "#{officialTestId}#{userId}")
    public Result webApiExamStart(Long officialTestId, Long userId) {
        OfficialTestRespDTO resultData = new OfficialTestRespDTO();

        OfficialTestUserEntity officialUser = officialTestUserService.getOne(Wrappers.<OfficialTestUserEntity>lambdaUpdate()
                .eq(OfficialTestUserEntity::getOfficialTestId, officialTestId)
                .eq(OfficialTestUserEntity::getUserId, userId));
        if (StringUtils.isEmpty(officialUser)) {
            return Result.error("考试信息不存在");
        }

        OfficialTestEntity officialTest = baseMapper.selectById(officialTestId);
        if (officialTest.getTestStartTime().getTime()>new Date().getTime()) {
            return Result.error("考试未开始");
        }
        if (officialTest.getTestEndTime().getTime()<new Date().getTime()) {
            return Result.error("考试时间已结束");
        }

        if (officialUser.getDoTest() == DoTestType.EXAM_OVER.getCode()) {
            return Result.error("考试已结束");
        }

        if (officialUser.getDoTest() == DoTestType.EXAM_NO.getCode()) {
            officialUser.setStateTime(0L);
            officialUser.setStartTime(new Date());
            Date gmtExpectEndTime = DateUtils.addDateMinutes( officialUser.getStartTime(), Math.toIntExact(officialTest.getTime()));
            officialUser.setExpectEndTime(gmtExpectEndTime);
            officialUser.setDoTest(DoTestType.EXAM_ING.getCode());

            if (!officialTestUserService.updateById(officialUser)) {
                throw new ResultException("获取考试信息失败");
            }
        }
        PaperDetailDTO result = JSON.parseObject(officialUser.getTestDetail(), PaperDetailDTO.class);
        result.getParagraphDatas().stream().forEach(s->{
            s.getParagraphLibraryDatas().stream().forEach(m->{
                m.setAnswer(null);
                m.setAnswerAnalysis(null);
                if(m.getType() == LibraryType.READING_COMPREHENSION.getCode()){
                    m.getChildDatas().stream().forEach(a1->{
                        a1.setAnswer(null);
                        a1.setAnswerAnalysis(null);
                    });
                }
            });
        });


        BeanUtils.copyProperties(officialUser,resultData);
        BeanUtils.copyProperties(officialTest,resultData);
        if(resultData.getStateTime()!=null){
            Long duration = resultData.getExpectEndTime().getTime()-resultData.getStartTime().getTime();
            resultData.setCountdown(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue()-resultData.getStateTime());
        }else{
            Long duration = resultData.getEndTime().getTime()-resultData.getStartTime().getTime();
            resultData.setCountdown(duration);
        }
        resultData.setTestDetail(result);
        resultData.setTotalNum(result.getTotalNum());

        return Result.ok(resultData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OFFICIAL_TEST_LOCK_KEY, key = "#{officialTestId}")
    public Result webApiTimingSave(Long officialTestId,Long stateTime,Long userId, OfficiatesTestDTO dto) {
        OfficialTestUserEntity answer = officialTestUserService.getOne(Wrappers.<OfficialTestUserEntity>lambdaQuery().eq(OfficialTestUserEntity::getUserId,userId).eq(OfficialTestUserEntity::getOfficialTestId,officialTestId));
        answer.setStateTime(stateTime);
        PaperDetailDTO testDetail = JSON.parseObject(answer.getTestDetail(),PaperDetailDTO.class);

        dto.getDatas().stream().forEach(s->{
            testDetail.getParagraphDatas().stream().forEach(a1->{
                if(s.getType() != LibraryType.READING_COMPREHENSION.getCode()){
                    if(a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                        a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setReply(s.getReply());
                    }
                }else if(s.getType() == LibraryType.READING_COMPREHENSION.getCode()){
                    //设置答案
                    s.getChildDatas().stream().forEach(a->{

                        a1.getParagraphLibraryDatas().stream().filter(m->m.getType()==LibraryType.READING_COMPREHENSION.getCode()).forEach(z1->{
                            if(z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().isPresent()){
                                z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().get().setReply(a.getReply());
                            }

                        });
                    });
                }
                if(a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                    a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setMark(s.isMark());
                }
            });
        });
        answer.setTestDetail(JSON.toJSONString(testDetail));
        if (!officialTestUserService.updateById(answer)) {
            throw new ResultException("保存考试信息失败");
        }
        return Result.ok();
    }

    @Override
    public Result webApiofficialInfo(Long officialTestId,Long userId) {
        OfficialTestRespDTO resultData = new OfficialTestRespDTO();

        OfficialTestUserEntity officialUser = officialTestUserService.getOne(Wrappers.<OfficialTestUserEntity>lambdaUpdate()
                .eq(OfficialTestUserEntity::getOfficialTestId, officialTestId)
                .eq(OfficialTestUserEntity::getUserId,userId)
        );
        if (StringUtils.isEmpty(officialUser)) {
            return Result.error("考试信息不存在");
        }

        OfficialTestEntity officialTest = baseMapper.selectById(officialTestId);

        if (officialUser.getDoTest() == DoTestType.EXAM_NO.getCode()) {
            officialUser.setStateTime(0L);
            officialUser.setStartTime(new Date());
            Date gmtExpectEndTime = DateUtils.addDateMinutes( officialUser.getStartTime(), Math.toIntExact(officialTest.getTime()));
            officialUser.setExpectEndTime(gmtExpectEndTime);
            officialUser.setDoTest(DoTestType.EXAM_ING.getCode());

            if (!officialTestUserService.updateById(officialUser)) {
                throw new ResultException("获取考试信息失败");
            }
        }
        PaperDetailDTO result = JSON.parseObject(officialUser.getTestDetail(), PaperDetailDTO.class);


        BeanUtils.copyProperties(officialUser,resultData);
        BeanUtils.copyProperties(officialTest,resultData);
        if(resultData.getStateTime()!=null){
            Long duration = resultData.getExpectEndTime().getTime()-resultData.getStartTime().getTime();
            resultData.setCountdown(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue()-resultData.getStateTime());
        }else{
            Long duration = resultData.getEndTime().getTime()-resultData.getStartTime().getTime();
            resultData.setCountdown(duration);
        }
        List<DictItemEntity> entitys = dictItemService.list();
        result.getParagraphDatas().stream().forEach(n->{
            n.getParagraphLibraryDatas().stream().forEach(s->{
                if(entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().isPresent()){
                    s.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().get().getItemName());
                }
            });
        });

        resultData.setTestDetail(result);
        resultData.setTotalNum(result.getTotalNum());
        resultData.setScores(officialUser.getScores());
        return Result.ok(resultData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result webApiTheirPapers(Long officialTestId, OfficiatesTestDTO info,Long userId,Long stateTime) {
        OfficialTestEntity officialTest = getById(officialTestId);

        OfficialTestUserEntity officialUser = officialTestUserService.getOne(Wrappers.<OfficialTestUserEntity>lambdaUpdate()
                .eq(OfficialTestUserEntity::getOfficialTestId, officialTest.getOfficialTestId())
                .eq(OfficialTestUserEntity::getUserId, userId));

        if (officialUser.getDoTest() == DoTestType.EXAM_OVER.getCode()) {
            return Result.error("考试已结束");
        }

        PaperDetailDTO dto = JSON.parseObject(officialUser.getTestDetail(),PaperDetailDTO.class);

        info.getDatas().stream().forEach(s->{
            dto.getParagraphDatas().stream().forEach(a1->{
                if(s.getType() != LibraryType.READING_COMPREHENSION.getCode()){
                    if(a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                        a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setReply(s.getReply());
                    }
                }else if(s.getType() == LibraryType.READING_COMPREHENSION.getCode()){
                    //设置答案
                    s.getChildDatas().stream().forEach(a->{
                        a1.getParagraphLibraryDatas().stream().filter(m->m.getType()==LibraryType.READING_COMPREHENSION.getCode()).forEach(z1->{
                            if(z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().isPresent()){
                                z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().get().setReply(a.getReply());
                            }
                        });
//
                    });
                }
                if(a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                    a1.getParagraphLibraryDatas().stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setMark(s.isMark());
                }
            });
        });


        // 正确数
        AtomicInteger trueNum = new AtomicInteger(0);
        // 错误数
        AtomicInteger falseNum = new AtomicInteger(0);
        // 未做数
        AtomicInteger notMakeNum = new AtomicInteger(0);
        // 分数
        AtomicDouble scores = new AtomicDouble(0);

        //获取所有的题眼
        Set<Long> eyeSet = new HashSet<>();

        // 题眼 总题数
        Map<Long, Integer> eyeCountMap = Maps.newHashMap();

        // 题眼 错误题数
        Map<Long, Integer> eyeErrorCountMap = Maps.newHashMap();

        // 题眼 正确题数
        Map<Long, Integer> eyeCorrectCountMap = Maps.newHashMap();

        // 题眼 未做题数
        Map<Long, Integer> eyeNotMakeCountMap = Maps.newHashMap();


        //设置题眼数据
        dto.getParagraphDatas().forEach(m -> {
            m.getParagraphLibraryDatas().stream().forEach(s->{
                eyeSet.add(s.getEyeType());
            });
        });


        List<PaperParagraphLibraryDTO> errorDatas = new ArrayList<>();
        List<PaperParagraphLibraryDTO> finalErrorDatas = errorDatas;
        eyeSet.stream().forEach(s->{
            AtomicInteger eyeTotal = new AtomicInteger();
            AtomicInteger eyeTrueNum = new AtomicInteger();//正确回答数量
            AtomicInteger eyeFalseNum = new AtomicInteger();//错误回答数量
            AtomicInteger unansweredNum = new AtomicInteger();//未答题数量
            dto.getParagraphDatas().forEach(m -> {
                m.getParagraphLibraryDatas().stream().forEach(n->{
                    if(null!=n.getEyeType()&&n.getEyeType().equals(s)){
                        eyeTotal.getAndIncrement();
                        finalErrorDatas.add(countLibrarys(n,eyeTrueNum,eyeFalseNum,unansweredNum,scores));
                    }
                });
            });
            eyeCountMap.put(s,eyeTotal.get());
            eyeErrorCountMap.put(s,eyeFalseNum.get());
            eyeCorrectCountMap.put(s,eyeTrueNum.get());
            eyeNotMakeCountMap.put(s,unansweredNum.get());

            trueNum.getAndAdd(eyeTrueNum.get());
            falseNum.getAndAdd(eyeFalseNum.get());
            notMakeNum.getAndAdd(unansweredNum.get());
        });
        errorDatas = errorDatas.stream().filter(s-> s!=null).collect(Collectors.toList());
        List<LibraryDetailDTO> errors = JmBeanUtils.entityToDtoList(errorDatas,LibraryDetailDTO.class);

        List<DictItemEntity> entitys = dictItemService.list();
        errors.stream().forEach(s->{
            if(entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().isPresent()){
                s.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().get().getItemName());
            }
            if(entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
        });

        officialUser.setEndTime(new Date());
        officialUser.setStateTime(stateTime);
        officialUser.setIsPass(scores.get() >= officialTest.getPassScore().doubleValue() ? 2 : 1);
        officialUser.setTrueNum(trueNum.get());
        officialUser.setFalseNum(falseNum.get());
        officialUser.setScores(new BigDecimal(scores.get()));
        officialUser.setTestDetail(JSON.toJSONString(dto));
        if(null!=errors&&errors.size()!=0){
            officialUser.setErrorDetail(JSON.toJSONString(errors));
        }
        // 设置 考试结束
        officialUser.setDoTest(DoTestType.EXAM_OVER.getCode());


        if (!officialTestUserService.updateById(officialUser)) {
            throw new ResultException("提交考试信息失败");
        }

        List<SysUserErrorEyeTypeEntity> userErrorEyeTypes = Lists.newArrayList();
        eyeCountMap.forEach((k, v) -> {
            SysUserErrorEyeTypeEntity userErrorEyeType = new SysUserErrorEyeTypeEntity();
            // 封装题眼统计信息
            System.out.println(k + "---" + JSON.toJSONString(v));
            userErrorEyeType.setTestId(officialTestId);
            userErrorEyeType.setUserId(userId);
            userErrorEyeType.setEyeType(k);
            userErrorEyeType.setTotalNum(v);
            userErrorEyeType.setTrueNum(StringUtils.isEmpty(eyeCorrectCountMap.get(k)) ? 0 : eyeCorrectCountMap.get(k));
            userErrorEyeType.setFalseNum(StringUtils.isEmpty(eyeErrorCountMap.get(k)) ? 0 : eyeErrorCountMap.get(k));
            userErrorEyeType.setUnansweredNum(StringUtils.isEmpty(eyeNotMakeCountMap.get(k)) ? 0 : eyeNotMakeCountMap.get(k));
            if(userErrorEyeType.getFalseNum()==0 && userErrorEyeType.getTrueNum()==0){
                userErrorEyeType.setErrorPercentage(1D);
            }else{
                userErrorEyeType.setErrorPercentage(BigDecimal.valueOf(userErrorEyeType.getFalseNum())
                        .divide(BigDecimal.valueOf(userErrorEyeType.getTotalNum()), 2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue());
            }
            userErrorEyeType.setType(ErrorEyeType.OFFICIAL_TEST.getCode());
            userErrorEyeType.setCreateTime(new Date());
            userErrorEyeTypes.add(userErrorEyeType);
        });

        if (!sysUserErrorEyeTypeService.saveBatch(userErrorEyeTypes)) {
            throw new ResultException("提交考试信息失败");
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void officialTestEndOperation() {
        List<OfficialTestEntity> examOfficialTests = baseMapper.selectExpireTest();
        examOfficialTests.stream().forEach(test -> {
            List<OfficialTestUserEntity> testUsers = officialTestUserService.list(Wrappers.<OfficialTestUserEntity>lambdaUpdate()
                    .eq(OfficialTestUserEntity::getOfficialTestId, test.getOfficialTestId())
                    .ne(OfficialTestUserEntity::getDoTest, DoTestType.EXAM_OVER.getCode()));
            testUsers.stream().forEach(s1->{
                // 正确数
                AtomicInteger trueNum = new AtomicInteger(0);
                // 错误数
                AtomicInteger falseNum = new AtomicInteger(0);
                // 未做数
                AtomicInteger notMakeNum = new AtomicInteger(0);
                // 分数
                AtomicDouble scores = new AtomicDouble(0);

                //获取所有的题眼
                Set<Long> eyeSet = new HashSet<>();

                // 题眼 总题数
                Map<Long, Integer> eyeCountMap = Maps.newHashMap();

                // 题眼 错误题数
                Map<Long, Integer> eyeErrorCountMap = Maps.newHashMap();

                // 题眼 正确题数
                Map<Long, Integer> eyeCorrectCountMap = Maps.newHashMap();

                // 题眼 未做题数
                Map<Long, Integer> eyeNotMakeCountMap = Maps.newHashMap();
//                System.out.println("info:"+s1.getTestDetail());
                PaperDetailDTO dto = JSON.parseObject(s1.getTestDetail(),PaperDetailDTO.class);

                //封装答案数据
                dto.getParagraphDatas().forEach(m -> {
                    m.getParagraphLibraryDatas().stream().forEach(s->{
                        eyeSet.add(s.getEyeType());
                    });
                });


                List<PaperParagraphLibraryDTO> errorDatas = new ArrayList<>();
                List<PaperParagraphLibraryDTO> finalErrorDatas = errorDatas;
                eyeSet.stream().forEach(s->{
                    AtomicInteger eyeTotal = new AtomicInteger();
                    AtomicInteger eyeTrueNum = new AtomicInteger();//正确回答数量
                    AtomicInteger eyeFalseNum = new AtomicInteger();//错误回答数量
                    AtomicInteger unansweredNum = new AtomicInteger();//未答题数量
                    dto.getParagraphDatas().forEach(m -> {
                        m.getParagraphLibraryDatas().stream().forEach(n->{
                            log.info("数据：{}",JSON.toJSON(n));
                            if(null!=n.getEyeType()&&n.getEyeType().equals(s)){
                                eyeTotal.getAndIncrement();
                                finalErrorDatas.add(countLibrarys(n,eyeTrueNum,eyeFalseNum,unansweredNum,scores));
                            }
                        });
                    });
                    eyeCountMap.put(s,eyeTotal.get());
                    eyeErrorCountMap.put(s,eyeFalseNum.get());
                    eyeCorrectCountMap.put(s,eyeTrueNum.get());
                    eyeNotMakeCountMap.put(s,unansweredNum.get());

                    trueNum.getAndAdd(eyeTrueNum.get());
                    falseNum.getAndAdd(eyeFalseNum.get());
                    notMakeNum.getAndAdd(unansweredNum.get());
                });
                errorDatas = errorDatas.stream().filter(s-> s!=null).collect(Collectors.toList());
                List<LibraryDetailDTO> errors = JmBeanUtils.entityToDtoList(errorDatas,LibraryDetailDTO.class);

                List<DictItemEntity> entitys = dictItemService.list();
                errors.stream().forEach(s->{
                    if(entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().isPresent()){
                        s.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().get().getItemName());
                    }
                    if(entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                        s.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
                    }
                });
                s1.setStartTime(new Date());
                s1.setEndTime(new Date());
                s1.setStateTime(0L);
                s1.setIsPass(1);
                s1.setTrueNum(trueNum.get());
                s1.setFalseNum(falseNum.get());
                s1.setScores(new BigDecimal(scores.get()));
                s1.setTestDetail(JSON.toJSONString(dto));
                if(null!=errors&&errors.size()!=0){
                    s1.setErrorDetail(JSON.toJSONString(errors));
                }

                // 设置 考试结束
                s1.setDoTest(DoTestType.EXAM_OVER.getCode());

                if (!officialTestUserService.updateById(s1)) {
                    throw new ResultException("提交考试信息失败");
                }

                List<SysUserErrorEyeTypeEntity> userErrorEyeTypes = Lists.newArrayList();
                eyeCountMap.forEach((k, v) -> {
                    SysUserErrorEyeTypeEntity userErrorEyeType = new SysUserErrorEyeTypeEntity();
                    // 封装题眼统计信息
                    System.out.println(k + "---" + JSON.toJSONString(v));
                    userErrorEyeType.setTestId(test.getOfficialTestId());
                    userErrorEyeType.setUserId(s1.getUserId());
                    userErrorEyeType.setEyeType(k);
                    userErrorEyeType.setTotalNum(v);
                    userErrorEyeType.setTrueNum(StringUtils.isEmpty(eyeCorrectCountMap.get(k)) ? 0 : eyeCorrectCountMap.get(k));
                    userErrorEyeType.setFalseNum(StringUtils.isEmpty(eyeErrorCountMap.get(k)) ? 0 : eyeErrorCountMap.get(k));
                    userErrorEyeType.setUnansweredNum(StringUtils.isEmpty(eyeNotMakeCountMap.get(k)) ? 0 : eyeNotMakeCountMap.get(k));
                    if(userErrorEyeType.getFalseNum()==0 && userErrorEyeType.getTrueNum()==0){
                        userErrorEyeType.setErrorPercentage(1D);
                    }else{
                        userErrorEyeType.setErrorPercentage(BigDecimal.valueOf(userErrorEyeType.getFalseNum())
                                .divide(BigDecimal.valueOf(userErrorEyeType.getTotalNum()), 2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue());
                    }
                    userErrorEyeType.setType(ErrorEyeType.OFFICIAL_TEST.getCode());
                    userErrorEyeType.setCreateTime(new Date());
                    userErrorEyeTypes.add(userErrorEyeType);
                });

                if (!sysUserErrorEyeTypeService.saveBatch(userErrorEyeTypes)) {
                    throw new ResultException("提交考试信息失败");
                }
            });
        });

    }

    /**
     * 统计试题正确错误的数据
     * @param s
     * @param trueNum
     * @param falseNum
     * @param unansweredNum
     */
    private PaperParagraphLibraryDTO countLibrarys(PaperParagraphLibraryDTO s, AtomicInteger trueNum, AtomicInteger falseNum, AtomicInteger unansweredNum ,AtomicDouble scores){
        //当试题类型不是阅读理解时
        if(s.getType() == LibraryType.SINGLE_SELECTION.getCode()||s.getType()==LibraryType.MULTIPLE_SELECTION.getCode()||s.getType()==LibraryType.TRUE_OR_FALSE.getCode()){
            if(null==s.getReply()){
                unansweredNum.getAndIncrement();
                return s;
            }else if(s.getAnswer().equals(s.getReply())){
                trueNum.getAndIncrement();
                scores.getAndAdd(s.getScore().doubleValue());
            }else if(!s.getAnswer().equals(s.getReply())){
                falseNum.getAndIncrement();
                return s;
            }
        } else if(s.getType() == LibraryType.FILLS_UP.getCode()){
            if(null== s.getReply()){
                unansweredNum.getAndIncrement();
                return s;
            } else {
                if(!checkFill(s.getReply(),s.getOptionDatas())){
                    trueNum.getAndIncrement();
                    scores.getAndAdd(s.getScore().doubleValue());
                }else{
                    falseNum.getAndIncrement();
                    return s;
                }
            }
        } else if(s.getType() == LibraryType.READING_COMPREHENSION.getCode()){
            PaperParagraphLibraryDTO table = JmBeanUtils.entityToDto(s,PaperParagraphLibraryDTO.class);
            List<LibraryChildDTO> childs = table.getChildDatas();
            AtomicInteger nullCount = new AtomicInteger();//未答题
            AtomicInteger readTrueCount = new AtomicInteger();//正确答题
            AtomicInteger readFalseCount = new AtomicInteger();//错误答题

            List<LibraryChildDTO> newChilds = new ArrayList<>();

            if(null!=childs&&childs.size()!=0){
                childs.stream().forEach(n->{
                    if(n.getType() == LibraryType.SINGLE_SELECTION.getCode()||n.getType()==LibraryType.MULTIPLE_SELECTION.getCode()||n.getType()==LibraryType.TRUE_OR_FALSE.getCode()){
                        if(null == n.getReply()){
                            newChilds.add(n);
                            nullCount.getAndIncrement();
                        }else if(n.getAnswer().equals(n.getReply())){
                            readTrueCount.getAndIncrement();
                            scores.getAndAdd( s.getScore().doubleValue());
                        }else if(!n.getAnswer().equals(n.getReply())){
                            newChilds.add(n);
                            readFalseCount.getAndIncrement();
                        }

                    } else if(n.getType() == LibraryType.FILLS_UP.getCode()){
                        if(null==n.getReply()){
                            newChilds.add(n);
                            nullCount.getAndIncrement();
                        }else {
                            if(!checkFill(n.getReply(),n.getOptionDatas())){
                                readTrueCount.getAndIncrement();
                                scores.getAndAdd( s.getScore().doubleValue());
                            }else{
                                newChilds.add(n);
                                readFalseCount.getAndIncrement();
                                return ;
                            }
                        }
                    }

                });
                if(nullCount.get() !=0){
                    table.setChildDatas(newChilds);
                    unansweredNum.getAndIncrement();
                    return table;
                }
                if(readFalseCount.get() !=0) {
                    table.setChildDatas(newChilds);
                    falseNum.getAndIncrement();
                    return table;
                }else if(readTrueCount.get() !=0 &&readTrueCount.get()==childs.size()) {
                    trueNum.getAndIncrement();
                }

            }
        }
        return null;
    }

    //判断填空题是否正确 true 包含不正确的
    private boolean checkFill(String reply,List<LibraryOptionDTO> optiondatas){
        String[] replay = reply.split(SplitConstant.ANSWER_SPLIT_STR);

        for(int i = 0;i< replay.length;i++){
            String[] answers =  optiondatas.get(i).getOptionContent().split(SplitConstant.ANSWER_CONCAT);
            int falseNum = 0;
            for(String answer : answers){
                if(!replay[i].equals(answer)){
                    falseNum++;
                }
                if(falseNum==answers.length){
                    return true;
                }
            }

        }
        return false;
    }
    /**
     * 获取段落试题数据
     * @param paperId
     * @param disorderSeq
     * @return
     */
    private PaperDetailDTO getParagraphLibrarys(Long paperId,Integer disorderSeq){
        PaperEntity paper = paperService.getById(paperId);
        PaperDetailDTO detailDTO = JmBeanUtils.entityToDto(paper,PaperDetailDTO.class);
        //获取段落数据
        List<PaperParagraphDTO> paperParagraphDatas =  JmBeanUtils.entityToDtoList(paragraphDao.selectList(Wrappers.<PaperParagraphEntity>lambdaQuery()
                .eq(PaperParagraphEntity::getPaperId,paperId).orderByAsc(PaperParagraphEntity::getSeq)
        ),PaperParagraphDTO.class);

        //获取段落试题数据
        paperParagraphDatas.stream().forEach(s->{
            List<PaperParagraphLibraryDTO> paperParagraphLibraryDTOs = JmBeanUtils.entityToDtoList(paragraphLibraryDao.selectList(Wrappers.<PaperParagraphLibraryEntity>lambdaQuery()
                    .eq(PaperParagraphLibraryEntity::getParagraphId,s.getParagraphId())
                    .orderByAsc(PaperParagraphLibraryEntity::getSeq)),PaperParagraphLibraryDTO.class);
            //获取试题
            paperParagraphLibraryDTOs.stream().forEach(m->BeanUtils.copyProperties(libraryService.detail(m.getLibraryId()),m));
            if (DisorderSeqType.YES.getCode() == disorderSeq) {
                // 段落试题 乱序
                Collections.shuffle(paperParagraphLibraryDTOs);
            }
            s.setParagraphLibraryDatas(paperParagraphLibraryDTOs);
        });

        detailDTO.setParagraphDatas(paperParagraphDatas);
        return detailDTO;


    }


    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    /**
     * 封装 考试用户数据入库
     *
     * @param officialTest
     * @param papers
     * @param userIds
     */
    private void insertOfficialTestUser(final OfficialTestEntity officialTest, List<ExamOfficialTestPaperDTO> papers, Set<Long> userIds) {

        List<OfficialTestUserEntity> testUsers = Lists.newArrayList();

        AtomicInteger index = new AtomicInteger(0);
        Set<LibraryEntity> examLibrarys = Sets.newHashSet();

        userIds.stream().forEach(userId -> {

            // 封装 考试用户数据
            OfficialTestUserEntity user = new OfficialTestUserEntity();
            user.setOfficialTestId(officialTest.getOfficialTestId());
            user.setUserId(userId);
            user.setCreateTime(new Date());
            user.setDoTest(DoTestType.EXAM_NO.getCode());
            user.setUserId(userId);
            user.setOfficialTestId(officialTest.getOfficialTestId());
            user.setTrueNum(0);
            user.setFalseNum(0);
            user.setScores(new BigDecimal(0));

            // 封装 考试用户答案数据
            ExamOfficialTestPaperDTO paper = getBalancingTestPaper(papers, index);

            // 题目 进入 已使用状态 集合
            paper.getPaperDatas().getParagraphDatas().stream().forEach(d -> {
                d.getParagraphLibraryDatas().stream().forEach(d1 -> {
                    LibraryEntity library = new LibraryEntity();
                    library.setLibraryId(d1.getLibraryId());
                    library.setIsTest(LibraryTestType.USED.getCode());
                    examLibrarys.add(library);
                });
            });
            user.setPaperId(paper.getPaperId());
            user.setTestDetail(JSON.toJSONString(paper.getPaperDatas()));
            user.setCreateTime(new Date());

            testUsers.add(user);

        });

        // 批量添加用户数据
        if (!officialTestUserService.saveBatch(testUsers)) {
            throw new ResultException("新增考试失败");
        }

        // 修改 试题为已使用
        if (!libraryService.updateBatchById(examLibrarys)) {
            throw new ResultException("新增考试失败");
        }

    }

    /**
     * 负载均衡 试卷
     *
     * @param papers
     * @param index
     * @return
     */
    private ExamOfficialTestPaperDTO getBalancingTestPaper(List<ExamOfficialTestPaperDTO> papers, AtomicInteger index) {
        if (index.get() == papers.size()) {
            index.set(0);
        }
        ExamOfficialTestPaperDTO paper = papers.get(index.getAndSet(index.get() + 1));
        return paper;
    }


    /**
     * 封装 考试Paper数据
     *
     * @param officialTest
     * @param paperIds
     * @return
     */
    private Map<String, Object> insertOfficialTestPaper(final OfficialTestEntity officialTest, Set<Long> paperIds) {
        Map<String, Object> ret = Maps.newHashMap();
        List<OfficialTestPaperEntity> collect = Lists.newArrayList();

        List<ExamOfficialTestPaperDTO> result = Lists.newArrayList();
        AtomicReference<BigDecimal> totalScore = new AtomicReference<>();
        paperIds.forEach(paperId -> {
            PaperEntity paper = paperService.getById(paperId);
            if (StringUtils.isEmpty(paper)) {
                throw new ResultException("试卷信息不存在");
            }
            OfficialTestPaperEntity testPaper = new OfficialTestPaperEntity();
            testPaper.setOfficialTestId(officialTest.getOfficialTestId());
            testPaper.setPaperId(paperId);
            testPaper.setCreateTime(new Date());
            collect.add(testPaper);

            //获取试卷数据
            PaperDetailDTO paperDTOS =  getParagraphLibrarys(paperId,officialTest.getDisorderSeq());

            ExamOfficialTestPaperDTO dto = new ExamOfficialTestPaperDTO();
            dto.setOfficialTestPaperEntity(testPaper);

            dto.setPaperDatas(paperDTOS);
            dto.setTotalScore(paper.getTotalScore());
            dto.setPaperId(paperId);
            totalScore.set(paper.getTotalScore());
            result.add(dto);
        });

        Map<BigDecimal, List<ExamOfficialTestPaperDTO>> checkTotalScore = result.stream().collect(Collectors.groupingBy(ExamOfficialTestPaperDTO::getTotalScore));
        if (checkTotalScore.size() > 1) {
            throw new ResultException("试卷分数不一致");
        }
        ret.put("testPapers", collect);
        ret.put("paperDTOs", result);
        ret.put("totalScore", totalScore.get());
        return ret;
    }

}
