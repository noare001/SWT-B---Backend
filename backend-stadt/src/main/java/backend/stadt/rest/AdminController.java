package backend.stadt.rest;

import backend.stadt.mqtt.StadtMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final StadtMqttClient mqttClient;

    @Autowired
    public AdminController(StadtMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
            "mqttConnected", mqttClient.isConnected(),
            "timestamp", Instant.now().toString()
        );
    }

    @PostMapping("/sendTestMessage")
    public ResponseEntity<String> sendTestMessage(@RequestParam String topic, @RequestParam String payload) {
        try {
            mqttClient.publish(topic, payload);
            return ResponseEntity.ok("Message sent.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}