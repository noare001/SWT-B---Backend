package backend.stadt.mqtt;

import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class StadtMqttClient implements MqttCallback {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "stadt-client";
    private final MqttClient client;

    public StadtMqttClient() throws MqttException {
        client = new MqttClient(BROKER_URL, CLIENT_ID, null);
        client.setCallback(this);
        client.connect();
        client.subscribe("event/add");
        client.subscribe("event/update");
        client.subscribe("loadData");
        System.out.println("Subscribed to event/add");
    }

    public void publish(String topic, String payload) throws MqttException {
        client.publish(topic, new MqttMessage(payload.getBytes()));
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        switch (topic) {
            case "loadData": publish("fillCache", getData()); break;
            default: System.out.println("Unknown topic: " + topic);
        }
        System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
    }

    public String getData() {
        //Muss noch implementiert werden
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
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Fehler beijm schließen der Connection!");
        }
    }
}
