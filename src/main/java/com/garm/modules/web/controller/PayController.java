package com.garm.modules.web.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.Result;
import com.garm.common.utils.SRedisUtil;
import com.garm.common.utils.wechat.PayResult;
import com.garm.common.validator.ValidatorUtils;
import com.garm.config.AlipayConfig;
import com.garm.config.WechatConfig;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.service.ServiceOrderService;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.service.SysUserService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.pay.WPayServiceDTO;
import com.garm.modules.web.dto.pay.WwxPayServiceDTO;
import com.garm.modules.web.model.PayModel;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 支付类
 * @author RKG
 */
@RestController
@Slf4j
@Api(tags = "前台-支付相关")
public class PayController extends AbstractController {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private ServiceOrderService serviceOrderService;

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 微信扫码支付---二维码获取
     * @param dto
     * @return
     * @throws Exception
     */
    @Login
    @PostMapping(value = "api/web/pay/wechat/createPreOrder")
    @ApiOperation("微信扫码支付")
    public Result createPreOrder(@RequestBody WwxPayServiceDTO dto, @LoginUser @ApiIgnore UserDTO user) throws Exception {
        //表单校验
        ValidatorUtils.validateEntity(dto);

        PayModel payModel = new PayModel();
        // 商户订单号
        String out_trade_no =String.valueOf(System.currentTimeMillis());

        String serviceTypeName = getItems().stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().get().getItemName();
        if(dto.getType().intValue() == 1){

            //月付
            payModel.setBody(serviceTypeName+"-月付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("M")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("M"+out_trade_no);
        }else if(dto.getType().intValue() == 2){
            // 商品描述
            payModel.setBody(serviceTypeName+"-学期付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("S")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("S"+out_trade_no);
        }else{
            return Result.error("请选择正确的购买类型");
        }
        payModel.setUserId(user.getUserId());
        payModel.setServiceType(dto.getServiceType());
        payModel.setServiceTypeName(serviceTypeName);
        payModel.setType(dto.getType());
        // 统一下单
        return serviceOrderService.addWechartOrder(payModel);
    }


    /**
     * 微信扫码支付----循环验证是否支付
     * @Description: 扫码完毕后每隔3秒检查是否支付成功
     * @return
     * @throws Exception
     *
     */
    @Login
    @PostMapping(value = "api/web/pay/wechat/wxPayIsSuccess/{orderNo}")
    @ApiOperation("微信扫码支付----循环验证是否支付")
    @ApiImplicitParam(name="orderNo",value="订单ID",required=true)
    public Result wxPayIsSuccess(@PathVariable String orderNo) throws Exception {
        // TODO 查看订单是否支付成功，成功返回true，失败返回false
        Object isOrderPaid = SRedisUtil.get(orderNo);
        if(isOrderPaid == null){
            return Result.error("订单已失效");
        }else{
            return Result.ok(isOrderPaid);
        }
    }

    /**
     * 微信扫码支付--------支付回调
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "api/web/pay/wechat/notify")
    public void Mynotify( @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
        if (request==null||response==null) {
            System.out.println("请求出错");
        }
        PayResult payResult = serviceOrderService.getWxPayResult(request);
        boolean isPaid = payResult.getReturn_code().equals("SUCCESS") ? true : false;
        // 查询该笔订单在微信那边是否成功支付
        // 支付成功，商户处理后同步返回给微信参数
        PrintWriter writer = response.getWriter();
        if (isPaid) {
            System.out.println("================================= 支付成功 =================================");

            // ====================== 操作商户自己的业务，比如修改订单状态，生成支付流水等 start ==========================
            serviceOrderService.updateByOrderNo(payResult.getOut_trade_no());
            // ============================================ 业务结束， end ==================================
            // 通知微信已经收到消息，不要再给我发消息了，否则微信会8连击调用本接口
            String noticeStr = setXML("SUCCESS", "");
            writer.write(noticeStr);
            writer.flush();

        } else {
            System.out.println("================================= 支付失败 =================================");

            // 支付失败
            String noticeStr = setXML("FAIL", "");
            writer.write(noticeStr);
            writer.flush();
        }

    }

    /**
     * 微信扫码支付--------回调成功通知微信
     * @param return_code
     * @param return_msg
     * @return
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }


    /**
     * 微信H5支付新增
     * @param dto
     * @return
     * @throws Exception
     */
    @Login
    @ApiOperation("微信H5支付")
    @PostMapping(value="api/web/pay/wechat/h5pay")
    public Result dopay(@LoginUser @ApiIgnore UserDTO user,@RequestBody WPayServiceDTO dto) throws Exception {
        ValidatorUtils.validateEntity(dto);
        if(StringUtils.isBlank(dto.getCode())){
            return Result.error("H5支付必须传入code值");
        }
        PayModel payModel = new PayModel();

        // 商户订单号
        String out_trade_no =String.valueOf(System.currentTimeMillis());

        String serviceTypeName = getItems().stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().get().getItemName();

        if(dto.getType().intValue() == 1){
            //月付
            payModel.setBody(serviceTypeName+"-月付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("M")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("M"+out_trade_no);
        }else if(dto.getType().intValue() == 2){
            // 商品描述
            payModel.setBody(serviceTypeName+"-学期付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("S")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("S"+out_trade_no);
        }else{
            return Result.error("请选择正确的购买类型");
        }
        payModel.setUserId(user.getUserId());
        payModel.setServiceType(dto.getServiceType());
        payModel.setType(dto.getType());
//        if(dto.getCode() != null) {
//            payModel.setCode(dto.getCode());
//        }else {
//            payModel.setCode(request.getParameter("code"));
//        }
        log.info("前端传入的 code："+dto.getCode());
        payModel.setCode(dto.getCode());
        log.info("数据的 code："+payModel.getCode());
        // 统一下单
        return serviceOrderService.signWechartOrder(payModel);
    }

    /**
     * 支付宝扫码支付-----生成二维码链接
     * @param dto
     * @return
     * @throws Exception
     */
    @Login
    @ApiOperation("支付宝扫码支付-----生成二维码链接")
    @PostMapping(value = "api/web/pay/alipay/createPreOrder")
    public Result goAlipay(@LoginUser @ApiIgnore UserDTO user,@RequestBody WPayServiceDTO dto) throws Exception {
        ValidatorUtils.validateEntity(dto);
        PayModel payModel = new PayModel();
        // 商户订单号
        String out_trade_no =String.valueOf(System.currentTimeMillis());

        String serviceTypeName = getItems().stream().filter(m->m.getDictItemId().equals(dto.getServiceType())).findFirst().get().getItemName();

        if(dto.getType().intValue() == 1){
            //月付
            payModel.setBody(serviceTypeName+"-月付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("M")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
//                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("M"+out_trade_no);
        }else if(dto.getType().intValue() == 2){
            // 商品描述
            payModel.setBody(serviceTypeName+"-学期付");
            String value = getItems().stream().filter(m->m.getRemarks().equals("S")).findFirst().get().getValue();
            if(StringUtils.isBlank(value)){
//                return Result.error("未设置金额");
            }
            payModel.setFee(new BigDecimal(value));
            payModel.setOrderNo("S"+out_trade_no);
        }else{
//            return Result.error("请选择正确的购买类型");
        }
        payModel.setUserId(user.getUserId());
        payModel.setServiceType(dto.getServiceType());
//        payModel.setServiceTypeName(dto.getServiceTypeName());
        payModel.setType(dto.getType());
        // 统一下单
        return serviceOrderService.addAlipayOrder(payModel);
    }

    /**
     * 支付宝同步通知页面
     * @Title: AlipayController.java
     * @Description: 支付宝同步通知页面
     */
    @RequestMapping(value = "api/web/pay/alipay/returnNotice")
    public void alipayReturnNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

        log.info("支付成功, 进入同步通知接口...");

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getALIPAYPUBLICKEY(), alipayConfig.getCHARSET(),
                alipayConfig.getSIGNTYPE()); // 调用SDK验证签名

        // ——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            serviceOrderService.updateByOrderNo(out_trade_no);

            log.info("********************** 支付成功(支付宝同步通知) **********************");
            log.info("* 订单号: {}", out_trade_no);
            log.info("* 支付宝交易号: {}", trade_no);
            log.info("* 实付金额: {}", total_amount);
            log.info("***************************************************************");

        } else {
            log.info("支付, 验签失败...");
        }
    }


    /**
     * 支付宝异步 通知页面
     * @Title: AlipayController.java
     * @Description: 支付宝异步 通知页面
     */
    @RequestMapping(value = "api/web/pay/alipay/notifyNotice")
    public void alipayNotifyNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

        log.info("支付成功, 进入异步通知接口...");

        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getALIPAYPUBLICKEY(), alipayConfig.getCHARSET(),
                alipayConfig.getSIGNTYPE()); // 调用SDK验证签名

        // ——请在这里编写您的程序（以下代码仅作参考）——

        /*
         * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
         * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）， 3、校验通知中的seller_id（或者seller_email)
         * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
         * 4、验证app_id是否为该商户本身。
         */
        if (signVerified) {// 验证成功
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                // 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意：
                // 付款完成后，支付宝系统发送该交易状态通知

                // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
                serviceOrderService.updateByOrderNo(out_trade_no);

                log.info("********************** 支付成功(支付宝异步通知) **********************");
                log.info("* 订单号: {}", out_trade_no);
                log.info("* 支付宝交易号: {}", trade_no);
                log.info("* 实付金额: {}", total_amount);
                log.info("***************************************************************");
            }
            log.info("支付成功...");

        } else {// 验证失败
            log.info("支付, 验签失败...");
        }
    }

    @PostMapping("api/web/pay/wechat/getConfig")
    public Result getConfig(){
        Map<String,Object> map = new HashMap<>();
        map.put("appid",wechatConfig.getAPPID());
        return Result.ok(map);
    }

}
