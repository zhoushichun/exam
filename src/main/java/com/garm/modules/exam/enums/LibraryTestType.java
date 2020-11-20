package com.garm.modules.exam.enums;

/**
 * @author xq
 * @title ExamLibraryTestType
 * @description 是否已被考试  1-未被使用过  2-已被使用
 * @date 2020/4/28 10:39
 */
public enum LibraryTestType {


    NOT_USE(1, "未被使用过"),
    USED(2, "已被使用");

    int code;
    String desc;

    LibraryTestType(int code, String desc) {
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

    public static String getDesc(int code) {
        for (LibraryTestType c : LibraryTestType.values()) {
            if (c.getCode() == code) {
                return c.getDesc();
            }
        }
        return null;
    }
}
