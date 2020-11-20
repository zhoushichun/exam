package com.garm.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.*;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dao.OfficialTestPaperDao;
import com.garm.modules.exam.dao.PaperParagraphDao;
import com.garm.modules.exam.dao.PaperParagraphLibraryDao;
import com.garm.modules.exam.dto.library.LibraryChildDTO;
import com.garm.modules.exam.dto.library.LibraryOptionDTO;
import com.garm.modules.exam.dto.paper.PaperDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphDTO;
import com.garm.modules.exam.dto.paper.PaperParagraphLibraryDTO;
import com.garm.modules.exam.entity.*;
import com.garm.modules.exam.enums.LibraryType;
import com.garm.modules.exam.enums.PaperTestType;
import com.garm.modules.exam.service.LibraryService;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.garm.modules.exam.dao.PaperDao;
import com.garm.modules.exam.service.PaperService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;


@Service("paperService")
@Slf4j
public class PaperServiceImpl extends ServiceImpl<PaperDao, PaperEntity> implements PaperService {

    @Autowired
    private PaperParagraphDao paragraphDao;

    @Autowired
    private PaperParagraphLibraryDao paragraphLibraryDao;

    @Autowired
    private OfficialTestPaperDao officialTestPaperDao;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private DictItemService dictItemService;


