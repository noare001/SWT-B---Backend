package fh.dualo.kidsapp.application.mqtt;

import fh.dualo.kidsapp.application.cache.KidsAppCache;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageRouter {

    private KidsAppCache cache;
    @Setter
    private MqttClient client;
    private List<SavedMessage> savedMessages;
    @Autowired
    public MessageRouter(KidsAppCache cache){
        this.cache = cache;
        savedMessages = new ArrayList<>();
    }

    public void processMessage(String topic, String payload){
        switch (topic){
            case "stadt/online": cache.fillCache();break;
            case "offer/processed": cache.update(payload);break;
        }
    }


    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        if(client != null && client.isConnected()){
            client.publish(topic, message);
            System.out.println("Published: " + payload);
        }else{
            savedMessages.add(new SavedMessage(message, topic));
            System.out.println("Saved Message for " + topic);
        }
    }

    private void sendSavedMessages(){
        if (client.isConnected()){
            System.out.println("Sending Saved Messages");
            savedMessages.forEach(m -> {
                try {
                    client.publish(m.topic,m.message);
                } catch (MqttException e) {
                    System.err.println("Error while sending saved Messages: " + e.getMessage());
                }
            });
        }
    }

    public void setMqttClient(MqttClient client){
        if(client != null){
            this.client = client;
            sendSavedMessages();
        }
    }

    @AllArgsConstructor
    private  class SavedMessage {
        private MqttMessage message;
        private String topic;
    }
}
