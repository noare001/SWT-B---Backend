package fh.dualo.kidsapp.application.mqtt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KidsAppMqttClient implements MqttCallback{

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "kidsapp-client";
    private List<SavedMessage> savedMessages;
    private MqttClient client;
    private MessageRouter messageRouter;

    public KidsAppMqttClient(@Lazy MessageRouter messageRouter) throws MqttException {
        this.messageRouter = messageRouter;
    }

    @PostConstruct
    public void setup() throws MqttException {
        savedMessages = new ArrayList<>();
        new Thread(this::connectWithRetry).start();
    }

    private void connectWithRetry() {
        boolean connected = false;
        while (!connected) {
            try {
                client = new MqttClient(BROKER_URL, CLIENT_ID, null);
                client.setCallback(this);
                client.connect();
                client.subscribe("event/add");
                client.subscribe("event/update");
                client.subscribe("cache/data");
                System.out.println("\u001B[32mVerbunden!\u001B[0m");
                sendSavedMessages();
                connected = true;
            } catch (MqttException e) {
                System.err.println("Verbindung fehlgeschlagen: " + e.getMessage());
                try {
                    Thread.sleep(20_000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("Verbindungsversuch abgebrochen.");
                    break;
                }
            }
        }
    }

    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        if(client.isConnected()){
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

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        messageRouter.processMessage(topic, new String(message.getPayload()));
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
        try {
            if(client.isConnected()){
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            System.err.println("Fehler beijm schließen der Connection!");
        }
    }

    @AllArgsConstructor
    private  class SavedMessage {
        private MqttMessage message;
        private String topic;
    }
}
