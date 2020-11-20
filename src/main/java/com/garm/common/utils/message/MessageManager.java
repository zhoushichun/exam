package com.garm.common.utils.message;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author EDZ
 */
public class MessageManager {
	private MessageProvider messageProvider;
    private static Logger logger = LoggerFactory.getLogger(MessageManager.class);

    private static String lkProvider="com.garm.common.utils.message.LKMessageProvider";
    private static String aliyunProvider="com.garm.common.utils.message.ALiYunMessageProvider";


    public MessageProvider getMessageProvider() {
        try {
            if("lk".equalsIgnoreCase(MessageConstant.provider)){
                messageProvider = (MessageProvider) Class.forName(lkProvider).newInstance();
            }else if("aliyun".equalsIgnoreCase(MessageConstant.provider)){
                messageProvider = (MessageProvider) Class.forName(aliyunProvider).newInstance();
            }else{
                logger.error("不存在短信类型");
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return messageProvider;
    }
}
