package backend.stadt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class MqttTestClient {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "spring-mqtt-sender";

    @PostConstruct
    public void sendTestMessage() {
        try {
            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID, null);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            String topic = "test/add";
            String message = "Hallo von Spring MQTT Client!";

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);

            client.publish(topic, mqttMessage);
            System.out.println("✅ MQTT-Nachricht gesendet: " + message);

            client.disconnect();
            client.close();

        } catch (MqttException e) {
            e.printStackTrace();
            System.err.println("❌ Fehler beim Senden der MQTT-Nachricht: " + e.getMessage());
        }
    }
}
