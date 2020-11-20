package com.garm.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class JMessage implements Serializable {

    /**
     * 电话号码
     */
    private String phone;
    /**
     * 内容
     */
    private String content;
    /**
     * 签名
     */
    private String signName;

    /**
     * 模板id
     */
    private String templateCode;

    /**
     * 验证码"{\"code\":\"this0is0a0test\"}"
     */
    private Map<String,Object> params;
}
