package com.garm.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.TestType;
import com.garm.modules.exam.dto.analyze.TestAnalyzeDTO;
import com.garm.modules.exam.dto.errorowntest.SelfTestListDTO;
import com.garm.modules.exam.dto.errorowntest.SelfTestUserListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorStatisticsDTO;
import com.garm.modules.exam.dto.library.LibraryListDTO;
import com.garm.modules.exam.entity.OfficialTestEntity;
import com.garm.modules.exam.entity.OwnTestEntity;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.OwnTestService;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.SysUserErrorEyeTypeDao;
import com.garm.modules.exam.entity.SysUserErrorEyeTypeEntity;
import com.garm.modules.exam.service.SysUserErrorEyeTypeService;

import javax.servlet.http.HttpServletRequest;


@Service("sysUserErrorEyeTypeService")
@Slf4j
public class SysUserErrorEyeTypeServiceImpl extends ServiceImpl<SysUserErrorEyeTypeDao, SysUserErrorEyeTypeEntity> implements SysUserErrorEyeTypeService {
    @Autowired
    private OfficialTestService officialTestService;
    @Autowired
    private OwnTestService ownTestService;

    @Autowired
    private DictItemService dictItemService;


    @Override
    public PageUtils<OfficialTestErrorListDTO> queryPage(Map<String, Object> params) {
        log.info("考试错题集统计-考试列表 入参 params {}: ", params);
        String serviceType = (String)params.get("serviceType");
        String officialTestName = (String)params.get("officialTestName");

        IPage<OfficialTestEntity> result = officialTestService.page(
                new Query<OfficialTestEntity>().getPage(params),
                Wrappers.<OfficialTestEntity>lambdaQuery()
                        .eq(StringUtils.isNotBlank(serviceType),OfficialTestEntity::getServiceType,serviceType)
                        .like(StringUtils.isNotBlank(officialTestName),OfficialTestEntity::getOfficialTestName,officialTestName)
                        .lt(OfficialTestEntity::getTestEndTime,new Date())
                        .orderByDesc(OfficialTestEntity::getTestEndTime)
        );
        List<OfficialTestErrorListDTO> data = JmBeanUtils.entityToDtoList(result.getRecords(),OfficialTestErrorListDTO.class);

        List<DictItemEntity> entitys = dictItemService.list();
        data.stream().forEach(s->{
            if(entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
        });
        //服务类型名称和题眼名称
        PageUtils<OfficialTestErrorListDTO> list = new PageUtils<OfficialTestErrorListDTO>(data,(int)result.getTotal(),(int)result.getSize(),(int)result.getCurrent());
        return list;
    }

    @Override
    public List<TestAnalyzeDTO.EyeAnalyze> queryAnalyzeInfo(int type) {
        return baseMapper.queryAnalyzeInfo(type);
    }

    @Override
    public Result<PageUtils<OfficialTestErrorStatisticsDTO>> queryOfficialTestErrorStatistics(Map<String, Object> params) {
        log.info("考试错题集统计-错题集统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String eyeType = (String)params.get("eyeType");
        String officialTestId = (String)params.get("officialTestId");
        if (null == officialTestId) {
            return Result.error("考试id officialTestId 参数不能为空");
        }
        OfficialTestEntity examOfficialTest = officialTestService.getById(officialTestId);
        if (examOfficialTest == null) {
            return Result.error("考试数据不存在");
        }
        if (examOfficialTest.getTestEndTime().getTime() > System.currentTimeMillis()) {
            return Result.error("该考试还未结束");
        }
        IPage<OfficialTestErrorStatisticsDTO> result = baseMapper.queryOfficialTestErrorStatistics(new Page(page,limit), TestType.OFFICIAL_TEST, officialTestId,eyeType);
        return Result.ok(new PageUtils(result));
    }

    @Override
    public Result<PageUtils<SelfTestUserListDTO>> querySelfTestUserList(Map<String, Object> params) {
        log.info("自测错题集统计-错题集统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String username = (String)params.get("username");
        String nickname = (String)params.get("nickname");
        return Result.ok(new PageUtils<SelfTestUserListDTO>(baseMapper.querySelfTestUserList(new Page(page,limit),username,nickname)));
    }

    @Override
    public Result detail(Map<String, Object> params) {
        log.info("考生自测列表数据 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String userId = (String)params.get("userId");
        String testName = (String)params.get("testName");
        String serviceType = (String)params.get("serviceType");
        if (null == userId) {
            return Result.error("userId参数不能为空");
        }
        IPage<OwnTestEntity> pages = ownTestService.page(new Page(page,limit), Wrappers.<OwnTestEntity>lambdaQuery()
                .eq(OwnTestEntity::getUserId, userId)
                .eq(StringUtils.isNotBlank(serviceType), OwnTestEntity::getServiceType, serviceType)
                .like(StringUtils.isNotBlank(testName), OwnTestEntity::getOwnTestName, testName)
                .isNotNull(OwnTestEntity::getTestEndTime)
                .orderByDesc(OwnTestEntity::getTestEndTime));
        List<DictItemEntity> entitys = dictItemService.list();
        List<SelfTestListDTO> dataList = pages.getRecords().stream().map(e -> {
            SelfTestListDTO respDTO = new SelfTestListDTO();
            BeanUtils.copyProperties(e, respDTO);
            respDTO.setOwnNum(e.getTotalNum());

            if(entitys.stream().filter(m->m.getDictItemId().equals(respDTO.getServiceType())).findFirst().isPresent()){
                respDTO.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(respDTO.getServiceType())).findFirst().get().getItemName());
            }
            respDTO.setTestMin(new BigDecimal(e.getStateTime()).divide(new BigDecimal(6000), BigDecimal.ROUND_UP).longValue());
            return respDTO;
        }).collect(Collectors.toList());
        PageUtils result = new PageUtils(pages);
        result.setList(dataList);
        return Result.ok(result);
    }

    @Override
    public Result<PageUtils<OfficialTestErrorStatisticsDTO>> queryOwnTestErrorStatistics(Map<String, Object> params) {
        log.info("考试错题集统计-错题集统计列表 入参 params {}: ", params);
        Long limit= Long.valueOf((String)params.get("limit"));
        Long page= Long.valueOf((String)params.get("page"));
        String eyeType = (String)params.get("eyeType");
        String ownTestId = (String)params.get("ownTestId");
        if (null == ownTestId) {
            return Result.error("自测 ownTestId 参数不能为空");
        }
        OwnTestEntity examOfficialTest = ownTestService.getById(ownTestId);
        if (examOfficialTest == null) {
            return Result.error("考试数据不存在");
        }
        if (examOfficialTest.getTestEndTime().getTime() > System.currentTimeMillis()) {
            return Result.error("该考试还未结束");
        }
        IPage<OfficialTestErrorStatisticsDTO> result = baseMapper.queryOfficialTestErrorStatistics(new Page(page,limit), TestType.OWN_TEST,ownTestId, eyeType);
        return Result.ok(new PageUtils(result));
    }

}
