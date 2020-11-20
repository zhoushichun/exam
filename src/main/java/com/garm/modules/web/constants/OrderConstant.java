package com.garm.modules.web.constants;

/**
 * 订单常用变量
 * @author RKG
 */
public class OrderConstant {

    /**
     * 订单过期时间 单位：s
     */
    public static final long EXPIRETIME_S = 2*60*60L;

    /**
     * 单位：H
     */
    public static final int EXPIRETIME_H = 2;

    /**
     * 月时间
     */
    public static final int MONTH_DAY = 31;

    /**
     * 季度时间
     */
    public static final int QUARTER_DAY = 186;
    /**
     * 未支付
     */
    public static final int NO_PAY = 1;

    /**
     * 已支付
     */
    public static final int PAY = 2;
}
