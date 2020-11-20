package com.garm.common.utils;

import com.garm.modules.exam.enums.ModelFileType;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 文件上传工具类
 * </p>
 *6
 * @author liwt
 * @since 2020-04-24
 */
@Service
@Slf4j
public class FileUtil  {

    @Value("${file.uploadFolder}")
    private String uploadFolder;//文件存放路径

    @Value("${file.modelFolder}")
    private String modelFolder;//文件存放路径

    /**
     * 保存文件
     * @param multipartFile
     * @param request
     * @return
     */
    public  String saveFile(MultipartFile multipartFile,HttpServletRequest request) {
        String result = "FAIL";
        String fileName = null;
        try {
            //以原来的名称命名,覆盖掉旧的
            fileName = multipartFile.getOriginalFilename();

            String format = fileName.substring(fileName.lastIndexOf(".") + 1);
            Long id = IdUtil.genId();
            String name = id+"." + format;
            String path = uploadFolder+getFilePath(format);
            File fileDir = new File(path);
            result = getFilePath(format)+name;
            if (!fileDir.exists() && !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }
            String storagePath = path + name;
            log.info("上传的文件：" + multipartFile.getName() + "," + multipartFile.getContentType() + "," + multipartFile.getOriginalFilename()
                    +"，保存的路径为：" + storagePath);
            Streams.copy(multipartFile.getInputStream(), new FileOutputStream(storagePath), true);

           result = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +result;//存储路径
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * 删除系统多余的文件
     * @param path
     */
    public void remove(String path){
        String[] paths = path.split("/");
        String newPath = "";
        for(int i=3;i<paths.length;i++){
            newPath +=paths[i]+"/";
        }
        if(!"".equals(newPath)){
            newPath = newPath.substring(0,newPath.length()-1);
            File file = new File(uploadFolder+newPath);
            if(file.exists()){
                file.delete();
            }
        }
    }


    /**
     * 下载系统文件
     * @param fileName
     * @param request
     * @param response
     */
    public void downloadModel(String fileName ,HttpServletRequest request, HttpServletResponse response) {
        // 当前项目下download文件夹
        try (InputStream in = this.getClass().getResourceAsStream(modelFolder + fileName + ".xlsx")){
            XSSFWorkbook xwb = new XSSFWorkbook(in);
            SXSSFWorkbook wb = new SXSSFWorkbook(xwb);
            // 打开流
            OutputStream outputStream = null;
            try {
                outputStream = response.getOutputStream();
                // 设置请求
                response.setContentType("application/application/vnd.ms-excel");
                response.setHeader("Content-disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName+ ".xlsx", "UTF-8"));
                // HSSFWorkbook写入流
                wb.write(outputStream);
                // 刷新流
                outputStream.flush();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (outputStream != null) {
                        // 关闭流
                        outputStream.close();
                    }
                } catch (Exception e2) {
                    log.error(e2.getMessage());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getFilePath(String fomart) {
        String prefix = "/upload/" ;
        if ("jpg".equalsIgnoreCase(fomart) || "png".equalsIgnoreCase(fomart) || "gif".equalsIgnoreCase(fomart)
                || "bmp".equalsIgnoreCase(fomart) || "jpeg".equalsIgnoreCase(fomart)) {
            prefix += "images/";
        } else if ("mp3".equalsIgnoreCase(fomart) || "mp4".equalsIgnoreCase(fomart)) {
            prefix += "video/";
        } else if ("doc".equalsIgnoreCase(fomart) || "docx".equalsIgnoreCase(fomart)) {
            prefix += "doc/";
        } else if ("pdf".equalsIgnoreCase(fomart)) {
            prefix += "pdf/";
        } else if ("xls".equalsIgnoreCase(fomart) || "xlsx".equalsIgnoreCase(fomart)) {
            prefix += "xls/";
        } else if ("txt".equalsIgnoreCase(fomart)) {
            prefix += "txt/";
        } else if ("zip".equalsIgnoreCase(fomart) || "rar".equalsIgnoreCase(fomart)) {
            prefix += "zip/";
        } else if ("ppt".equalsIgnoreCase(fomart) || "pptx".equalsIgnoreCase(fomart)) {
            prefix += "ppt/";
        } else {//其他
            prefix += "other/";
        }
        ;
        return prefix + new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
    }


    public void handleExcel(String fileName, String[] header, List<Map<String, Object>> data,HttpServletResponse response){
        SXSSFWorkbook wb = ExcelUtil.datas2Excel(fileName,header,data);
        // 打开流
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 设置请求
            response.setContentType("application/application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + URLEncoder.encode( fileName+".xlsx", "UTF-8"));
            // HSSFWorkbook写入流
            wb.write(outputStream);
            // 刷新流
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    // 关闭流
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error(e2.getMessage());
            }

        }
    }
}
