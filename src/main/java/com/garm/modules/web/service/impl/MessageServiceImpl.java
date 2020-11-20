package com.garm.modules.web.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.garm.common.exception.ResultException;
import com.garm.common.utils.JMessage;
import com.garm.common.utils.SRedisUtil;
import com.garm.common.utils.Result;
import com.garm.common.utils.message.MessageConstant;
import com.garm.common.utils.message.MessageManager;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.web.constants.MesasgeConstant;
import com.garm.modules.web.dao.UserDao;
import com.garm.modules.web.dto.login.MessageDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.model.MessageModel;
import com.garm.modules.web.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private UserDao userDao;

    private final String login_template = "您正在进行登录校验，验证码：code,请在10分钟内使用，切勿将验证码泄露于他人。【明博英语】";
    private final String regist_template = "您正在进行注册校验，验证码：code,请在10分钟内使用，切勿将验证码泄露于他人。【明博英语】";
    private final String update_template = "您正在进行修改账号校验，验证码：code,请在10分钟内使用，切勿将验证码泄露于他人。【明博英语】";
    private final String forget_template = "您正在进行找回密码校验，验证码：code,请在10分钟内使用，切勿将验证码泄露于他人。【明博英语】";
    private final String update_pwd_template = "您正在进行修改密码校验，验证码：code,请在10分钟内使用，切勿将验证码泄露于他人。【明博英语】";


    /**
     * 得到验证码
     * @return
     */
    public String getCode(){
        String code = (int)((Math.random()*9+1)*100000)+"";
        return code;
    }

    @Override
    public Result sendMessage(MessageDTO dto) {
        String code = getCode();
        JMessage message = new JMessage();
        message.setPhone(dto.getPhone());
        switch (dto.getType()){
            case MesasgeConstant.REGIST:
                //注册时，账号不能存在数据库
                if(checkUserInDB(dto.getPhone())){
                    return Result.error("账号已存在");
                }
                message.setContent(regist_template.replace("code",code));
                break;
            case MesasgeConstant.FORGET:
                //忘记密码时，账号必须存在数据库
                if(!checkUserInDB(dto.getPhone())){
                    return Result.error("账号不存在");
                }
                message.setContent(forget_template.replace("code",code));
                break;
             case MesasgeConstant.UPDATE_PWD:
                //修改密码时，账号必须存在数据库
                if(!checkUserInDB(dto.getPhone())){
                    return Result.error("账号不存在");
                }
                message.setContent(update_pwd_template.replace("code",code));
                break;
            case MesasgeConstant.UPDATE:
                //修改为新账号时，新账号不能存在数据库
                if(checkUserInDB(dto.getPhone())){
                    return Result.error("账号已存在");
                }
                message.setContent(update_template.replace("code",code));
                break;
            default:
               return Result.error("请填写短信类型");
        }
        MessageModel model = new MessageModel();
        model.setPhone(dto.getPhone());
        model.setCode(code);
        model.setType(dto.getType());
        //验证码保留10分钟
        SRedisUtil.set(model.getType()+"_"+ model.getPhone(), model, 600L);
        System.out.println(SRedisUtil.get(model.getType()+"_"+ model.getPhone()));;
        if(MessageConstant.isSend){
            sendProdMessage(message);
        }else{
            return Result.ok(message.getContent());
        }
        return Result.ok();
    }

    @Override
    public int checkCode(MessageModel model) {
        //0-成功 1-错误 2-无效
        Object obj = SRedisUtil.get(model.getType()+"_"+ model.getPhone());
        if(obj != null){
            MessageModel modelR = (MessageModel)obj;
            if(modelR.getCode().equalsIgnoreCase(model.getCode())){
                return 0;
            }
            return 1;
        }
        return 2;
    }

    public Boolean checkUserInDB(String phone){
        final UserEntity user = userDao
                .selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, phone).eq(UserEntity::getIsDel, DeleteConstant.NOT_DELETE));
        if(user == null){
            return false;
        }
        return true;
    }

    private void sendProdMessage(JMessage message){
        //短信发送具体方法体

            try {
                MessageManager manager = new MessageManager();
                JSONObject obj= manager.getMessageProvider().sendSMS(message);
                if(obj.getInteger("code") != 0){
                    log.error("短信发送失败：电话号码："+message.getPhone()+";错误原因："+obj.getString("msg"));
                    throw new ResultException(obj.getString("msg"));
                }
            } catch (Exception e) {
                log.error("短信发送失败：电话号码："+message.getPhone()+";错误原因："+e.getMessage());
                throw new ResultException(e.getMessage());
            }


    }
}
