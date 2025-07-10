package backend.stadt.mqtt;


import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.RegistrationService;
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
    private RegistrationService registrationService;

    @Autowired
    public MessageRouter(RegistrationService registrationService) {
        this.registrationService = registrationService;
        savedMessages = new ArrayList<>();
    }

    public void processMessage(String topic, String payload) {
        switch (topic) {
            case "offer/register":
                handleRegistration(payload);
                break;
            case "offer/status":
                handleStatusChange(payload);
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

            boolean success = registrationService.registerUserToOffer(userId, offerId);

            if (success) {
                System.out.printf("Registrierung erfolgreich (userId=%d, offerId=%d)%n", userId, offerId);
                // optional Rückmeldung senden
                sendMessage("offer/register/response", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"SUCCESS\"}", userId, offerId));
            } else {
                System.out.printf("Registrierung fehlgeschlagen (userId=%d, offerId=%d)%n", userId, offerId);
                sendMessage("offer/register/response", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"FAILED\"}", userId, offerId));
            }

        } catch (Exception e) {
            System.err.println("Fehler bei Registrierung über MQTT: " + e.getMessage());
        }
    }
    private void handleStatusChange(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(payload);

            Integer userId = json.get("userId").asInt();
            Integer offerId = json.get("offerId").asInt();
            RegistrationStatus status = RegistrationStatus.valueOf(json.get("status").asText());

            var success = registrationService.changeStatus(userId, offerId, status);

            if (success) {
                System.out.printf("Statusveränderung erfolgreich (userId=%d, offerId=%d)%n", userId, offerId);
                // optional Rückmeldung senden
                sendMessage("offer/status/response", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"SUCCESS\"}", userId, offerId));
            } else {
                System.out.printf("Statusveränderung fehlgeschlagen (userId=%d, offerId=%d)%n", userId, offerId);
                sendMessage("offer/status/response", String.format("{\"userId\":%d,\"offerId\":%d,\"status\":\"FAILED\"}", userId, offerId));
            }

        } catch (Exception e){
            System.err.println("Fehler bei StatusChange: " + e.getMessage());
        }
    }

    @AllArgsConstructor
    private  class SavedMessage {
        private MqttMessage message;
        private String topic;
    }

}
