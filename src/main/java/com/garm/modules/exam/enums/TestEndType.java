package com.garm.modules.exam.enums;

/**
 * @author liwt
 * @title TestEndType
 * @description 考试结束标识;1，已结束;2,未结束
 * @date 2020/4/28 11:24
 */
public enum TestEndType {

    YES(1, "已结束"),
    NO(2, "未结束");

    int code;
    String desc;

    TestEndType(int code, String desc) {
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
