package com.garm.modules.exam.enums;

/**
 * 是否乱序
 * @author ldx
 * @title DoTestType
 * @description
 * @date 2020/4/26 15:58
 */
public enum DisorderSeqType {


    NO(1, "否"),
    YES(2, "是");

    int code;
    String desc;

    DisorderSeqType(int code, String desc) {
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
