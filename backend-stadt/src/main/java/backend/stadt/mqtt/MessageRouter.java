package backend.stadt.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private MessageHandler messageHandler;

    @Setter
    private MqttClient client;
    private List<SavedMessage> savedMessages;

    @Autowired
    public MessageRouter(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
        savedMessages = new ArrayList<>();
    }

    public void processMessage(String topic, String payload, StadtMqttClient client){
            switch (topic) {
                default: System.out.println("Unknown topic: " + topic);
            }
    }

    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        if(isConnected()){
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

    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    @AllArgsConstructor
    private  class SavedMessage {
        private MqttMessage message;
        private String topic;
    }

}
