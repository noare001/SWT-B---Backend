package fh.duao.MqttBroker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationServer {

    public static void main(String[] args) {
        SpringApplication.run(MqttBrokerApplication.class, args);
    }
}
