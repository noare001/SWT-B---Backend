package fh.dualo.kidsapp.application.cache;

import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import fh.dualo.kidsapp.application.user.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class KidsAppDataService extends KidsAppData {

    private KidsAppMqttClient mqttSubscriber;

    private WebClient webClient;

    public KidsAppDataService(KidsAppMqttClient mqttSubscriber) {
        this.mqttSubscriber = mqttSubscriber;
    }

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl("http://localhost:8082/api/stadt/cache").build();
    }

    @Override
    public String getOffer(String key) {
        //ToDO Soll direkt eine Anfrage An die Stadt senden um die passenden offer zu finden. Implementieren wir wenn wir zeit dazu haben
        return null;
    }
}
