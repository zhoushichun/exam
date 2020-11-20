package com.garm.modules.exam.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.*;
import com.garm.modules.exam.constants.SplitConstant;
import com.garm.modules.exam.constants.TestType;
import com.garm.modules.exam.dao.OwnTestTypeDao;
import com.garm.modules.exam.dao.SysUserErrorEyeTypeDao;
import com.garm.modules.exam.dto.library.LibraryChildDTO;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.library.LibraryOptionDTO;
import com.garm.modules.exam.dto.library.LibraryPaperListDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphLibraryDTO;
import com.garm.modules.exam.entity.*;
import com.garm.modules.exam.enums.LibraryType;
import com.garm.modules.exam.enums.RangeType;
import com.garm.modules.exam.enums.TestEndType;
import com.garm.modules.exam.service.LibraryService;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.OfficialTestUserService;
import com.garm.modules.web.dto.error.ErrorTestDetailDTO;
import com.garm.modules.web.dto.error.ErrorTestRespDTO;
import com.garm.modules.web.dto.error.WErrorTestQueryRequestDTO;
import com.garm.modules.web.dto.owntest.request.WOwnTestQueryDTO;
import com.garm.modules.web.dto.owntest.request.WOwnTestRequestDTO;
import com.garm.modules.web.dto.owntest.request.OwnTestDetailReqDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestDetailRespDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestRespDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestStatusCount;
import com.garm.modules.web.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.garm.modules.exam.dao.OwnTestDao;
import com.garm.modules.exam.service.OwnTestService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("ownTestService")
@Slf4j
public class OwnTestServiceImpl extends ServiceImpl<OwnTestDao, OwnTestEntity> implements OwnTestService {

    @Autowired
    private OwnTestTypeDao ownTestTypeDao;

    @Autowired
    private OfficialTestService officialTestService;

    @Autowired
    private OfficialTestUserService officialTestUserService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private SysUserErrorEyeTypeDao sysUserErrorEyeTypeDao;

    private final static String OWN_TEST_EDITOR_KEY = "ownTest::editor::";

    private final static String OWN_TEST_SAVE_KEY = "ownTest::save::";

