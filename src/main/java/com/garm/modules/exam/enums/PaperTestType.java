package com.garm.modules.exam.enums;

/**
 * @author liwt
 * @title ExamPaperTestType
 * @description 试卷是否已被用于考试 是否已被考试  1-未被使用过  2-已被使用
 * @date 2020/4/28 15:58
 */
public enum PaperTestType {


    NO(1, "未被使用过"),
    YES(2, "已被使用");

    Integer code;
    String desc;

    PaperTestType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(int code) {
        for (PaperTestType c : PaperTestType.values()) {
            if (c.getCode() == code) {
                return c.getDesc();
            }
        }
        return null;
    }
}
