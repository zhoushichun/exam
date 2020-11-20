package com.garm.modules.exam.enums;
/**
 * @author liwt
 * @title DifficultyType
 * @description 难度类型,1-简单,2-中等,3-困难

 * @date 2020/4/28 11:24
 */
public enum DifficultyType {

    SIMPLE(1, "简单"),
    MEDIUM(2, "中等"),
    DIFFICULTY(3, "困难");

    int code;
    String desc;

    DifficultyType(int code, String desc) {
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

    public static Integer getCode(String msg) {
        for (DifficultyType c : DifficultyType.values()) {
            if (msg.equals(c.getDesc())) {
                return c.getCode();
            }
        }
        return null;
    }

}
