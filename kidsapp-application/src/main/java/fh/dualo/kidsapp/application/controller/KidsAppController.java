package fh.dualo.kidsapp.application.controller;

import fh.dualo.kidsapp.application.cache.KidsAppDataCache;
import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KidsAppController {

    private KidsAppDataCache appCache;
    private KidsAppMqttClient mqttClient;
    private WebClient webClient;

    @Autowired
    public KidsAppController(KidsAppDataCache appCache, KidsAppMqttClient mqttClient) {
        this.appCache = appCache;
        this.mqttClient = mqttClient;
        webClient = WebClient.create();
    }

    @GetMapping
    public String searchOffeqr(@RequestParam String searchString) {
        return appCache.getOffer(searchString);
    }

    @GetMapping
    @RequestMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password){
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("password", password);

        return webClient.post()
                .uri("http://localhost:8082/api/stadt")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // block() für synchronen Aufruf – in REST-Controllern üblich

    }
}


