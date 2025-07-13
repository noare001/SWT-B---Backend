package backend.stadt.mqtt;


import backend.stadt.enums.RegistrationStatus;
import backend.stadt.modells.OfferRegistration;
import backend.stadt.repositorys.DatabaseService;
import com.fasterxml.jackson.databind.JsonNode;
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


    @Setter
    private MqttClient client;
    private List<SavedMessage> savedMessages;
    private DatabaseService databaseService;

    @Autowired
    public MessageRouter(DatabaseService databaseService) {
        this.databaseService = databaseService;
        savedMessages = new ArrayList<>();
    }

    public void processMessage(String topic, String payload) {
        switch (topic) {
            case "offer/register":
                handleRegistration(payload);
                break;
            case "registration/delete":
                deleteRegistration(payload);
                break;
            default:
                System.out.println("Unknown topic: " + topic);
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

    private void handleRegistration(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(payload);

            Integer userId = json.get("userId").asInt();
            Integer offerId = json.get("offerId").asInt();

            OfferRegistration registration = databaseService.registerUserToOffer(userId, offerId);

            if (registration != null) {
                System.out.printf("Registrierung erfolgreich (userId=%d, offerId=%d)%n", userId, offerId);
                sendMessage("registration/processed", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"%s\"}", userId, offerId, registration.getStatus().name()));
            } else {
                System.out.printf("Registrierung fehlgeschlagen (userId=%d, offerId=%d)%n", userId, offerId);
                sendMessage("registration/processed", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"%s\"}", userId, offerId,RegistrationStatus.DECLINED.name()));
            }

        } catch (Exception e) {
            System.err.println("Fehler bei Registrierung über MQTT: " + e.getMessage());
        }
    }
    private void deleteRegistration(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(payload);

            int userId = json.get("userId").asInt();
            int offerId = json.get("offerId").asInt();

            databaseService.deleteRegistration(userId,offerId);

        } catch (Exception e){
            System.err.println("Fehler beim Löschen: " + e.getMessage());
        }
    }

    @AllArgsConstructor
    private  class SavedMessage {
        private MqttMessage message;
        private String topic;
    }

}
