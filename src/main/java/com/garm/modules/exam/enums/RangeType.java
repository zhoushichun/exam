package com.garm.modules.exam.enums;

/**
 * @author liwt
 * @title RangeType
 * @description 使用范围
 * 使用范围,1-考试,2-自测,3，都有
 * @date 2020/4/28 11:24
 */
public enum RangeType {

    OFFICIAL_TEST(1, "考试"),
    OWN_TEST(2, "自测"),
    ALL(3, "都有");

    Integer code;
    String desc;

    RangeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
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

    public static Integer getCode(String desc){
        for (RangeType type : RangeType.values()) {
            if (desc.equals(type.getDesc())) {
                return type.getCode();
            }
        }
        return null;
    }

}
