package com.garm.modules.exam.enums;

/**
 * 是否已考试
 * @author ldx
 * @title DoTestType
 * @description
 * @date 2020/4/26 15:58
 */
public enum DoTestType {


    EXAM_NO(1, "未考试"),
    EXAM_ING(2, "正在进行中"),
    EXAM_OVER(3,"已结束");

    int code;
    String desc;

    DoTestType(int code, String desc) {
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
