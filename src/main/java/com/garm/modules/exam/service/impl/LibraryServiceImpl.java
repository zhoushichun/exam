package com.garm.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.*;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dao.LibraryOptionDao;
import com.garm.modules.exam.dto.library.*;
import com.garm.modules.exam.entity.LibraryOptionEntity;
import com.garm.modules.exam.enums.*;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.garm.modules.exam.dao.LibraryDao;
import com.garm.modules.exam.entity.LibraryEntity;
import com.garm.modules.exam.service.LibraryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service("libraryService")
@Slf4j
public class LibraryServiceImpl extends ServiceImpl<LibraryDao, LibraryEntity> implements LibraryService {
    private static final String REDIS_KEY = "exam::library::";

    @Autowired
    private LibraryOptionDao optionDao;

    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private FileUtil fileUtil;


    @Override
    @RedisLockAable(root = REDIS_KEY + "add::", key = "#{dto.type}")
    @Transactional(rollbackFor = Exception.class)
    public Result add(LibraryDTO dto) {
        log.info("新增题库>>>入参{}", dto);

        Long id = insertLibrary(dto);

        insertLibraryOption(dto.getOptionDatas(),id,dto.getType());

        return Result.ok();
    }

    @Override
    @RedisLockAable(root = REDIS_KEY + "add:read:", key = "#{dto.libraryName}")
    @Transactional(rollbackFor = Exception.class)
    public Result addRead(LibraryDTO dto) {
        log.info("新增题库---阅读理解>>>入参{}", dto);
        Long pid = insertLibrary(dto);

        List<LibraryChildDTO> child = dto.getChildDatas();
        child.stream().forEach(s->{
            LibraryEntity entity = JmBeanUtils.entityToDto(s,LibraryEntity.class);
            entity.setParentLibraryId(pid);
            entity.setLibraryId(IdUtil.genId());
            entity.setCreateTime(new Date());
            baseMapper.insert(entity);
            insertLibraryOption(s.getOptionDatas(),entity.getLibraryId(),entity.getType());
        });
        return Result.ok();
    }

    @Override
    @RedisLockAable(root = REDIS_KEY + "edit::", key = "#{dto.type}")
    @Transactional(rollbackFor = Exception.class)
    public Result edit(LibraryDTO dto) {
        log.info("编辑题库>>>入参{}", dto);

        LibraryEntity examLibrary = baseMapper.selectOne(Wrappers.<LibraryEntity>lambdaQuery()
                        .eq(LibraryEntity::getLibraryName,dto.getLibraryName())
                        .eq(LibraryEntity::getType,dto.getType())
                        .eq(LibraryEntity::getEyeType,dto.getEyeType())
                        .eq(LibraryEntity::getRangeType,dto.getRangeType())
                        .eq(LibraryEntity::getDifficultyType,dto.getDifficultyType())
                        .ne(LibraryEntity::getLibraryId,dto.getLibraryId())
                        .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
        );
        if(examLibrary!=null){
            throw new ResultException("该题目内容的试题已存在！");
        }

        if(null!=dto.getUrl()&&!dto.getUrl().equals(dto.getUrl())){
            fileUtil.remove(dto.getUrl());
        }
        LibraryEntity entity = new LibraryEntity();
        BeanUtils.copyProperties(dto, entity);
        String str = dto.getLibraryNameContent();
        str = str.replaceAll("\"","\'");
        entity.setLibraryNameContent(str);
        entity.setUpdateTime(new Date());
        baseMapper.updateById(entity);

        optionDao.delete(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,dto.getLibraryId()));

        insertLibraryOption(dto.getOptionDatas(),dto.getLibraryId(),dto.getType());

