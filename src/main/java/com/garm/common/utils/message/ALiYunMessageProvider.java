package com.garm.common.utils.message;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.garm.common.utils.JMessage;


/**
 * 阿里云短信发送
 * @className: ALiYunMessageProvider
 * @description:TODO
 * @author: ruankegang
 * @date: 2019年12月27日 下午4:57:01
 */
public class ALiYunMessageProvider extends AbstractMessageProvider {

	@Override
	public JSONObject sendSMS(JMessage message) {
        JSONObject obj = new JSONObject();
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", MessageConstant.id, MessageConstant.pwd);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", message.getPhone());
        request.putQueryParameter("SignName", message.getSignName());
        request.putQueryParameter("TemplateCode", message.getTemplateCode());
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(message.getParams()));

        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject object = JSONObject.parseObject(response.getData());
            if(!"OK".equals(object.get("Code"))){
                obj.put("code",-1);
                obj.put("msg",object.get("message"));
            }else {
                obj.put("code",0);
                obj.put("msg","发送成功");
            }
        } catch (ClientException e) {
            obj.put("code",-1);
            obj.put("msg","阿里云客户端发生异常");
        }
        return obj;
	}
}
