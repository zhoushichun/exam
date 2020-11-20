package com.garm.modules.exam.enums;

/**
 * 文件模板映射
 * @author
 * @title ModelFileType
 * @description
 * @date 2020/6/5 10:24
 */
public enum ModelFileType {

    USER_MODEL(1, "userImportModel"),
    LIBRARY_MODEL(2, "libraryImportModel");

    int code;
    String desc;

    ModelFileType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(int code){
        for (ModelFileType e:ModelFileType.values()) {
            if(e.getCode()==code) return e.getDesc();
        }
        return "";
    }
}
