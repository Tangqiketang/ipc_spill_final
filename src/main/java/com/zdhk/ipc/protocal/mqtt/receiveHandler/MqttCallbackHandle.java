package com.zdhk.ipc.protocal.mqtt.receiveHandler;

import com.alibaba.fastjson.JSON;
import com.zdhk.ipc.protocal.mqtt.sendHandler.MsgWriter;
import com.zdhk.ipc.protocal.mqtt.topic.TopicDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述:
 * 处理消息具体类
 *
 * @auther WangMin
 * @create 2020-07-30 8:58
 */
@Slf4j
//@Service
public class MqttCallbackHandle {


    //service

    @Autowired
    private MsgWriter msgWriter;

    public void handle(String topic, String payload){
        log.info("MqttCallbackHandle:" + topic + "---"+ payload);
        if (topic.equalsIgnoreCase(TopicDevice.WILL_OFFLINE_TOPIC)){
            log.info("设备离线");
            // 业务逻辑
            return ;
        }

       /* NettyRsp rsp = dispatcherService.handleByReceiveMsgType(payload,null);
        try{
            String sendTopic = new StringBuilder().append(TopicDevice.SEND_TO_DEVICE).append(rsp.getAddr()).toString();
            String result = JSON.toJSONString(rsp);
            msgWriter.sendToMqtt(sendTopic,1,result);
        } catch (Exception e){
            log.error("mqtt解析topic失败，获取addr失败,payload:"+payload);
        }*/


    }
}
