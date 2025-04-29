package fh.duao.MqttBroker;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class MqttBrokerApplication {

    private Server mqttBroker;

    @PostConstruct
    public void startBroker() {
        mqttBroker = new Server();
        Properties brokerProperties = new Properties();
        brokerProperties.setProperty("port", "1883");
        brokerProperties.setProperty("host", "0.0.0.0");
        brokerProperties.setProperty("persistent_store", "moquette_store.mapdb");

        try {
            mqttBroker.startServer(new MemoryConfig(brokerProperties));
            System.out.println("MQTT Broker started successfully!");
        } catch (IOException e) {
            System.err.println("Failed to start MQTT Broker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopBroker() {
        if (mqttBroker != null) {
            System.out.println("Stopping MQTT Broker...");
            mqttBroker.stopServer();
        }
    }
}
