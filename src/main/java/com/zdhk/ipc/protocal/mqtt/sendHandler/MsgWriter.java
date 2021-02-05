    package com.zdhk.ipc.protocal.mqtt.sendHandler;

    import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

    //@Component
    //@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MsgWriter {
        void sendToMqtt(String data);
        void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
        void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
    }