        return Result.ok();
    }

    @Override
    @RedisLockAable(root = REDIS_KEY + "edit:read:", key = "#{dto.type}")
    @Transactional(rollbackFor = Exception.class)
    public Result editRead(LibraryDTO dto) {
        log.info("编辑题库---阅读理解>>>入参{}", dto);
        LibraryEntity examLibrary = baseMapper.selectOne(Wrappers.<LibraryEntity>lambdaQuery()
                .eq(LibraryEntity::getLibraryName,dto.getLibraryName())
                .eq(LibraryEntity::getType,dto.getType())
                .eq(LibraryEntity::getEyeType,dto.getEyeType())
                .eq(LibraryEntity::getRangeType,dto.getRangeType())
                .eq(LibraryEntity::getDifficultyType,dto.getDifficultyType())
                .ne(LibraryEntity::getLibraryId,dto.getLibraryId())
                .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
        );
        if(examLibrary!=null){
            throw new ResultException("该题目内容的试题已存在！");
        }
        if(null!=dto.getUrl()&&!dto.getUrl().equals(dto.getUrl())){
            fileUtil.remove(dto.getUrl());
        }
        LibraryEntity entity = new LibraryEntity();
        BeanUtils.copyProperties(dto, entity);
        String str = dto.getLibraryNameContent();
        str = str.replaceAll("\"","\'");
        entity.setLibraryNameContent(str);
        entity.setUpdateTime(new Date());
        baseMapper.updateById(entity);

        baseMapper.delete(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getParentLibraryId,dto.getLibraryId()));

        List<LibraryChildDTO> child = dto.getChildDatas();
        child.stream().forEach(s->{
            LibraryEntity childentity = JmBeanUtils.entityToDto(s,LibraryEntity.class);

            childentity.setParentLibraryId(entity.getLibraryId());
            childentity.setLibraryId(IdUtil.genId());
            childentity.setCreateTime(new Date());
            baseMapper.insert(childentity);

            optionDao.delete(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,childentity.getLibraryId()));


            insertLibraryOption(s.getOptionDatas(),childentity.getLibraryId(),childentity.getType());


        });
        return Result.ok();
    }

    @Override
    public LibraryDetailDTO detail(Long libraryId) {
        LibraryEntity library = baseMapper.selectById(libraryId);

        LibraryDetailDTO dto = JmBeanUtils.entityToDto(library,LibraryDetailDTO.class);

        List<DictItemEntity> entitys = dictItemService.list();
        if(dto!=null){
            if(entitys.stream().filter(m->m.getDictItemId().equals(dto.getEyeType())).findFirst().isPresent()){
                dto.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(dto.getEyeType())).findFirst().get().getItemName());
            }
            if(entitys.stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().isPresent()){
                dto.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().get().getItemName());
            }

            if(dto.getType()== LibraryType.READING_COMPREHENSION.getCode()){
                List<LibraryEntity> childs = baseMapper.selectList(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getParentLibraryId,libraryId));
                if(null!=childs&&childs.size()!=0){
                    List<LibraryChildDTO> child = JmBeanUtils.entityToDtoList(childs,LibraryChildDTO.class);
                    AtomicInteger len = new AtomicInteger();
                    child.stream().forEach(s->{
                        s.setParentLibraryId(childs.get(len.intValue()).getParentLibraryId());
                        List<LibraryOptionEntity> options= optionDao.selectList(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,s.getLibraryId()));
                        if(null!=options&&options.size()!=0){
                            s.setOptionDatas(JmBeanUtils.entityToDtoList(options, LibraryOptionDTO.class));
                        }
                        len.getAndIncrement();
                    });
                    dto.setChildDatas(child);
                }
            }else{
                List<LibraryOptionEntity> options= optionDao.selectList(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,libraryId));
                if(null!=options&&options.size()!=0){
                    dto.setOptionDatas(JmBeanUtils.entityToDtoList(options, LibraryOptionDTO.class));
                }
            }
        }

        return dto;
    }


    @Override
    public Result<FileUploadFailDTO> uploadFile(MultipartFile file, Long serviceType) {
        if (file != null ) {
            //以原来的名称命名,覆盖掉旧的
            String fileName = ExcelUtil.getFileName(file.getOriginalFilename());

            if (ExcelUtil.isExcel2003(fileName)||ExcelUtil.isExcel2007(fileName)) {

                //成功数量
                AtomicInteger successNum = new AtomicInteger(0);
                //失败数量
                AtomicInteger failNum = new AtomicInteger(0);

                List<FileLibraryFailInfoDTO> failDatas = new ArrayList<FileLibraryFailInfoDTO>();

                List<DictItemEntity> items = dictItemService.list();

                try(InputStream in = file.getInputStream()){
                    //读取文件流数据
                    List<List<List<String>>> results = ExcelUtil.read(in,ExcelUtil.isExcel2003(fileName),5);
                    parseFile(results,items,serviceType,successNum,failNum,failDatas);

                }catch (ResultException ae){
                    log.info(ae.getMessage());
                    throw ae;
                }catch (Exception excelException){
                    excelException.printStackTrace();
                    log.info(excelException.getMessage());
                    throw new ResultException("文件数据上传失败!");
                }

                FileUploadFailDTO failDTO = new FileUploadFailDTO();
                failDTO.setSuccessNum(successNum.intValue());
                failDTO.setFailNum(failNum.intValue());
                failDTO.setFailDatas(failDatas);
                return Result.ok(failDTO);
            }else{
                throw new ResultException("请选择xls或xlsx文件");
            }
        }
        return Result.error("文件不存在");
    }

    @Override
    public Result<List<LibraryPaperListDTO>> getRandLibrarys(Map<String, Object> params) {
        log.info("获取随机试题>>>入参 ExamLibraryListReqDTO{}:", params);
        Integer num = Integer.valueOf((String)params.get("num"));
        String type = (String)params.get("type");
        String serviceType = (String)params.get("serviceType");
        String difficultyType = (String)params.get("difficultyType");
        String libraryIds = (String)params.get("libraryIds");
        String rangeType = (String)params.get("rangeType");
        List<Integer> range = (List<Integer>)params.get("range");
        String eyeType = (String)params.get("eyeType");

        List<LibraryEntity> page = this.list(
                Wrappers.<LibraryEntity>lambdaQuery()
                        .eq(LibraryEntity::getParentLibraryId,0)
                        .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
                        .eq(!StringUtils.isBlank(type),LibraryEntity::getType,type)
                        .eq(!StringUtils.isBlank(serviceType),LibraryEntity::getServiceType,serviceType)
                        .eq(!StringUtils.isBlank(difficultyType),LibraryEntity::getDifficultyType,difficultyType)
                        .in(null!=range&&range.size()!=0,LibraryEntity::getRangeType,range)
                        .in(StringUtils.isNotBlank(rangeType),LibraryEntity::getRangeType,rangeType)
                        .eq(!StringUtils.isBlank(eyeType),LibraryEntity::getEyeType,eyeType)
                        .notIn(StringUtils.isNotBlank(libraryIds),LibraryEntity::getLibraryId,libraryIds.split(","))
                        .orderByDesc(LibraryEntity::getUpdateTime)
        );
        if(null==page ||page.size()==0){
            throw new ResultException("相关类型试题已使用完毕，请联系管理员进行添加");
        }
        int[] idxs = getRandArrays(page.size(),num);

        List<LibraryPaperListDTO> list = new ArrayList<>();
        for(int i=0;i<idxs.length;i++){
            list.add(JmBeanUtils.entityToDto(page.get(idxs[i]),LibraryPaperListDTO.class));
        }
        list.stream().forEach(s->{
            if(dictItemService.list().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( dictItemService.list().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
            int count = baseMapper.selectCount(Wrappers.<LibraryEntity>lambdaQuery()
                    .eq(LibraryEntity::getParentLibraryId,s.getLibraryId())
            );
            if(count==1||count==0){
                s.setChildNum("-");
            }else{
                s.setChildNum(String.valueOf(count));
            }
        });

        return Result.ok(list);
    }

    @Override
    public List<LibraryDetailDTO> listLibrarys(List<Long> ids) {
        List<LibraryEntity> librarys = baseMapper.selectBatchIds(ids);

        List<LibraryDetailDTO> dtos = JmBeanUtils.entityToDtoList(librarys,LibraryDetailDTO.class);

        List<DictItemEntity> entitys = dictItemService.list();
        dtos.stream().forEach(dto->{

            if(entitys.stream().filter(m->m.getDictItemId().equals(dto.getEyeType())).findFirst().isPresent()){
                dto.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(dto.getEyeType())).findFirst().get().getItemName());
            }
            if(entitys.stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().isPresent()){
                dto.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().get().getItemName());
            }

            if(dto.getType()== LibraryType.READING_COMPREHENSION.getCode()){
                List<LibraryEntity> childs = baseMapper.selectList(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getParentLibraryId,dto.getLibraryId()));
                if(null!=childs&&childs.size()!=0){
                    List<LibraryChildDTO> child = JmBeanUtils.entityToDtoList(childs,LibraryChildDTO.class);
                    AtomicInteger len = new AtomicInteger();
                    child.stream().forEach(s->{
                        s.setParentLibraryId(childs.get(len.intValue()).getParentLibraryId());
                        List<LibraryOptionEntity> options= optionDao.selectList(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,s.getLibraryId()));
                        if(null!=options&&options.size()!=0){
                            s.setOptionDatas(JmBeanUtils.entityToDtoList(options, LibraryOptionDTO.class));
                        }
                        len.getAndIncrement();
                    });
                    dto.setChildDatas(child);
                }
            }else{
                List<LibraryOptionEntity> options= optionDao.selectList(Wrappers.<LibraryOptionEntity>lambdaQuery().eq(LibraryOptionEntity::getLibraryId,dto.getLibraryId()));
                if(null!=options&&options.size()!=0){
                    dto.setOptionDatas(JmBeanUtils.entityToDtoList(options, LibraryOptionDTO.class));
                }
            }

        });
        return dtos;
    }

    /**
     * 获取指定范围的随机不重复的数组
     * size 范围大小
     * num  数量
     * @return
     */
    private int[] getRandArrays(int size,int num){
        if(size==1){
            return new int[]{0};
        }

        if(size<=num){
            int[]  redNumber = new int[size];

            for(int i = 0;i<redNumber.length;i++){
                redNumber[i] = i;
            }
            return redNumber;
        }else {
            Random random = new  Random();
            int[]  redBall = new int[size-1];
            for(int i = 0;i<redBall.length;i++){
                redBall[i] = i+1;
            }
            int[]  redNumber = new int[num];            //存储六个随机数的实际数组
            int index = -1;                             //通过随机数字数组下标获取随机数
            for(int i = 0;i<redNumber.length;i++){
                index = random.nextInt(redBall.length-i);   //每次获取数字数组长度-i的随机数，比如第一次循环为33第二次为32，
                redNumber[i] = redBall[index];              //把数字数组随机下标的值赋给实际数组
                int temp = redBall[index];                  //定义一个变量暂存下标为index时的值
                redBall[index] = redBall[redBall.length-1-i];//把下标为index的值与数组下标最后的值交换
                redBall[redBall.length-1-i] = temp;          //交换后，下次循环把数字数组最后的值去掉，从而实现不重复
            }
            return redNumber;
        }

    }

    @Override
    public Result<PageUtils<LibraryPaperListDTO>> getfixtureLibrarys(Map params) {
        log.info("获取固定试题>>>入参 ExamLibraryListReqDTO{}:", params);
        String libraryName = (String)params.get("libraryName");
        String type = (String)params.get("type");
        String serviceType = (String)params.get("serviceType");
        String difficultyType = (String)params.get("difficultyType");
        String libraryIds = (String)params.get("libraryIds");
        String[] ids = StringUtils.isNotBlank(libraryIds)?libraryIds.split(","):null;
        String rangeType = (String)params.get("rangeType");

        String eyeType = (String)params.get("eyeType");


//        List<Integer> range = new ArrayList<>();
//        if(StringUtils.isNotBlank(rangeType)){
//            if(rangeType.equals(RangeType.OFFICIAL_TEST.getCode().toString())){
//                range.add(RangeType.ALL.getCode());
//                range.add(RangeType.OFFICIAL_TEST.getCode());
//            }else if(rangeType.equals(RangeType.OWN_TEST.getCode().toString())){
//                range.add(RangeType.ALL.getCode());
//                range.add(RangeType.OWN_TEST.getCode());
//            }else if(rangeType.equals(RangeType.ALL.getCode().toString())){
//                range.add(RangeType.ALL.getCode());
//                range.add(RangeType.OWN_TEST.getCode());
//                range.add(RangeType.OFFICIAL_TEST.getCode());
//            }
//        }

        IPage<LibraryEntity> page = this.page(
                new Query<LibraryEntity>().getPage(params),
                Wrappers.<LibraryEntity>lambdaQuery()
                        .eq(LibraryEntity::getParentLibraryId,0)
                        .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
                        .like(!StringUtils.isBlank(libraryName),LibraryEntity::getLibraryName,libraryName)
                        .eq(!StringUtils.isBlank(type),LibraryEntity::getType,type)
                        .eq(!StringUtils.isBlank(serviceType),LibraryEntity::getServiceType,serviceType)
                        .eq(!StringUtils.isBlank(difficultyType),LibraryEntity::getDifficultyType,difficultyType)
//                        .in(null!=range&&range.size()!=0,LibraryEntity::getRangeType,range)
                        .in(StringUtils.isNotBlank(rangeType),LibraryEntity::getRangeType,rangeType)
                        .eq(!StringUtils.isBlank(eyeType),LibraryEntity::getEyeType,eyeType)
                        .notIn(StringUtils.isNotBlank(libraryIds),LibraryEntity::getLibraryId,ids)
                        .orderByDesc(LibraryEntity::getUpdateTime)
        );

        if(null==page.getRecords() ||page.getRecords().size()==0){
            throw new ResultException("相关类型试题已使用完毕，请进行添加");
        }
        PageUtils pageUtils = new PageUtils(page);
        List<LibraryPaperListDTO> list = JmBeanUtils.entityToDtoList(pageUtils.getList(),LibraryPaperListDTO.class);

        list.stream().forEach(s->{
            if(dictItemService.list().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( dictItemService.list().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
            int count = baseMapper.selectCount(Wrappers.<LibraryEntity>lambdaQuery()
                    .eq(LibraryEntity::getParentLibraryId,s.getLibraryId())
            );
            if(count==1||count==0){
                s.setChildNum("-");
            }else{
                s.setChildNum(String.valueOf(count));
            }
        });
        //服务类型名称和题眼名称
        PageUtils<LibraryPaperListDTO> result = new PageUtils<>();
        BeanUtils.copyProperties(pageUtils,result);
        result.setList(list);

        return Result.ok(result);
    }

    /**
     * 解析文件流数据
     * @param results  文件数据解析后的集合
     * @param items             所有的字典数据映射
     * @param serviceType       服务名称code
     * @param successNum        成功保存的数量
     * @param failNum          保存失败的数量
     * @param failDatas         失败时保存的数据
     */
    private void parseFile(List<List<List<String>>> results,List<DictItemEntity> items,Long serviceType, AtomicInteger successNum, AtomicInteger failNum,List<FileLibraryFailInfoDTO> failDatas){

        AtomicInteger i = new AtomicInteger(1);

        results.stream().forEach(s->{
            if(i.intValue()!=LibraryType.READING_COMPREHENSION.getCode()){
                handleLibrarys(s,serviceType,items,successNum, failNum, failDatas,i.intValue());
            }else{
                handleRead(s,serviceType,items,successNum, failNum, failDatas);
            }

            i.getAndIncrement();
        });
    }

    /**
     * 封装子题数据
     * @param m                     子题数据
     * @param childDTOs            子题集合
     */
    private void setData(List<String> m,List<LibraryChildDTO> childDTOs){
        LibraryChildDTO dto = new LibraryChildDTO();

        //试题类型
        String libraryType = m.get(ReadLibraryFileDataType.LIBRARY_TYPE.getCode());
        if(libraryType.equals(LibraryType.SINGLE_SELECTION.getDesc())){
            if(!"".equals(m.get(ReadLibraryFileDataType.ANSWER.getCode()))){
                String answer = this.char2Number(m.get(ReadLibraryFileDataType.ANSWER.getCode()));
                dto.setAnswer(answer);
            }


            List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();

            for(int loc = ReadLibraryFileDataType.ITEM.getCode() ;loc <m.size();loc++) {
                if ("".equals(m.get(loc))&&loc==m.size()-1) {
                    continue;
                }
                LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                optionDTO.setOptionContent(m.get(loc));
                optionDTO.setOptionType(String.valueOf((loc-7)));
                optionDTOs.add(optionDTO);
            }
            dto.setOptionDatas(optionDTOs);
        }else if(libraryType.equals(LibraryType.MULTIPLE_SELECTION.getDesc())){//多选题
            if(!"".equals(m.get(ReadLibraryFileDataType.ANSWER.getCode()))){
                String answer = this.char2Number(m.get(ReadLibraryFileDataType.ANSWER.getCode()));
                StringBuffer a = new StringBuffer();
                for (int z = 0; z <answer.length(); z++) {
                    a.append(answer.charAt(z)+",");
                }
                answer = a.substring(0,a.length()-1);
                dto.setAnswer(answer);
            }


            List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
            for(int loc = ReadLibraryFileDataType.ITEM.getCode() ;loc <m.size();loc++) {
                if ("".equals(m.get(loc))&&loc==m.size()-1) {
                    continue;
                }
                LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                optionDTO.setOptionContent(m.get(loc));
                optionDTO.setOptionType(String.valueOf((loc-7)));
                optionDTOs.add(optionDTO);
            }
            dto.setOptionDatas(optionDTOs);

        }else if(libraryType.equals(LibraryType.TRUE_OR_FALSE.getDesc())) {//判断题
            if(!"".equals(m.get(ReadLibraryFileDataType.ANSWER.getCode()))){
                dto.setAnswer(ChooseAnswerType.getCode(m.get(ReadLibraryFileDataType.ANSWER.getCode())));
            }
            List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
            LibraryOptionDTO optionDTO = new LibraryOptionDTO();
            optionDTO.setOptionContent("正确");
            optionDTO.setOptionType("1");
            optionDTOs.add(optionDTO);
            optionDTO = new LibraryOptionDTO();
            optionDTO.setOptionContent("错误");
            optionDTO.setOptionType("2");
            optionDTOs.add(optionDTO);
            dto.setOptionDatas(optionDTOs);
        }else if(libraryType.equals(LibraryType.FILLS_UP.getDesc())){

            List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
            if(!"".equals(m.get(ReadLibraryFileDataType.ANSWER.getCode()))){
                String [] options = m.get(ReadLibraryFileDataType.ANSWER.getCode()).split("、");
                for(int loc = 1 ;loc <=options.length;loc++){
                    if ("".equals(m.get(loc))&&loc==m.size()-1) {
                        continue;
                    }
                    LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                    optionDTO.setOptionContent(options[loc-1]);
                    optionDTO.setOptionType(String.valueOf(loc));
                    optionDTOs.add(optionDTO);
                }
                dto.setOptionDatas(optionDTOs);
            }
        }
        dto.setType(LibraryType.getCode(libraryType));
        dto.setLibraryName(m.get(ReadLibraryFileDataType.CHILD_LIBRARY_NAME.getCode()));
        dto.setLibraryNameContent(m.get(ReadLibraryFileDataType.CHILD_LIBRARY_NAME.getCode()));
        dto.setAnswerAnalysis(m.get(ReadLibraryFileDataType.ANSWER_ANALYSIS.getCode()));

        childDTOs.add(dto);

    }

    /**
     * 处理阅读理解
     * @param s             试题数据聚合
     * @param serviceType   服务类型
     * @param items         选项结合
     * @param successNum    成功数量
     * @param failNum       失败数量
     * @param failDatas     失败数据集合
     */
    private void handleRead(List<List<String>> s,Long serviceType,List<DictItemEntity> items,AtomicInteger successNum, AtomicInteger failNum,List<FileLibraryFailInfoDTO> failDatas){
        AtomicInteger rowNum = new AtomicInteger(1);
        //父题题目
        String content = "";

        //子题数量
        int count = 0;
        //题总数
        int total = 0;

        LibraryDTO readDTO = new LibraryDTO();
        readDTO.setServiceType(serviceType);
        readDTO.setType(LibraryType.READING_COMPREHENSION.getCode());
//        readDTO.setRangeType(RangeType.ALL.getCode());


        List<LibraryChildDTO> childDTOs = new ArrayList<>();

        //之前的阅读理解的末尾行号
        AtomicInteger rowNumEnd = new AtomicInteger(2);
        AtomicInteger rowNumStart = new AtomicInteger(2);

        for(List<String> m : s ) {
            String library = m.get(ReadLibraryFileDataType.LIBRARY_NAME.getCode());
            readDTO.setRangeType(RangeType.getCode(m.get(ReadLibraryFileDataType.RANGE_TYPE_NAME.getCode())));
            readDTO.setLibraryName(library);
            readDTO.setLibraryNameContent("<p>"+library+"</p>");

            //开始读取文件时
            if ("".equals(content)) {
                //content.set(m.get(ReadLibraryFileDataType.LIBRARY_NAME.getCode()));
                content = m.get(ReadLibraryFileDataType.LIBRARY_NAME.getCode());
                //难度
                String difficulty = m.get(ReadLibraryFileDataType.DIFFICULTY_TYPE_NAME.getCode());
                readDTO.setDifficultyType(DifficultyType.getCode(difficulty));
                //题眼
                String eyeTyepName = m.get(ReadLibraryFileDataType.EYE_TYPE_NAME.getCode());
                if(items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                    readDTO.setEyeType( items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().get().getDictItemId());
                }else if(StringUtils.isNotBlank(eyeTyepName)&&!items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                    FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();
                    failData.setRowNum(rowNum.intValue());
                    failData.setPaperName(LibraryType.READING_COMPREHENSION.getDesc());
                    failData.setReason("题眼不正确");
                    failDatas.add(failData);
                    failNum.getAndIncrement();
                    rowNum.getAndIncrement();
                    return;
                }


                //封装数据
                setData(m, childDTOs);
                count++;
                total++;
                if (s.size() == 1) {
                    readDTO.setChildDatas(childDTOs);
                    try{
//                        ValidatorUtils.validateEntity(readDTO, UpdateGroup.class);
                        checkBaseParams(readDTO);
                        this.addRead(readDTO);
                        successNum.getAndIncrement();
                    }catch (ResultException e){
                        FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();

                        failData.setRowNum(rowNum.intValue());
                        failData.setPaperName(LibraryType.getDesc(readDTO.getType()));
                        failData.setReason(e.getMsg());
                        failDatas.add(failData);
                        failNum.getAndIncrement();
                    }
                }
            } else if (content.equals(library)) {


                //封装数据
                setData( m, childDTOs);
                count++;
                total++;
                if (s.size() == count || s.size() == total) {
                    readDTO.setChildDatas(childDTOs);
                    try{
//                        ValidatorUtils.validateEntity(readDTO, UpdateGroup.class);
                        checkBaseParams(readDTO);
                        this.addRead(readDTO);
                        successNum.getAndIncrement();
                    }catch (ResultException e){
                        FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();

                        failData.setRowNum(rowNum.intValue());
                        failData.setPaperName(LibraryType.getDesc(readDTO.getType()));
                        failData.setReason(e.getMsg());
                        failDatas.add(failData);
                        failNum.getAndIncrement();
                    }
                }
            } else if (!content.equals(library)) {

                if (count != 0) {
                    readDTO.setRangeType(RangeType.getCode(m.get(ReadLibraryFileDataType.RANGE_TYPE_NAME.getCode())));
                    readDTO.setLibraryName(content);
                    readDTO.setLibraryNameContent("<p>"+content+"</p>");
                    readDTO.setChildDatas(childDTOs);
                    try{
//                        ValidatorUtils.validateEntity(readDTO, UpdateGroup.class);
                        checkBaseParams(readDTO);
                        this.addRead(readDTO);
                        successNum.getAndIncrement();
                    }catch (ResultException e){
                        FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();

                        failData.setRowNum(rowNum.intValue());
                        failData.setPaperName(LibraryType.getDesc(readDTO.getType()));
                        failData.setReason(e.getMsg());
                        failDatas.add(failData);
                        failNum.getAndIncrement();
                    }
                    count = 0;
                    childDTOs.clear();
                    rowNumStart.set(rowNumEnd.intValue());
                }

                //content.set(m.get(ReadLibraryFileDataType.LIBRARY_NAME.getCode()));
                content = m.get(ReadLibraryFileDataType.LIBRARY_NAME.getCode());

                //封装数据
                setData(m, childDTOs);

                //难度
                String difficulty = m.get(ReadLibraryFileDataType.DIFFICULTY_TYPE_NAME.getCode());
                readDTO.setDifficultyType(DifficultyType.getCode(difficulty));
                //题眼
                String eyeTyepName = m.get(ReadLibraryFileDataType.EYE_TYPE_NAME.getCode());
                if(items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                    readDTO.setEyeType( items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().get().getDictItemId());
                }else if(StringUtils.isNotBlank(eyeTyepName)&&!items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                    FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();
                    failData.setRowNum(rowNum.intValue());
                    failData.setPaperName(LibraryType.READING_COMPREHENSION.getDesc());
                    failData.setReason("题眼不正确");
                    failDatas.add(failData);
                    failNum.getAndIncrement();
                    rowNum.getAndIncrement();
                    return;
                }
                total++;
            }

            rowNumEnd.getAndIncrement();
            rowNum.getAndIncrement();
        }
    }



    /**
     * 处理单选/多选/判断/填空题
     * @param s             试题数据聚合
     * @param serviceType   服务类型
     * @param items         选项结合
     * @param successNum    成功数量
     * @param failNum       失败数量
     * @param failDatas     失败数据集合
     */
    private void handleLibrarys(List<List<String>> s,Long serviceType,List<DictItemEntity> items,AtomicInteger successNum, AtomicInteger failNum,List<FileLibraryFailInfoDTO> failDatas,int type){
        AtomicInteger rowNum = new AtomicInteger(2);
        s.forEach(m->{
            LibraryDTO dto = new LibraryDTO();
            dto.setServiceType(serviceType);
            dto.setType(type);
            //设置使用范围(设置默认值为都有)
            dto.setRangeType(RangeType.getCode(m.get(LibraryFileDataType.RANGE_TYPE_NAME.getCode())));
            if(type==1){
                if(!"".equals(m.get(LibraryFileDataType.ANSWER.getCode()))){
                    dto.setAnswer(this.char2Number(m.get(LibraryFileDataType.ANSWER.getCode())));
                }

                List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
                for(int loc = LibraryFileDataType.ITEM.getCode() ;loc <m.size();loc++){
                    if ("".equals(m.get(loc))&&loc==m.size()-1) {
                        continue;
                    }
                    LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                    optionDTO.setOptionContent(m.get(loc));
                    optionDTO.setOptionType(String.valueOf((loc-5)));
                    optionDTOs.add(optionDTO);
                }
                dto.setOptionDatas(optionDTOs);
            }else if(type==2){
                if(!"".equals(m.get(LibraryFileDataType.ANSWER.getCode()))){
                    String answer = this.char2Number(m.get(LibraryFileDataType.ANSWER.getCode()));

                    StringBuffer a = new StringBuffer();
                    for (int z = 0; z <answer.length(); z++) {
                        a.append(answer.charAt(z)+",");
                    }
                    answer = a.substring(0,a.length()-1);
                    dto.setAnswer(answer);
                }


                List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
                for(int loc = LibraryFileDataType.ITEM.getCode() ;loc <m.size();loc++){
                    if ("".equals(m.get(loc))&&loc==m.size()-1) {
                        continue;
                    }
                    LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                    optionDTO.setOptionContent(m.get(loc));
                    optionDTO.setOptionType(String.valueOf((loc - 5)));
                    optionDTOs.add(optionDTO);
                }
                dto.setOptionDatas(optionDTOs);
            }else if(type==3){
                if(!"".equals(m.get(LibraryFileDataType.ANSWER.getCode()))){
                    dto.setAnswer(ChooseAnswerType.getCode(m.get(LibraryFileDataType.ANSWER.getCode())));
                }
                List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();
                LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                optionDTO.setOptionContent("正确");
                optionDTO.setOptionType("1");
                optionDTOs.add(optionDTO);
                optionDTO = new LibraryOptionDTO();
                optionDTO.setOptionContent("错误");
                optionDTO.setOptionType("2");
                optionDTOs.add(optionDTO);
                dto.setOptionDatas(optionDTOs);
            }else if(type==4){

                List<LibraryOptionDTO> optionDTOs = new ArrayList<LibraryOptionDTO>();

                if(!"".equals(m.get(LibraryFileDataType.ANSWER.getCode()))){
                    String [] options = m.get(LibraryFileDataType.ANSWER.getCode()).split("、");
                    for(int loc = 1 ;loc <=options.length;loc++){
                        if(!"".equals(m.get(loc))&&loc!=m.size()-1) {
                            LibraryOptionDTO optionDTO = new LibraryOptionDTO();
                            optionDTO.setOptionContent(options[loc - 1]);
                            optionDTO.setOptionType(String.valueOf(loc));
                            optionDTOs.add(optionDTO);
                        }
                    }
                    dto.setOptionDatas(optionDTOs);
                }
            }


            dto.setAnswerAnalysis(m.get(LibraryFileDataType.ANSWER_ANALYSIS.getCode()));
            dto.setLibraryName(m.get(LibraryFileDataType.LIBRARY_NAME.getCode()));
            dto.setLibraryNameContent("<p>"+m.get(LibraryFileDataType.LIBRARY_NAME.getCode())+"</p>");
            String difficulty = m.get(LibraryFileDataType.DIFFICULTY_TYPE_NAME.getCode());
            dto.setDifficultyType(DifficultyType.getCode(difficulty));
            String eyeTyepName = m.get(LibraryFileDataType.EYE_TYPE_NAME.getCode());
            if(items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                dto.setEyeType( items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().get().getDictItemId());
            }else if(StringUtils.isNotBlank(eyeTyepName)&&!items.stream().filter(n->n.getItemName().equals(eyeTyepName)).findFirst().isPresent()){
                FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();
                failData.setRowNum(rowNum.intValue());
                failData.setPaperName(LibraryType.getDesc(dto.getType()));
                failData.setReason("题眼不正确");
                failDatas.add(failData);
                failNum.getAndIncrement();
                rowNum.getAndIncrement();
                return;
            }

            try{
//                ValidatorUtils.validateEntity(dto, AddGroup.class);
                checkBaseParams(dto);
                this.add(dto);
                successNum.getAndIncrement();
            }catch (ResultException e){
                FileLibraryFailInfoDTO failData = new FileLibraryFailInfoDTO();
                failData.setRowNum(rowNum.intValue());
                failData.setPaperName(LibraryType.getDesc(dto.getType()));
                failData.setReason(e.getMsg());
                failDatas.add(failData);
                failNum.getAndIncrement();
            }
            rowNum.getAndIncrement();
        });
    }
    // 将字母转换成数字_1
    private String char2Number(String input) {
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();
        if (null != input && !"".equals(input)) {
            for (char c : input.toCharArray()) {
                if (String.valueOf(c).matches(reg)) {
                    strBuf.append(c - 96);
                } else {
                    strBuf.append(c);
                }
            }
            return strBuf.toString();
        } else {
            return input;
        }
    }

    /**
     * 试题选项新增
     * @param list  选项集合
     * @param id
     * @param type  试题类型
     */
    private void insertLibraryOption(List<LibraryOptionDTO> list,Long id,Integer type){
        if(type==LibraryType.SINGLE_SELECTION.getCode()||type==LibraryType.MULTIPLE_SELECTION.getCode()){
            if(null==list||list.size()<2){
                throw new ResultException("试题选项至少为2项");
            }
        }else if(type==LibraryType.FILLS_UP.getCode()){
            if(null==list||list.size()<1){
                throw new ResultException("试题选项至少为1项");
            }
        }

        List<LibraryOptionEntity> optionEntitys = JmBeanUtils.entityToDtoList(list,LibraryOptionEntity.class);
        optionEntitys.stream().forEach(s->{
            if(StringUtils.isBlank(s.getOptionContent())){
                throw new ResultException("请填写试题选项内容");
            }
            s.setLibraryId(id);
            optionDao.insert(s);
        });
    }

    /**
     * 试题新增
     * @param dto  试题数据集合
     * @return
     */
    private Long insertLibrary(LibraryDTO dto){
        LibraryEntity examLibrary = baseMapper.selectOne(Wrappers.<LibraryEntity>lambdaQuery()
                .eq(LibraryEntity::getLibraryName,dto.getLibraryName())
//                .eq(LibraryEntity::getLibraryNameContent,dto.getLibraryNameContent())
                .eq(LibraryEntity::getType,dto.getType())
                .eq(LibraryEntity::getEyeType,dto.getEyeType())
                .eq(LibraryEntity::getRangeType,dto.getRangeType())
                .eq(LibraryEntity::getDifficultyType,dto.getDifficultyType())
                .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
        );
        if(examLibrary!=null){
            throw new ResultException("该题目内容的试题已存在！");
        }
        if(null==dto.getAnswer()){
            if(dto.getType()==LibraryType.SINGLE_SELECTION.getCode()||dto.getType()==LibraryType.MULTIPLE_SELECTION.getCode()||dto.getType()==LibraryType.TRUE_OR_FALSE.getCode()){
                throw new ResultException("请勾选答案");
            }
        }
        Long id = IdUtil.genId();
        LibraryEntity entity = new LibraryEntity();
        BeanUtils.copyProperties(dto, entity);
        String str = dto.getLibraryNameContent();
        str = str.replaceAll("\"","\'");
        entity.setLibraryNameContent(str);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setLibraryId(id);
        baseMapper.insert(entity);
        return id;
    }

    @Override
    public PageUtils<LibraryEntity> queryPage(Map<String, Object> params) {
        String libraryName = (String)params.get("libraryName");
        String type = (String)params.get("type");
        String serviceType = (String)params.get("serviceType");
        String eyeType = (String)params.get("eyeType");
        String difficultyType = (String)params.get("difficultyType");
        String rangeType = (String)params.get("rangeType");
        String answerAnalysis = (String)params.get("answerAnalysis");

        List<LibraryEntity> datas = this.list(Wrappers.<LibraryEntity>lambdaQuery().like(!StringUtils.isBlank(answerAnalysis),LibraryEntity::getAnswerAnalysis,answerAnalysis));
        List<Long> ids = null;
        if(null!=datas && datas.size()!=0){
            List<Long> pIds = datas.stream().map(s->s.getParentLibraryId()).collect(Collectors.toList());

            ids = datas.stream().map(s->s.getLibraryId()).collect(Collectors.toList());
            ids.addAll(pIds);
        }

        IPage<LibraryEntity> page = this.page(
                new Query<LibraryEntity>().getPage(params),
                Wrappers.<LibraryEntity>lambdaQuery()
                        .eq(LibraryEntity::getParentLibraryId,0)
                        .eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
                        .like(!StringUtils.isBlank(libraryName),LibraryEntity::getLibraryName,libraryName)
//                        .like(!StringUtils.isBlank(answerAnalysis),LibraryEntity::getAnswerAnalysis,answerAnalysis)
                        .in(null!=ids,LibraryEntity::getLibraryId,ids)
                        .eq(!StringUtils.isBlank(type),LibraryEntity::getType,type)
                        .eq(!StringUtils.isBlank(serviceType),LibraryEntity::getServiceType,serviceType)
                        .eq(!StringUtils.isBlank(eyeType),LibraryEntity::getEyeType,eyeType)
                        .eq(!StringUtils.isBlank(difficultyType),LibraryEntity::getDifficultyType,difficultyType)
                        .eq(!StringUtils.isBlank(rangeType),LibraryEntity::getRangeType,rangeType)
                        .orderByDesc(LibraryEntity::getUpdateTime)
        );

        return new PageUtils(page);
    }


    /**
     * 参数校验
     *
     * @param dto
     * @return
     */
    private boolean checkBaseParams(LibraryDTO dto) {

        if (dto.getDifficultyType() == null) {
            throw new ResultException( "难度不能为空");
        }
        if (dto.getEyeType() == null) {
            throw new ResultException("题眼不能为空");
        }
        if (dto.getRangeType() == null) {
            throw new ResultException("使用范围不能为空");
        }
        if (StringUtils.isBlank(dto.getLibraryName())) {
            throw new ResultException("题目内容不能为空");
        }
        if(dto.getType()!=LibraryType.READING_COMPREHENSION.getCode()&&dto.getType()!=LibraryType.FILLS_UP.getCode()){
            if (StringUtils.isBlank(dto.getAnswer())) {
                throw new ResultException("答案不能为空");
            }
            if (StringUtils.isBlank(dto.getAnswerAnalysis())) {
                throw new ResultException("答案解析不能为空");
            }
        }else if (dto.getType()!=LibraryType.FILLS_UP.getCode()){
            dto.getChildDatas().stream().forEach(s->{
                if (s.getType()!=LibraryType.FILLS_UP.getCode()&&StringUtils.isBlank(s.getAnswer())) {
                    throw new ResultException("答案不能为空");
                }
                if (s.getType()!=LibraryType.FILLS_UP.getCode()&&StringUtils.isBlank(s.getAnswerAnalysis())) {
                    throw new ResultException("答案解析不能为空");
                }
            });

        }else if(dto.getType()==LibraryType.FILLS_UP.getCode()){
            if (null==dto.getOptionDatas()||dto.getOptionDatas().size()==0) {
                throw new ResultException("答案不能为空");
            }
            if (null==dto.getOptionDatas()||dto.getOptionDatas().size()==0) {
                throw new ResultException("答案解析不能为空");
            }
        }

        if(dto.getType()!=LibraryType.READING_COMPREHENSION.getCode()){
            List<LibraryOptionDTO> list = dto.getOptionDatas();
            checkOption(list);

        }else if (dto.getType() ==LibraryType.READING_COMPREHENSION.getCode()){
            dto.getChildDatas().stream().forEach(m->{
                List<LibraryOptionDTO> list = m.getOptionDatas();
                checkOption(list);
            });

        }

        return true;
    }

    private void checkOption(List<LibraryOptionDTO> list){

        List<LibraryOptionEntity> optionEntitys = JmBeanUtils.entityToDtoList(list,LibraryOptionEntity.class);
        AtomicInteger i = new AtomicInteger(1);
        optionEntitys.stream().forEach(s->{
            if(StringUtils.isBlank(s.getOptionContent())){
                throw new ResultException("选项"+numberToLetter(i.get())+"为空");
            }
            i.getAndIncrement();
        });
    }

    //数字转字母 1-26 ： A-Z
    public String numberToLetter(int num) {
        if (num <= 0) {
            return null;
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter = ((char) (num % 26 + (int) 'A')) + letter;
            num = (int) ((num - num % 26) / 26);
        } while (num > 0);

        return letter;
    }


}
