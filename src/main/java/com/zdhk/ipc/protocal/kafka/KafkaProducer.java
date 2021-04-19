package com.zdhk.ipc.protocal.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

//@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate  kafkaTemplate;





    private void sendMessage(String topic, Object object){
        ListenableFuture<SendResult<String,Object>> future = kafkaTemplate.send(topic,object);
        //log.info("发送消息topic:{},content:{}",topic, object.toString());

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("发送消息失败topic:{},content:{}",topic, object.toString());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("发送消息成功topic:{},content:{}",topic, object.toString());
            }
        });

    }

}
