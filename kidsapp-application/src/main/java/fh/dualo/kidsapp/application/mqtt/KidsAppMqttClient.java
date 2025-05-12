package fh.dualo.kidsapp.application.mqtt;

import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class KidsAppMqttClient implements MqttCallback{

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "kidsapp-client";
    private final MqttClient client;

    public KidsAppMqttClient() throws MqttException {
        client = new MqttClient(BROKER_URL, CLIENT_ID, null);
        client.setCallback(this);
        client.connect();
        client.subscribe("event/add");
        client.subscribe("event/update");
        System.out.println("Subscribed to event/add");
    }
    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        client.publish(topic, message);
        System.out.println("Published: " + payload);
    }
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
    }
    @Override
    public void connectionLost(Throwable throwable) {

    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken deliveryToken) {
    }



    @PreDestroy
    public void destroy(){
        try{
            client.disconnect();
            client.close();
        }catch (MqttException e){
            e.printStackTrace();
            System.out.println("Fehler beijm schließen der Connection!");
        }
    }
}
