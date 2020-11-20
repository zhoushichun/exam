package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.library.FileUploadFailDTO;
import com.garm.modules.exam.dto.library.LibraryDTO;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.library.LibraryPaperListDTO;
import com.garm.modules.exam.entity.LibraryEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
public interface LibraryService extends IService<LibraryEntity> {
    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils<LibraryEntity> queryPage(Map<String, Object> params);

    /**
     * 添加题库-单选.多选.判断.填空
     *
     * @param dto
     * @return
     */
    Result add(LibraryDTO dto);

    /**
     * 添加题库-阅读理解
     *
     * @param dto
     * @return
     */
    Result addRead(LibraryDTO dto);

    /**
     * 编辑题库-单选.多选.判断.填空
     * @param dto
     * @return
     */
    Result edit(LibraryDTO dto);

    /**
     * 编辑题库-阅读理解
     * @param dto
     * @return
     */
    Result editRead(LibraryDTO dto);

    /**
     * 试题详情
     */
    LibraryDetailDTO detail(Long libraryId);

    /**
     * 文件上传
     * @param file
     * @return
     */
    Result<FileUploadFailDTO> uploadFile(MultipartFile file, Long serviceType);

    /**
     * 获取固定试题
     *
     * @return
     */
    Result<PageUtils<LibraryPaperListDTO>> getfixtureLibrarys(Map<String,Object> params);

    /**
     * 获取随机试题
     *
     * @return
     */
    Result<List<LibraryPaperListDTO>> getRandLibrarys(Map<String,Object> params);


    /**
     * 获取id数组的试题数据
     */
    List<LibraryDetailDTO> listLibrarys(List<Long> ids);


}

