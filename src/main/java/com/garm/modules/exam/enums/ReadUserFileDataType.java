package com.garm.modules.exam.enums;

/**
 * @author xq
 * @title ReadLibraryFileDataType
 * @description 用户文件导入数据
 * @date 2020/4/28 11:24
 */
public enum ReadUserFileDataType {

    NICK_NAME(0, "用户名"),
    MOBILE(1, "用户手机号"),
    SERVICE_TYPE_NAME(2, "服务类型"),
    SERVICE_START_TIME(3, "服务开始时间"),
    SERVICE_END_TIME(4, "服务结束时间"),
    TYPE(5, "支付方式")
    ;

    int code;
    String desc;

    ReadUserFileDataType(int code, String desc) {
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
