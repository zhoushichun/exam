package com.garm.modules.exam.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.*;
import com.garm.common.utils.wechat.*;
import com.garm.config.AlipayConfig;
import com.garm.config.WechatConfig;
import com.garm.modules.exam.constants.UserType;
import com.garm.modules.exam.dao.SysUserServiceDao;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.service.SysUserService;
import com.garm.modules.web.constants.OrderConstant;
import com.garm.modules.web.constants.PayTypeConstant;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.web.dao.UserDao;
import com.garm.modules.web.dto.pay.AliPayOrderDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.model.PayModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.garm.modules.exam.dao.ServiceOrderDao;
import com.garm.modules.exam.entity.ServiceOrderEntity;
import com.garm.modules.exam.service.ServiceOrderService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service("serviceOrderService")
@Slf4j
public class ServiceOrderServiceImpl extends ServiceImpl<ServiceOrderDao, ServiceOrderEntity> implements ServiceOrderService {

    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private SysUserServiceDao sysUserServiceDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PageUtils<ServiceOrderDTO> queryPage(Map<String, Object> params) {
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");
        Long page = Long.valueOf((String)params.get("page"));
        Long limit = Long.valueOf((String)params.get("limit"));

        IPage<ServiceOrderDTO> result = baseMapper.selectList(new Page().setCurrent(page).setSize(limit),startTime,endTime);

        return new PageUtils(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addWechartOrder(PayModel payModel) throws Exception {
        // 生成预付单对象
        PreOrder o = new PreOrder();
        // 生成随机字符串
        String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");
        o.setAppid(wechatConfig.getAPPID());
        o.setBody(payModel.getBody());
        o.setMch_id(wechatConfig.getMCH_ID());
        o.setNotify_url(wechatConfig.getNOTIFY_URL());
        o.setOut_trade_no(payModel.getOrderNo());
        // 判断有没有输入订单总金额，没有输入默认1分钱
        if (payModel.getFee() != null && !payModel.getFee().equals("")) {
            o.setTotal_fee(payModel.getFee().multiply(BigDecimal.valueOf(100)).intValue());
        } else {
            o.setTotal_fee(1);
        }
        o.setNonce_str(nonce_str);
        o.setTrade_type(TradeType.NATIVE);
        o.setSpbill_create_ip(wechatConfig.getSPBILL_CREATE_IP());
        SortedMap<Object, Object> p = o.beanToMap();

        // 获得签名
        String sign = Sign.createSign("utf-8", p, wechatConfig.getKEY());
        o.setSign(sign);
        // Object转换为XML
        String xml = XmlUtil.object2Xml(o, PreOrder.class);
        // 统一下单地址
        String url = wechatConfig.getPLACEANORDER_URL();
        // 调用微信统一下单地址
        String returnXml = HttpUtil.sendPost(url, xml);
        // XML转换为Object
        PreOrderResult preOrderResult = (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
        if ("FAIL".equalsIgnoreCase(preOrderResult.getReturn_code())) {
            return Result.error(preOrderResult.getReturn_msg());
        }
        //添加数据库和redis
        insertOrder(payModel, PayTypeConstant.WECHAT);
        Map<String,Object> re = new HashMap<>();
        re.put("orderNo",payModel.getOrderNo());
        re.put("fee",payModel.getFee());
        re.put("codeUrl",preOrderResult.getCode_url());
        return Result.ok(re);
    }

    @Override
    public PayResult getWxPayResult(HttpServletRequest request) throws Exception {
        InputStream inStream = request.getInputStream();
        BufferedReader in = null;
        String result = "";
        in = new BufferedReader(
                new InputStreamReader(inStream));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        PayResult pr = (PayResult)XmlUtil.xml2Object(result, PayResult.class);
        return pr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByOrderNo(String out_trade_no) {
        //1.修改订单状态
        final ServiceOrderEntity order = baseMapper.selectOne(Wrappers.<ServiceOrderEntity>lambdaQuery().eq(ServiceOrderEntity::getOrderNo, out_trade_no));
        ServiceOrderEntity serviceOrder = new ServiceOrderEntity();
        serviceOrder.setOrderId(order.getOrderId());
        serviceOrder.setStatus(OrderConstant.PAY);
        serviceOrder.setPayTime(new Date());
        baseMapper.updateById(serviceOrder);
        //2.新增用户服务信息
        SysUserServiceEntity sysUserService = new SysUserServiceEntity();
        sysUserService.setCreateTime(new Date());
        if(order.getType().intValue() == 1){
            //月付
            sysUserService.setExpireTime(DateUtils.addDays(new Date(),OrderConstant.MONTH_DAY));
        }else {
            sysUserService.setExpireTime(DateUtils.addDays(new Date(),OrderConstant.QUARTER_DAY));
        }
        sysUserService.setUpdateTime(new Date());
        sysUserService.setOrderId(order.getOrderId());
        sysUserService.setServiceId(IdUtil.genId());
        sysUserService.setServiceType(order.getServiceType());
//        sysUserService.setServiceTypeName(order.getServiceTypeName());
        sysUserService.setStatus(OrderConstant.PAY);
        sysUserService.setUserId(order.getUserId());
        sysUserService.setType(order.getType());
        sysUserServiceDao.insert(sysUserService);
        //3.修改用户服务类型
        UserEntity user = new UserEntity();
        user.setUserId(order.getUserId());
        user.setUserType(UserType.PAYING_USER);
        userDao.updateById(user);

        //订单有效时长2个小时
        SRedisUtil.set(order.getOrderNo(),true, OrderConstant.EXPIRETIME_S);
    }

    @Override
    public Result signWechartOrder(PayModel payModel) throws Exception {
        //获取用户openID(JSAPI支付必须传openid)
        String openId = MobileUtil.getOpenId(wechatConfig.getAPPID(),wechatConfig.getKEY(),payModel.getCode());
        if("**************".equals(openId)){
            openId=(String)SRedisUtil.get(payModel.getCode());
        }
        // 生成预付单对象
        PreOrder o = new PreOrder();
        // 生成随机字符串
        String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");
        o.setAppid(wechatConfig.getAPPID());
        o.setBody(payModel.getBody());
        o.setMch_id(wechatConfig.getMCH_ID());
        o.setNotify_url(wechatConfig.getNOTIFY_URL());//回调接口
        o.setOut_trade_no(payModel.getOrderNo());
        // 判断有没有输入订单总金额，没有输入默认1分钱
        if (payModel.getFee() != null && !payModel.getFee().equals("")) {
            o.setTotal_fee(payModel.getFee().multiply(BigDecimal.valueOf(100)).intValue());
        } else {
            o.setTotal_fee(1);
        }
        o.setNonce_str(nonce_str);
        o.setTrade_type(wechatConfig.getTRADE_TYPE());// 交易类型H5支付 也可以是小程序支付参数
        o.setSpbill_create_ip(wechatConfig.getSPBILL_CREATE_IP());
        o.setOpenid(openId);
        SortedMap<Object, Object> p = o.beanToMap();

//        p.put("openid", openId);//用户openID

        // 获得签名
        String sign = Sign.createSign("utf-8", p, wechatConfig.getKEY());
        o.setSign(sign);
        // Object转换为XML
        String xml = XmlUtil.object2Xml(o, PreOrder.class);
        // 统一下单地址
        String url = wechatConfig.getPLACEANORDER_URL();
        // 调用微信统一下单地址
        String returnXml = HttpUtil.sendPost(url, xml);
        // XML转换为Object
        PreOrderResult preOrderResult = (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
        if ("SUCCESS".equalsIgnoreCase(preOrderResult.getResult_code())) {
            //获取预支付交易会话标识
            String prepay_id = preOrderResult.getPrepay_id();
            SortedMap<Object, Object> finalpackage = new TreeMap<Object, Object>();
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = o.getNonce_str();
            String packages = "prepay_id=" + prepay_id;
            finalpackage.put("appId",  o.getAppid());
            finalpackage.put("timeStamp", timestamp);
            finalpackage.put("nonceStr", nonceStr);
            finalpackage.put("package", packages);
            finalpackage.put("signType", "MD5");
            //这里很重要  参数一定要正确 狗日的腾讯 参数到这里就成大写了
            //可能报错信息(支付验证签名失败 get_brand_wcpay_request:fail)
            sign = Sign.createSign("utf-8", finalpackage, wechatConfig.getKEY());
            StringBuffer resultUrl = new StringBuffer();
            resultUrl.append("redirect:/weixinMobile/payPage?");
            resultUrl.append("timeStamp="+timestamp+"&nonceStr=" + nonceStr + "&package=" + packages);
            resultUrl.append("&signType=MD5" + "&paySign=" + sign+"&appid="+ o.getAppid());
            resultUrl.append("&orderNo="+o.getOut_trade_no()+"&totalFee="+o.getTotal_fee());
            Map<String,Object> map = new HashMap<>();
            map.put("url",resultUrl.toString());
            map.put("timeStamp",timestamp);
            map.put("nonceStr",nonceStr);
            map.put("package",packages);
            map.put("paySign",sign);
            map.put("appid",o.getAppid());
            map.put("totalFee",o.getTotal_fee());
            map.put("orderNo",o.getOut_trade_no());
            map.put("signType","MD5");
            //添加数据库和redis
            insertOrder(payModel, PayTypeConstant.WECHAT);
            return Result.ok(map);
        }
        return Result.error(preOrderResult.getErr_code_des());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAlipayOrder(PayModel payModel) throws Exception {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGATEWAYURL(), alipayConfig.getAPPID(),
                alipayConfig.getMERCHANTPRIVATEKEY(), "json", alipayConfig.getCHARSET(), alipayConfig.getALIPAYPUBLICKEY(),
                alipayConfig.getSIGNTYPE());

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getRETURNURL());
        alipayRequest.setNotifyUrl(alipayConfig.getNOTIFYURL());

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = payModel.getOrderNo();
        // 付款金额，必填
        String total_amount = payModel.getFee()+"";
        // 订单名称，必填
        String subject = payModel.getBody();
        // 商品描述，可空
        String body = "";

        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "2h";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
                + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\"," + "\"timeout_express\":\""
                + timeout_express + "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //添加数据库和redis
        insertOrder(payModel,PayTypeConstant.ALIPAY);

        AliPayOrderDTO resultData = new AliPayOrderDTO();
        resultData.setOrderNo(payModel.getOrderNo());
        resultData.setPayUrl(result);
        log.info("result:{}",result);


        return Result.ok(resultData);
    }

    private void insertOrder(PayModel payModel, int payType) {
        ServiceOrderEntity order = new ServiceOrderEntity();
        order.setFee(payModel.getFee());
        order.setCreateTime(new Date());
        order.setExpireTime(DateUtils.addHours(new Date(), OrderConstant.EXPIRETIME_H));
        order.setOrderId(IdUtil.genId());
        order.setOrderNo(payModel.getOrderNo());
        order.setPayType(payType);
        order.setServiceType(payModel.getServiceType());
        order.setStatus(OrderConstant.NO_PAY);//未支付
        order.setType(payModel.getType());
        order.setUserId(payModel.getUserId());
        if(baseMapper.insert(order) != 1){
            throw new ResultException("创建订单失败！");
        }
        //订单有效时长2个小时
        SRedisUtil.set(order.getOrderNo(),false, OrderConstant.EXPIRETIME_S);
    }

}
