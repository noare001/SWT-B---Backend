package backend.stadt.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageRouter {

    private MessageHandler messageHandler;
    private ObjectMapper mapper;

    @Setter
    private StadtMqttClient mqttClient;
    @Autowired
    public MessageRouter(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
        mapper = new ObjectMapper();
    }

    public void processMessage(String topic, String payload, StadtMqttClient client){
        try{
            switch (topic) {
                case "offer/add": {
                        client.publish("offer/received", mapper.writeValueAsString(messageHandler.addOffer(payload)));
                } break;
                default: System.out.println("Unknown topic: " + topic);
            }
        }catch (JsonProcessingException | MqttException e){
            System.err.println("Fehler im topic " + topic + " mit dem payload: " + payload);
            System.err.println(e.getMessage());
        }
    }

    public void sendMessage(String topic, String payload){
        if (mqttClient != null){ //-> D.h das wir verbunden sind
            try{
                mqttClient.publish(topic, payload);
            } catch (MqttException e) {
                System.err.println("Konnte die Nachricht nicht senden!");
                System.err.println(e.getMessage());
            }
        }
    }

}