    private final static String OWN_TEST_DEL_KEY = "ownTest::del::";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OwnTestEntity> page = this.page(
                new Query<OwnTestEntity>().getPage(params),
                new QueryWrapper<OwnTestEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OWN_TEST_EDITOR_KEY,key = "#{dto.ownTestName}")
    public Result<OwnTestDetailRespDTO> generatePaper(WOwnTestRequestDTO dto, UserDTO user) {
        log.info("开始生成自测考试试卷>>>入参{}", dto);
        OwnTestEntity check = baseMapper.selectOne(Wrappers.<OwnTestEntity>lambdaQuery()
                        .eq(OwnTestEntity::getOwnTestName, dto.getOwnTestName())
//                    .eq(OwnTestEntity::getUserId, userModel.getUserId())
        );
        if(null == dto.getOwnTestId()){
            if (!StringUtils.isEmpty(check)) {
                return Result.error("自测试卷名已存在");
            }
            OwnTestEntity ownTest = new OwnTestEntity();
            BeanUtils.copyProperties(dto, ownTest);
            ownTest.setOwnTestId(IdUtil.genId());
            ownTest.setCreateTime(new Date());
            ownTest.setUserId(user.getUserId());

            Map<String,Object> params = new HashMap<>();
            params.put("num",dto.getOwnNum().toString());
            params.put("type",dto.getType().toString());
            params.put("serviceType",dto.getServiceType().toString());
            params.put("difficultyType",dto.getDifficultyType().toString());
            List<Integer> range = new ArrayList<>();
            range.add(RangeType.OWN_TEST.getCode());
            range.add(RangeType.ALL.getCode());
            params.put("range", range);
            params.put("eyeType",dto.getEyeType().toString());
            params.put("libraryIds","");
            List<LibraryPaperListDTO> libraries = libraryService.getRandLibrarys(params).getData();
            List<Long> lists = libraries.stream().map(LibraryPaperListDTO::getLibraryId).collect(Collectors.toList());
            List<LibraryDetailDTO> dtos  = libraryService.listLibrarys(lists);

            ownTest.setTestDetail(JSON.toJSONString(dtos));
            ownTest.setCreateTime(new Date());
            ownTest.setTotalNum(dtos.size());
            if (baseMapper.insert(ownTest) != 1) {
                throw new ResultException("新增自测试卷失败");
            }

            OwnTestTypeEntity ownTestType = new OwnTestTypeEntity();

            BeanUtils.copyProperties(dto, ownTestType);
            ownTestType.setOwnTestId(ownTest.getOwnTestId());
            ownTestType.setId(IdUtil.genId());
            ownTestType.setDifficultyType(dto.getDifficultyType());
            ownTestType.setCreateTime(new Date());

            if(ownTestTypeDao.insert(ownTestType)!=1){
                throw new ResultException("新增自测试题失败");
            }


            OwnTestDetailRespDTO respDTO = new OwnTestDetailRespDTO();
            respDTO.setOwnTestId(ownTest.getOwnTestId());
            respDTO.setDatas(dtos);

            return Result.ok(respDTO);
        } else {
            if (!StringUtils.isEmpty(check) && !check.getOwnTestId().equals(dto.getOwnTestId())) {
                return Result.error("自测试卷名已存在");
            }
            OwnTestEntity ownTest = new OwnTestEntity();
            BeanUtils.copyProperties(dto, ownTest);
            ownTest.setUpdateTime(new Date());



            OwnTestTypeEntity ownTestType = new OwnTestTypeEntity();

            BeanUtils.copyProperties(dto, ownTestType);
            ownTestType.setOwnTestId(ownTest.getOwnTestId());
            ownTestType.setId(IdUtil.genId());
            ownTestType.setDifficultyType(dto.getDifficultyType());
            ownTestType.setCreateTime(new Date());

            if(ownTestTypeDao.insert(ownTestType)!=1){
                throw new ResultException("新增自测试题失败");
            }


            Map<String,Object> params = new HashMap<>();
            params.put("num",dto.getOwnNum().toString());
            params.put("type",dto.getType().toString());
            params.put("serviceType",dto.getServiceType().toString());
            params.put("eyeType",dto.getEyeType().toString());
            List<Integer> range = new ArrayList<>();
            range.add(RangeType.OWN_TEST.getCode());
            range.add(RangeType.ALL.getCode());
            params.put("range", range);
            params.put("difficultyType",dto.getDifficultyType().toString());
            List<Long> ids = dto.getDatas().stream().map(LibraryDetailDTO::getLibraryId).collect(Collectors.toList());
            AtomicReference<String> libraryIds = new AtomicReference<>("");
            ids.stream().forEach(s->{
                libraryIds.updateAndGet(v -> v + s +",");
            });
            String libraryId = libraryIds.get().substring(0,(libraryIds.get().length()-1));
            System.out.println(libraryId);
            params.put("libraryIds",libraryId);
            List<LibraryPaperListDTO> libraries = libraryService.getRandLibrarys(params).getData();
            List<Long> lists = libraries.stream().map(LibraryPaperListDTO::getLibraryId).collect(Collectors.toList());

            List<LibraryDetailDTO> dtos  = libraryService.listLibrarys(lists);
            if(libraries.size()==0){
                throw new ResultException("暂无该条件试题数据");
            }

            dto.getDatas().addAll(dtos);
            ownTest.setTestDetail(JSON.toJSONString(dto.getDatas()));
            ownTest.setTotalNum(dto.getDatas().size());
            if (baseMapper.updateById(ownTest) != 1) {
                throw new ResultException("更新自测试题失败");
            }

            OwnTestDetailRespDTO respDTO = new OwnTestDetailRespDTO();
            respDTO.setDatas(dto.getDatas());
            respDTO.setOwnTestId(dto.getOwnTestId());
            return Result.ok(respDTO);
        }
    }

