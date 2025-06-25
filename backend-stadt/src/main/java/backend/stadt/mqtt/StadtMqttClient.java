package backend.stadt.mqtt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class StadtMqttClient implements MqttCallback {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "stadt-client";
    private MqttClient client;

    public StadtMqttClient() {

    }

    @PostConstruct
    public void start(){
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
                client.subscribe("cache/request");
                System.out.println("\u001B[32mVerbunden!\u001B[0m");
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

    public void publish(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setRetained(true);
        client.publish(topic, message);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        switch (topic) {
            case "cache/request": publish("cache/data", getData()); break;
            default: System.out.println("Unknown topic: " + topic);
        }
        System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
    }

    public String getData() {
        //ToDo um dass Chache zu fillen. Hier müssen alle Offer Daten als JSON-String übertragen werden
        return "Luke, i am your data";
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @PreDestroy
    public void destroy() {
        try {
            if(client.isConnected()){
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Fehler beijm schließen der Connection!");
        }
    }

    public boolean isConnected(){
        if(client != null && client.isConnected()){
            return true;
        }
        return false;
    }
}