    private final static String PAPER_EDITOR_KEY = "paper::editor::";
    private final static String PAPER_DEL_KEY = "paper::del::";
    private final static String PAPER_COPY_KEY = "paper::copy::";


    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = PAPER_EDITOR_KEY,key = "#{form.paperName}")
    public Result add(PaperDTO form) {
        log.info("新增试卷信息>>>入参{}", form);

        PaperEntity check = baseMapper.selectOne(Wrappers.<PaperEntity>lambdaQuery()
                .eq(PaperEntity::getPaperName, form.getPaperName()).eq(PaperEntity::getServiceType,form.getServiceType())
        );
        if (!StringUtils.isEmpty(check)) {
            return Result.error("试卷名已存在");
        }
        PaperEntity paper = new PaperEntity();
        BeanUtils.copyProperties(form, paper);
        paper.setPaperId(IdUtil.genId());
        paper.setCreateTime(new Date());
        paper.setUpdateTime(new Date());
        if (baseMapper.insert(paper) != 1) {
            throw new ResultException("新增试卷失败");
        }
        insertLists(paper.getPaperId(),form.getParagraphDatas());

        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = PAPER_EDITOR_KEY,key = "#{form.paperId}#{form.paperName}")
    public Result edit(PaperDTO form) {
        int count = officialTestPaperDao.selectCount(Wrappers.<OfficialTestPaperEntity>lambdaQuery().eq(OfficialTestPaperEntity::getPaperId,form.getPaperId()));
        if(count!=0){
            return Result.error("试卷已被用于考试，无法编辑");
        }
        PaperEntity check = baseMapper.selectOne(Wrappers.<PaperEntity>lambdaQuery()
                .eq(PaperEntity::getPaperName, form.getPaperName())
                .eq(PaperEntity::getServiceType,form.getServiceType())
                .ne(PaperEntity::getPaperId,form.getPaperId())
        );
        if (!StringUtils.isEmpty(check)) {
            return Result.error("试卷名已存在");
        }
        PaperEntity paper = new PaperEntity();
        BeanUtils.copyProperties(form, paper);
        paper.setUpdateTime(new Date());
        if (baseMapper.updateById(paper) != 1) {
            throw new ResultException("编辑试卷失败");
        }

        paragraphDao.delete(Wrappers.<PaperParagraphEntity>lambdaQuery().eq(PaperParagraphEntity::getPaperId,form.getPaperId()));
        paragraphLibraryDao.delete(Wrappers.<PaperParagraphLibraryEntity>lambdaQuery().eq(PaperParagraphLibraryEntity::getPaperId,form.getPaperId()));

        insertLists(paper.getPaperId(),form.getParagraphDatas());

        return Result.ok();
    }

    @Override
    public PaperDetailDTO detail(Long paperId) {
        PaperEntity paper = baseMapper.selectById(paperId);
        if(null==paper){
            throw new ResultException("试卷为空");
        }
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

            paperParagraphLibraryDTOs.stream().forEach(m->{
                int count = libraryService.count(Wrappers.<LibraryEntity>lambdaQuery()
                        .eq(LibraryEntity::getParentLibraryId,m.getLibraryId())
                );
                if(count==1||count==0){
                    m.setChildNum("-");
                }else{
                    m.setChildNum(String.valueOf(count));
                }
            });

            s.setParagraphLibraryDatas(paperParagraphLibraryDTOs);
        });

        detailDTO.setParagraphDatas(paperParagraphDatas);

        return detailDTO;
    }

    @Override
    public void copy(Long paperId) {
        log.info("复制试卷>>>入参{}", paperId);
        PaperEntity paper =  baseMapper.selectById(paperId);

        String paperName =getOnePaperName(paper.getPaperName());

        paper.setPaperId(IdUtil.genId());
        paper.setPaperName(paperName);
        paper.setCreateTime(new Date());
        paper.setIsDel(DeleteConstant.NOT_DELETE);
        paper.setIsTest(PaperTestType.NO.getCode());
        if (baseMapper.insert(paper) != 1) {
            throw new ResultException("复制试卷失败");
        }

        List<PaperParagraphEntity> lists = paragraphDao.selectList(Wrappers.<PaperParagraphEntity>lambdaQuery()
                .eq(PaperParagraphEntity::getPaperId,paperId)
        );

        lists.stream().forEach(examPaperParagraph->{

            List<PaperParagraphLibraryEntity> libarys = paragraphLibraryDao.selectList(Wrappers.<PaperParagraphLibraryEntity>lambdaQuery()
                    .eq(PaperParagraphLibraryEntity::getPaperId,paperId)
                    .eq(PaperParagraphLibraryEntity::getParagraphId,examPaperParagraph.getParagraphId())
            );
            examPaperParagraph.setPaperId(paper.getPaperId());
            examPaperParagraph.setParagraphId(IdUtil.genId());
            examPaperParagraph.setCreateTime(new Date());
            examPaperParagraph.setParagraphNum(libarys.size());
            if (paragraphDao.insert(examPaperParagraph) != 1) {
                throw new ResultException("复制试卷失败");
            }

            libarys.stream().forEach(paragraphLibrary->{
                paragraphLibrary.setId(IdUtil.genId());
                paragraphLibrary.setPaperId(paper.getPaperId());
                paragraphLibrary.setParagraphId(examPaperParagraph.getParagraphId());
                paragraphLibrary.setCreateTime(new Date());
                if (paragraphLibraryDao.insert(paragraphLibrary) != 1) {
                    throw new ResultException("复制试卷失败");
                }
            });
        });

    }

    @Override
    public void export(Integer type,Long paperId, HttpServletResponse response) {
        PaperDetailDTO datas = this.detail(paperId);
        String tPName = datas.getPaperName();
        String fileName = datas.getPaperName();
        CustomXWPFDocument document = new CustomXWPFDocument();
        TestPaperExportUtil.setTilte(document, tPName);

        List<PaperParagraphDTO> paragraps = datas.getParagraphDatas();
        // 段落
        for (PaperParagraphDTO paragrap : paragraps) {
            //段落名称
            List<PaperParagraphLibraryDTO> librarys = paragrap.getParagraphLibraryDatas();

            if (librarys != null && !librarys.isEmpty()) {
                String paragraphName = paragrap.getParagraphName();
                TestPaperExportUtil.setQuestionGroup(document, paragraphName);
            }
            for (PaperParagraphLibraryDTO library : librarys) {
                Integer libraryType = library.getType();

                if(libraryType!= LibraryType.READING_COMPREHENSION.getCode()){
                    String libraryName = library.getLibraryName();
                    TestPaperExportUtil.setQuestion(document, (library.getSeq()+1)+":"+"\t"+libraryName);
                    if(type == 2){

                        if(libraryType != LibraryType.FILLS_UP.getCode()){
                            List<LibraryOptionDTO> item = library.getOptionDatas();
                            item.forEach(s->{
                                TestPaperExportUtil.setItem(document, numToLetter(s.getOptionType())+"\t"+s.getOptionContent());
                            });
                            TestPaperExportUtil.setItem(document,"答案：" +numToLetter(library.getAnswer()));
                        } else {
                            List<LibraryOptionDTO> item = library.getOptionDatas();
                            item.forEach(s->{
                                TestPaperExportUtil.setItem(document,"答案"+s.getOptionType()+"："+ s.getOptionContent());
                            });
                        }
                        TestPaperExportUtil.setItem(document,"答案解析：" +library.getAnswerAnalysis());
                    }else if(type ==1){
                        if(libraryType != LibraryType.FILLS_UP.getCode()){
                            List<LibraryOptionDTO> item = library.getOptionDatas();
                            item.forEach(s->{
                                TestPaperExportUtil.setItem(document, numToLetter(s.getOptionType())+"\t"+s.getOptionContent());
                            });
                        }
                    }


                }else if(libraryType== LibraryType.READING_COMPREHENSION.getCode()){
                    String libraryName = library.getLibraryName();
                    TestPaperExportUtil.setQuestion(document, (library.getSeq()+1)+":"+"\t"+libraryName);
                    List<LibraryChildDTO> childs = library.getChildDatas();
                    final int[] i = {1};
                    childs.stream().forEach(s->{
                        String childName = s.getLibraryName();
                        TestPaperExportUtil.setQuestion(document, (paragrap.getSeq()+1)+"."+ i[0] +" "+childName);
//                        List<LibraryOptionDTO> item = library.getOptionDatas();
//                        item.forEach(m->{
//                            TestPaperExportUtil.setItem(document, numToLetter(m.getOptionType())+"\t\t\t"+m.getOptionContent());
//                        });
                        if(type == 2){

                            if(s.getType() != LibraryType.FILLS_UP.getCode()){
                                List<LibraryOptionDTO> item = s.getOptionDatas();
                                if(null!=item){
                                    item.forEach(m->{
                                        TestPaperExportUtil.setItem(document, numToLetter(m.getOptionType())+"\t"+m.getOptionContent());
                                    });
                                }
                                TestPaperExportUtil.setItem(document,"答案：" +numToLetter(s.getAnswer()));
                            } else {
                                List<LibraryOptionDTO> item = s.getOptionDatas();
                                if(null!=item){
                                    item.forEach(m->{
                                        TestPaperExportUtil.setItem(document,"答案"+m.getOptionType()+"："+ m.getOptionContent());
                                    });
                                }

                            }
                            TestPaperExportUtil.setItem(document,"答案解析：" +s.getAnswerAnalysis());
                        }else if(type ==1){
                            if(s.getType()  != LibraryType.FILLS_UP.getCode()){
                                List<LibraryOptionDTO> item = s.getOptionDatas();
                                if(null!=item){
                                    item.forEach(m->{
                                        TestPaperExportUtil.setItem(document, numToLetter(m.getOptionType())+"\t"+m.getOptionContent());
                                    });
                                }
                            }
                        }
                        i[0]++;
                    });
                }
            }
        }
        try {
            // 打开流
            OutputStream outputStream = null;
            try {
                // 设置请求
                response.setContentType("application/msword"); // word格式
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName + ".docx", "UTF-8"));
                outputStream = response.getOutputStream(); // 直接下载导出
                document.write(outputStream);
                // 刷新流
                outputStream.flush();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new ApiException("文件导出异常，请联系管理员");
            } finally {
                if(null!=outputStream){
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将数字转换成字母
    public static String numToLetter(String input) {
        if(input!=null && !"".equals(input)){
            String[] in = input.split(",");
            String result = "";
            for(String t : in){
                for (byte b : t.getBytes()) {
                    result +=(char) (b + 48)+",";
                }
            }

            return result.substring(0,result.length()-1).toUpperCase();
        }
        return null;
    }

    @Override
    public PageUtils<PaperEntity> queryPage(Map<String, Object> params) {
        String paperName = (String)params.get("paperName");
        String serviceType = (String)params.get("serviceType");
        String paperIds = (String)params.get("paperIds");
//        String libraryIds = (String)params.get("libraryIds");
        String[] ids = org.apache.commons.lang.StringUtils.isNotBlank(paperIds)?paperIds.split(","):null;
        IPage<PaperEntity> page = this.page(
                new Query<PaperEntity>().getPage(params),
                Wrappers.<PaperEntity>lambdaQuery()
                .eq(PaperEntity::getIsDel, DeleteConstant.NOT_DELETE)
                .like(!org.apache.commons.lang.StringUtils.isBlank(paperName),PaperEntity::getPaperName,paperName)
                .eq(!org.apache.commons.lang.StringUtils.isBlank(serviceType),PaperEntity::getServiceType,serviceType)
                .notIn(!org.apache.commons.lang.StringUtils.isBlank(paperIds),PaperEntity::getPaperId,ids)
                .orderByDesc(PaperEntity::getUpdateTime)
        );

        return new PageUtils(page);
    }


    private void insertLists(Long paperId, List<PaperParagraphDTO> lists){
        if(paperParagraphRequestDTOIsRepeat(lists)){
            throw new ResultException("段落名不能重复");
        }
        lists.stream().forEach(s->{
            PaperParagraphEntity examPaperParagraph = new PaperParagraphEntity();
            BeanUtils.copyProperties(s, examPaperParagraph);
            examPaperParagraph.setPaperId(paperId);
            examPaperParagraph.setParagraphId(IdUtil.genId());
            examPaperParagraph.setCreateTime(new Date());
            examPaperParagraph.setParagraphNum(s.getParagraphLibraryDatas().size());
            if (paragraphDao.insert(examPaperParagraph) != 1) {
                throw new ResultException("新增段落失败");
            }

            List<PaperParagraphLibraryDTO> datas = s.getParagraphLibraryDatas();
            datas.stream().forEach(m->{
                PaperParagraphLibraryEntity paragraphLibrary = new PaperParagraphLibraryEntity();
                BeanUtils.copyProperties(m, paragraphLibrary);
                paragraphLibrary.setId(IdUtil.genId());
                paragraphLibrary.setPaperId(paperId);
                paragraphLibrary.setParagraphId(examPaperParagraph.getParagraphId());
                paragraphLibrary.setCreateTime(new Date());
                if (paragraphLibraryDao.insert(paragraphLibrary) != 1) {
                    throw new ResultException("新增段落试题失败");
                }
            });
        });
    }

    /**
     * 判断List<PaperParagraphRequestDTO>的对象code是否有重复，有重复true
     *
     * @param orderList
     * @return
     */
    private Boolean paperParagraphRequestDTOIsRepeat(List<PaperParagraphDTO> orderList) {
        Set<PaperParagraphDTO> set = new TreeSet<PaperParagraphDTO>(new Comparator<PaperParagraphDTO>() {
            @Override
            public int compare(PaperParagraphDTO a, PaperParagraphDTO b) {
                return a.getParagraphName().compareTo(b.getParagraphName());
            }
        });
        set.addAll(orderList);
        if (set.size() < orderList.size()) {
            return true;
        }
        return false;
    }

    /**
     * 复制----获取唯一试卷名
     * @param papaerName
     * @return
     */
    private String getOnePaperName(String papaerName){
        int i = 1;
        String newPapaerName = null;
        StringBuilder  s= new StringBuilder(papaerName);
        System.out.println(s.indexOf("("));
        while(true){
            if(s.indexOf("(")!=-1){
                System.out.println(s.substring(0,s.indexOf("(")));
                s = new StringBuilder(s.substring(0,s.indexOf("(")));
            }
            newPapaerName = s+"("+NumberUtil.int2chineseNum(i)+")";
            PaperEntity check = baseMapper.selectOne(Wrappers.<PaperEntity>lambdaQuery().eq(PaperEntity::getPaperName, newPapaerName));
            if(null !=check){
                i++;
            } else {
                papaerName = newPapaerName;
                break;
            }
        }

        return papaerName;
    }

}
