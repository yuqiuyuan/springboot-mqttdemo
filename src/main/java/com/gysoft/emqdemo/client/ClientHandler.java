package com.gysoft.emqdemo.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 魏文思
 * @date 2019/11/15$ 17:43$
 */
@Component
public class ClientHandler {

    @Value("${com.mqtt.topic}")
    public String topic;
    @Value("${com.mqtt.host}")
    public String broker;
    @Value("${com.mqtt.clientid}")
    public String clientId;
    @Value("${com.mqtt.qos}")
    public int qos;


    @PostConstruct
    public void init() {
        String topic = "demo/topics";
        String content = "Message from MqttPublishSample";
        int qos = 1;
        String broker = "tcp://10.200.0.91:1883";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            System.out.println("clientId:" + clientId);
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.subscribe("demo/topics", 0);
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(mqttMessage);
                    System.out.println();
                    System.out.println(s);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
          /*  System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);*/
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
