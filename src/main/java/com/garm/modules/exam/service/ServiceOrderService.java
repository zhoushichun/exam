package com.garm.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.common.utils.wechat.PayResult;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.entity.ServiceOrderEntity;
import com.garm.modules.web.model.PayModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 服务订单
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:39
 */
public interface ServiceOrderService extends IService<ServiceOrderEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils<ServiceOrderDTO> queryPage(Map<String, Object> params);

    /**
     * 微信订单
     * @param payModel
     * @return
     */
    Result addWechartOrder(PayModel payModel) throws Exception;

    /**
     * 获取支付结果
     * @param request
     * @return
     * @throws Exception
     */
    PayResult getWxPayResult(HttpServletRequest request) throws Exception;

    /**
     * 根据订单号修改订单状态
     * @param out_trade_no
     */
    void updateByOrderNo(String out_trade_no);

    /**
     * 微信H5支付参数签名
     * @param payModel
     * @return
     */
    Result signWechartOrder(PayModel payModel) throws Exception;

    /**
     * 阿里支付订单
     * @param payModel
     * @return
     * @throws Exception
     */
    Result addAlipayOrder(PayModel payModel) throws Exception;

}

