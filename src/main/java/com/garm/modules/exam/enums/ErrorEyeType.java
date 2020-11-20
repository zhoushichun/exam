package com.garm.modules.exam.enums;

/**
 * @author
 * @title ErrorEyeType
 * @description
 * @date 2020/4/28 11:24
 */
public enum ErrorEyeType {

    OFFICIAL_TEST(1, "考试错题统计"),
    USER_ERROR_LIB(2, "用户错题统计");

    int code;
    String desc;

    ErrorEyeType(int code, String desc) {
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
