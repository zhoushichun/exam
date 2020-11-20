package com.garm.modules.web.service;

import com.garm.common.utils.Result;
import com.garm.modules.web.dto.login.MessageDTO;
import com.garm.modules.web.model.MessageModel;

/**
 * 短信控制类
 * @author RKG
 */
public interface IMessageService {

    /**
     * 发送验证码
     * @param dto
     * @return
     */
    Result sendMessage(MessageDTO dto);

    /**
     * 校验
     * @param mesage
     * @return 0-成功 1-错误 2-无效
     */
    int checkCode(MessageModel mesage);
}
