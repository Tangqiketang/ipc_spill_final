package com.zdhk.ipc.protocal.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

//@Component
@Slf4j
public class KafkaConsumer {


    /**
     * 同步设备端配置到平台及手机端
     * @param record
     */
    @KafkaListener(topics = "DEVICE_CONFIG_UPDATE")
    public void syncDeviceConfig(ConsumerRecord<String,Object> record){
        log.info("kafka收到配置同步信息record:{}",record.value());
    }


    /**
     * test自己发送自己接收
     * @param record
     */
    @KafkaListener(topics = "I2V_USER_INFO_UPDATE")
    public void receiveUserSync(ConsumerRecord<String,Object> record){
        log.info("自发自收测试,收到消息：recode:"+record.value());

    }

}