    @Override
    public Result preview(Long ownTestId) {
        log.info("自测试卷预览信息>>>入参{}", ownTestId);
        OwnTestEntity data  = baseMapper.selectById(ownTestId);

        if(null == data){
            throw new ResultException("自测数据不存在");
        }
        try {
            List<LibraryDetailDTO> result = JSONArray.parseArray(data.getTestDetail(), LibraryDetailDTO.class);
            return Result.ok(result);
        }catch (Exception e){
            throw new ResultException("自测试卷数据格式有误，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OWN_TEST_EDITOR_KEY,key = "#{ownTestId}")
    public Result start(Long ownTestId) {
        log.info("开始自测考试信息>>>入参{}", ownTestId);
        OwnTestEntity ownTest = baseMapper.selectById(ownTestId);
        ownTest.setStartTime(new Date());
        ownTest.setUpdateTime(new Date());

        ownTest.setTestStartTime(new Date());
        ownTest.setExpectEndTime(DateUtils.addDateMinutes(new Date(),ownTest.getTime()));
        if (baseMapper.updateById(ownTest) != 1) {
            throw new ResultException("开始自测考试失败");
        }
        List<LibraryDetailDTO> datas = JSONUtils.jsonToList(ownTest.getTestDetail(), LibraryDetailDTO.class);

        datas.stream().forEach(m->{
            m.setAnswer(null);
            m.setAnswerAnalysis(null);
            if(m.getType() == LibraryType.READING_COMPREHENSION.getCode()){
                m.getChildDatas().stream().forEach(a1->{
                    a1.setAnswer(null);
                    a1.setAnswerAnalysis(null);
                });
            }
        });


        OwnTestDetailRespDTO respDTO = new OwnTestDetailRespDTO();
        respDTO.setOwnTestId(ownTestId);
        respDTO.setDatas(datas);

        if(ownTest.getStateTime()!=null){
            Long duration = ownTest.getExpectEndTime().getTime()-ownTest.getStartTime().getTime();
            System.out.println(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue());
            respDTO.setCountdown(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue()-ownTest.getStateTime());
        }else{
            Long duration = ownTest.getEndTime().getTime()-ownTest.getStartTime().getTime();
            respDTO.setCountdown(duration);
        }

        return Result.ok(respDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OWN_TEST_SAVE_KEY,key = "#{dto.stateTime}")
    public Result saveData(OwnTestDetailReqDTO dto) {
        log.info("保存自测考试数据>>>入参{}", dto);
        //封装答案数据模型
        OwnTestEntity testAnswer = new OwnTestEntity();
        BeanUtils.copyProperties(dto,testAnswer);


        //回答试卷的数据
        List<LibraryDetailDTO> answerInfo = JSON.parseArray(baseMapper.selectById(dto.getOwnTestId()).getTestDetail(),LibraryDetailDTO.class);

        dto.getDatas().stream().forEach(s->{

            if(s.getType() != LibraryType.READING_COMPREHENSION.getCode()){
                if(answerInfo.stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                    answerInfo.stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setReply(s.getReply());
                }
            }else if(s.getType() == LibraryType.READING_COMPREHENSION.getCode()){
                //设置答案
                s.getChildDatas().stream().forEach(a->{
                    answerInfo.stream().filter(m->m.getType()==LibraryType.READING_COMPREHENSION.getCode()).forEach(z1->{
                        if(z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().isPresent()){
                            z1.getChildDatas().stream().filter(j->j.getLibraryId().equals(a.getLibraryId())).findFirst().get().setReply(a.getReply());
                        }
                    });
                });
            }
            if(answerInfo.stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().isPresent()){
                answerInfo.stream().filter(m->m.getLibraryId().equals(s.getLibraryId())).findFirst().get().setMark(s.isMark());
            }

        });

        if(dto.getIsEnd()== TestEndType.YES.getCode()){

            testAnswer.setEndTime(new Date());//自测结束时间
            testAnswer.setTestEndTime(new Date());

            List<OwnTestTypeEntity> ownTestTypes =  ownTestTypeDao.selectList(Wrappers.<OwnTestTypeEntity>lambdaQuery()
                    .eq(OwnTestTypeEntity::getOwnTestId,dto.getOwnTestId()))
                    .stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()-> new TreeSet<OwnTestTypeEntity>(Comparator.comparing(OwnTestTypeEntity::getEyeType))), ArrayList::new));



            OwnTestEntity ownTest = baseMapper.selectOne(Wrappers.<OwnTestEntity>lambdaQuery().eq(OwnTestEntity::getOwnTestId,dto.getOwnTestId()));

            ownTestTypes.forEach(m->{
                SysUserErrorEyeTypeEntity userErrorEyeType = new SysUserErrorEyeTypeEntity();
                userErrorEyeType.setEyeType(m.getEyeType());

                AtomicInteger trueNum = new AtomicInteger();//正确回答数量
                AtomicInteger falseNum = new AtomicInteger();//错误回答数量
                AtomicInteger unansweredNum = new AtomicInteger();//未答题数量
                AtomicInteger totalNum = new AtomicInteger();//题眼总题数
                answerInfo.forEach(s->{
                    if(m.getEyeType().equals(s.getEyeType())){
                        totalNum.getAndIncrement();
                        countLibrarys(s,trueNum,falseNum,unansweredNum);
                    }
                });

                userErrorEyeType.setTestId(ownTest.getOwnTestId());
                userErrorEyeType.setUserId(ownTest.getUserId());
                userErrorEyeType.setCreateTime(new Date());
                userErrorEyeType.setTotalNum(totalNum.intValue());
                userErrorEyeType.setTrueNum(trueNum.intValue());
                userErrorEyeType.setType(TestType.OWN_TEST);
                userErrorEyeType.setUnansweredNum(unansweredNum.intValue());//未答题数量
                userErrorEyeType.setFalseNum(falseNum.intValue());//错题数量
                if(userErrorEyeType.getFalseNum()==0 && userErrorEyeType.getTrueNum()==0){
                    userErrorEyeType.setErrorPercentage(1D);
                }else{
                    userErrorEyeType.setErrorPercentage(BigDecimal.valueOf(userErrorEyeType.getFalseNum())
                            .divide(BigDecimal.valueOf(userErrorEyeType.getTotalNum()), 2, BigDecimal.ROUND_HALF_UP)
                            .doubleValue());
                }
                if (sysUserErrorEyeTypeDao.insert(userErrorEyeType)!=1) {
                    throw new ResultException("保存自测考试数据失败");
                }

            });

            AtomicInteger trueCount = new AtomicInteger();//正确回答数量
            AtomicInteger falseCount = new AtomicInteger();//错误回答数量
            AtomicInteger unansweredCount = new AtomicInteger();//未答题数量

            List<LibraryDetailDTO> dtos = new ArrayList<LibraryDetailDTO>();
            List<LibraryDetailDTO> finalDtos = dtos;

            answerInfo.stream().forEach(s->{
                finalDtos.add(countLibrarys(s,trueCount,falseCount,unansweredCount));

            });
            testAnswer.setTrueNum(trueCount.intValue());//正确答案数量
            testAnswer.setFalseNum(answerInfo.size()-trueCount.intValue());//错题数量

            //错题数据
            dtos = dtos.stream().filter(s-> s!=null).collect(Collectors.toList());

            if(null!=dtos&&dtos.size()!=0){
                testAnswer.setErrorDetail(JSON.toJSONString(dtos));//错题数据
            }
        }

        testAnswer.setTestDetail(JSON.toJSONString(answerInfo));//回答详情数据

        if(baseMapper.updateById(testAnswer)!=1){
            throw new ResultException("保存自测信息失败");
        }
        return Result.ok();
    }

    @Override
    public Result count(Long userId) {
        //自测中
        int testingCount = baseMapper.selectCount(Wrappers.<OwnTestEntity>lambdaQuery().eq(OwnTestEntity::getUserId,userId).isNotNull(OwnTestEntity::getTestStartTime).isNull(OwnTestEntity::getTestEndTime));
        //已结束
        int endCount = baseMapper.selectCount(Wrappers.<OwnTestEntity>lambdaQuery().eq(OwnTestEntity::getUserId,userId).isNotNull(OwnTestEntity::getTestStartTime).isNotNull(OwnTestEntity::getTestEndTime));

        return Result.ok(new OwnTestStatusCount(testingCount,endCount));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = OWN_TEST_DEL_KEY,key = "#{ownTestId}")
    public Result clear(Long ownTestId) {
        log.info("清除自测考试信息>>>入参{}", ownTestId);
        if(baseMapper.deleteById(ownTestId)!=1)
            throw new ResultException("清除自测考试数据失败");
        ownTestTypeDao.delete(Wrappers.<OwnTestTypeEntity>lambdaQuery().eq(OwnTestTypeEntity::getOwnTestId,ownTestId));
        return  Result.ok();
    }

    @Override
    public Result detail(Long ownTestId) {
        log.info("获取自测考试详情>>>入参{}", ownTestId);

        OwnTestEntity data= baseMapper.selectById(ownTestId);
        OwnTestRespDTO responseDTO = new OwnTestRespDTO();
        BeanUtils.copyProperties(data, responseDTO);
        responseDTO.setGmtStartTime(data.getTestStartTime());
        responseDTO.setGmtStartTime(data.getTestStartTime());
        responseDTO.setGmtExpectEndTime(data.getExpectEndTime());
        responseDTO.setGmtCreate(data.getCreateTime());
        responseDTO.setGmtEntTime(data.getEndTime());

        if(responseDTO.getStateTime()!=null){
            Long duration = responseDTO.getGmtExpectEndTime().getTime()-responseDTO.getGmtStartTime().getTime();
            System.out.println(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue());
            responseDTO.setCountdown(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue()-responseDTO.getStateTime());
        }else{
            Long duration = responseDTO.getGmtEntTime().getTime()-responseDTO.getGmtStartTime().getTime();
            responseDTO.setCountdown(duration);
        }

        try{
            responseDTO.setTestDetail( JSONUtils.jsonToList(data.getTestDetail(),LibraryDetailDTO.class));
            if(!StringUtils.isEmpty(data.getErrorDetail())){
                responseDTO.setErrorDetail(JSONUtils.jsonToList(data.getErrorDetail(),LibraryDetailDTO.class));
            }
        }catch(Exception e){
            throw new ResultException("考试详情数据有误");
        }
        return Result.ok(responseDTO);
    }

    @Override
    public Result list(WOwnTestQueryDTO dto,Long userId) {
        log.info("获取自测考试信息>>>入参{}", dto);
        Page page = new Page();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());

        IPage<OwnTestEntity> datas= this.page(page,Wrappers.<OwnTestEntity>lambdaQuery()
                .eq(StringUtils.isEmpty(dto.getServiceId())?false:true,OwnTestEntity::getServiceType,dto.getServiceId())
                .like(StringUtils.isEmpty(dto.getOwnTestName())?false:true,OwnTestEntity::getOwnTestName,dto.getOwnTestName())
                .eq(StringUtils.isEmpty(userId)?false:true, OwnTestEntity::getUserId,userId)
                .isNotNull(dto.getIsEnd()==TestEndType.YES.getCode(),OwnTestEntity::getEndTime)
                .isNull(dto.getIsEnd()==TestEndType.NO.getCode(),OwnTestEntity::getEndTime)
                .ge(StringUtils.isEmpty(dto.getStartTime())?false:true,OwnTestEntity::getStateTime,dto.getStartTime())
                .le(StringUtils.isEmpty(dto.getEndTime())?false:true,OwnTestEntity::getEndTime,dto.getEndTime())
                .isNotNull(OwnTestEntity::getStartTime)
                .orderByDesc(OwnTestEntity::getCreateTime)
        );

        List<OwnTestRespDTO> resultData = datas.getRecords().stream().map(data->{
            OwnTestRespDTO responseDTO = new OwnTestRespDTO();
            responseDTO.setGmtStartTime(data.getTestStartTime());
            responseDTO.setGmtStartTime(data.getTestStartTime());
            responseDTO.setGmtExpectEndTime(data.getExpectEndTime());
            responseDTO.setGmtCreate(data.getCreateTime());
            responseDTO.setGmtEntTime(data.getEndTime());
            responseDTO.setOwnTestId(data.getOwnTestId());
            responseDTO.setOwnTestName(data.getOwnTestName());
            responseDTO.setStateTime(data.getStateTime());
            responseDTO.setTime(data.getTime());
            responseDTO.setTotalNum(data.getTotalNum());
            responseDTO.setTrueNum(data.getTrueNum());
            if(responseDTO.getStateTime()!=null){
                Long duration = responseDTO.getGmtExpectEndTime().getTime()-responseDTO.getGmtStartTime().getTime();
                responseDTO.setCountdown(new BigDecimal(duration.toString()).divide(new BigDecimal("1000")).longValue()-responseDTO.getStateTime());
            }else{
                Long duration = responseDTO.getGmtEntTime().getTime()-responseDTO.getGmtStartTime().getTime();
                responseDTO.setCountdown(duration);
            }
            List<LibraryDetailDTO> detailDTO =JSONUtils.jsonToList(data.getTestDetail(),LibraryDetailDTO.class);

            Long remainderNum = detailDTO.stream().filter(s->!s.isMark()).count();
            responseDTO.setRemainderNum(Integer.parseInt(remainderNum.toString()));

//            try{
//                responseDTO.setTestDetail( JSONUtils.jsonToList(data.getTestDetail(),LibraryDetailDTO.class));
//                if(!StringUtils.isEmpty(data.getErrorDetail())){
//                    responseDTO.setErrorDetail(JSONUtils.jsonToList(data.getErrorDetail(),LibraryDetailDTO.class));
//                }
//            }catch(Exception e){
//                throw new ResultException("考试详情数据有误");
//            }
            return responseDTO;
        }).collect(Collectors.toList());

        PageUtils uto = new PageUtils(datas);

        uto.setList(resultData);

        return Result.successPage(uto);
    }

