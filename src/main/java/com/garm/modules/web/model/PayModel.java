package com.garm.modules.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayModel implements Serializable {

    /**
     * 商品描述
     */
    private String body;

    /**
     * 费用 单位为分
     */
    private BigDecimal fee;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 类型
     */
    private Long serviceType;

    /**
     * 类型名
     */
    private String serviceTypeName;
    /**
     * 购买类型 1-月付 2-学期付
     */
    private Integer type;

    /**
     * 微信H5支付随机字符串，获取code 这个在微信支付调用时会自动加上这个参数 无须设置
     */
    private String code;
}
