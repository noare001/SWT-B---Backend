package backend.stadt.mqtt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class StadtMqttClient implements MqttCallbackExtended {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "stadt-client";
    private MqttClient client;
    private MessageRouter router;

    public StadtMqttClient(MessageRouter router) {
        this.router = router;
    }

    @PostConstruct
    public void init(){
        new Thread(this::connectWithRetry).start();
    }

    private void connectWithRetry() {
        boolean connected = false;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setKeepAliveInterval(20);
        while (!connected) {
            try {
                client = new MqttClient(BROKER_URL, CLIENT_ID, null);

                client.setCallback(this);
                client.connect(options);
                client.subscribe("offer/add");
                client.subscribe("offer/update");
                client.subscribe("cache/request");
                client.subscribe("offer/register");
                client.subscribe("offer/status");
                connected = true;
                System.out.println("\u001B[32mVerbunden!\u001B[0m");
                router.setMqttClient(client);
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

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        router.processMessage(topic, new String(message.getPayload()));
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.err.println("CONNECTION LOST");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}

    @PreDestroy
    public void destroy() {
        try {
            if(client.isConnected()){
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Fehler beim schließen der Connection!");
        }
    }
    @Override
    public void connectComplete(boolean b, String s) {
        try {
            MqttMessage message = new MqttMessage("".getBytes());
            message.setRetained(true);
            client.publish("stadt/online", message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
