package com.garm.modules.exam.enums;

/**
 * @author xq
 * @title LibraryFileDataType
 * @description 试题文件导入数据
 * @date 2020/4/28 11:24
 */
public enum LibraryFileDataType {
    RANGE_TYPE_NAME(0, "使用范围"),
    DIFFICULTY_TYPE_NAME(1, "难度"),
    EYE_TYPE_NAME(2, "题眼"),
    LIBRARY_NAME(3, "题目"),
    ANSWER(4, "答案"),
    ANSWER_ANALYSIS(5, "答案解析"),
    ITEM(6, "选项"),
    ;

    int code;
    String desc;

    LibraryFileDataType(int code, String desc) {
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

}