    @Override
    public Result listErrorTest(WErrorTestQueryRequestDTO dto,Long userId) {
        log.info("获取自测考试信息>>>入参{}", dto);
        Page page = new Page();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());

        IPage<ErrorTestRespDTO> results = null;
        if(dto.getType() == TestType.OFFICIAL_TEST){
            results = baseMapper.queryOfficialPageList(page,dto,userId);
        }else if(dto.getType() == TestType.OWN_TEST){
            results = baseMapper.queryOwnPageList(page,dto,userId);
        }
        results.getRecords().stream().forEach(m-> m.setErrorDetail(null));

        return Result.ok(new PageUtils(results));
    }

    @Override
    public Result getErrorTestDetail(Integer type, Long testId, Long userId)  {
        ErrorTestDetailDTO result = new ErrorTestDetailDTO();
        if(type == TestType.OFFICIAL_TEST){
            OfficialTestUserEntity userEntity = officialTestUserService.getOne(Wrappers.<OfficialTestUserEntity>lambdaQuery().eq(OfficialTestUserEntity::getOfficialTestId,testId).eq(OfficialTestUserEntity::getUserId,userId));
            BeanUtils.copyProperties(userEntity,result);
            OfficialTestEntity testEntity = officialTestService.getById(testId);
            BeanUtils.copyProperties(testEntity,result);
            result.setTestName(testEntity.getOfficialTestName());
            result.setTestId(testId);
            try{
                result.setErrorDetail(JSONUtils.jsonToList(userEntity.getErrorDetail(),LibraryDetailDTO.class));
            }catch(Exception e){
                throw new ResultException("错题详情数据有误");
            }

        }else if(type ==TestType.OWN_TEST ){
            OwnTestEntity ownTestEntity = baseMapper.selectById(testId);
            BeanUtils.copyProperties(ownTestEntity,result);
            result.setTestName(ownTestEntity.getOwnTestName());
            result.setTestId(ownTestEntity.getOwnTestId());
            try{
                result.setErrorDetail(JSONUtils.jsonToList(ownTestEntity.getErrorDetail(),LibraryDetailDTO.class));
            }catch(Exception e){
                throw new ResultException("错题详情数据有误");
            }
        }
        return Result.ok(result);
    }


    /**
     * 统计试题正确错误的数据
     * @param s
     * @param trueNum
     * @param falseNum
     * @param unansweredNum
     */
    private LibraryDetailDTO countLibrarys(LibraryDetailDTO s,AtomicInteger trueNum,AtomicInteger falseNum, AtomicInteger unansweredNum ){
        //当试题类型不是阅读理解时
        if(s.getType() == LibraryType.SINGLE_SELECTION.getCode()||s.getType()==LibraryType.MULTIPLE_SELECTION.getCode()||s.getType()==LibraryType.TRUE_OR_FALSE.getCode()){
            if(null==s.getReply()){
                unansweredNum.getAndIncrement();
                return s;
            }else if(s.getAnswer().equals(s.getReply())){
                trueNum.getAndIncrement();
            }else if(!s.getAnswer().equals(s.getReply())){
                falseNum.getAndIncrement();
                return s;
            }
        } else if(s.getType() == LibraryType.FILLS_UP.getCode()){
            if(null==s.getReply()){
                unansweredNum.getAndIncrement();
                return s;
            } else {
                if(!checkFill(s.getReply(),s.getOptionDatas())){
                    trueNum.getAndIncrement();
                }else{
                    falseNum.getAndIncrement();
                    return s;
                }
            }
        } else if(s.getType() == LibraryType.READING_COMPREHENSION.getCode()){
            LibraryDetailDTO table = JmBeanUtils.entityToDto(s,LibraryDetailDTO.class);
            List<LibraryChildDTO> childs = table.getChildDatas();
            AtomicInteger nullCount = new AtomicInteger();//未答题
            AtomicInteger readTrueCount = new AtomicInteger();//正确答题
            AtomicInteger readFalseCount = new AtomicInteger();//错误答题

            List<LibraryChildDTO> newChilds = new ArrayList<>();
            childs.stream().forEach(n->{
                if(n.getType() == LibraryType.SINGLE_SELECTION.getCode()||n.getType()==LibraryType.MULTIPLE_SELECTION.getCode()||n.getType()==LibraryType.TRUE_OR_FALSE.getCode()){
                    if(null==n.getReply()){
                        newChilds.add(n);
                        nullCount.getAndIncrement();
                    }else if(n.getAnswer().equals(n.getReply())){
                        readTrueCount.getAndIncrement();
                    }else if(!n.getAnswer().equals(n.getReply())){
                        newChilds.add(n);
                        readFalseCount.getAndIncrement();
                    }
                } else if(n.getType() == LibraryType.FILLS_UP.getCode()){
                    if(null==n.getReply()){
                        newChilds.add(n);
                        nullCount.getAndIncrement();
                    } else {
                        if(!checkFill(n.getReply(),n.getOptionDatas())){
                            readTrueCount.getAndIncrement();
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
            }if(readFalseCount.get() !=0) {
                table.setChildDatas(newChilds);
                falseNum.getAndIncrement();
                return table;
            } else if(readTrueCount.get() !=0 &&readTrueCount.get()==childs.size()) {
                trueNum.getAndIncrement();
            }

        }
        return null;
    }

    //判断填空题是否正确   true-包含不正确的
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
}
