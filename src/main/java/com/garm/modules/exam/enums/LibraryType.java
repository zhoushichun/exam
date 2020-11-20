package com.garm.modules.exam.enums;

/**
 * @author xq
 * @title LibraryType
 * @description 试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解
 * @date 2020/4/26 15:58
 */
public enum LibraryType {


    SINGLE_SELECTION(1, "单选题"),
    MULTIPLE_SELECTION(2, "多选题"),
    TRUE_OR_FALSE(3, "判断题"),
    FILLS_UP(4, "填空题"),
    READING_COMPREHENSION(5, "阅读理解");

    int code;
    String desc;

    LibraryType(int code, String desc) {
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
        for (LibraryType c : LibraryType.values()) {
            if (c.getCode() == code) {
                return c.getDesc();
            }
        }
        return null;
    }

    public static Integer getCode(String msg) {
        for (LibraryType c : LibraryType.values()) {
            if (msg.equals(c.getDesc())) {
                return c.getCode();
            }
        }
        return null;
    }
}
