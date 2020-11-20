package com.garm.modules.exam.enums;
/**
 * @author liwt
 * @title ChooseAnswerType
 * @description 选择题答案类型 1代表true 2.代表false

 * @date 2020/4/28 11:24
 */
public enum ChooseAnswerType {

    TRUE(1, "对"),
    FALSE(2, "错");

    int code;
    String desc;

    ChooseAnswerType(int code, String desc) {
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

    public static String getCode(String desc) {
        if(TRUE.desc.equals(desc)) {
            return String.valueOf(TRUE.code);
        }else if(FALSE.desc.equals(desc)) {
            return String.valueOf(FALSE.code);
        }else {
            return String.valueOf(0);
        }
    }

}
