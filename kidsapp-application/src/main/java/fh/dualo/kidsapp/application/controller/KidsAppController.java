package fh.dualo.kidsapp.application.controller;

import fh.dualo.kidsapp.application.cache.KidsAppCache;
import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import fh.dualo.kidsapp.application.user.KidsAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class KidsAppController {

    private KidsAppCache appCache;
    private KidsAppMqttClient mqttClient;
    private KidsAppUserService userService;

    @Autowired
    public KidsAppController(KidsAppUserService userService, KidsAppCache appCache, KidsAppMqttClient mqttClient) {
        this.appCache = appCache;
        this.mqttClient = mqttClient;
        this.userService = userService;
    }

    @GetMapping
    public String searchOffeqr(@RequestParam String searchString) {
        return appCache.getOffer(searchString);
    }

    @GetMapping
    @RequestMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam("name") String name, @RequestParam("password") String password) {

        // block() erhält entweder eine Map oder null (bei Mono.empty())
        Map<String, Object> result = userService.login(password, name);

        if (result == null) {
            // 404 ohne Body
            return ResponseEntity.notFound().build();
        }
        // 200 OK mit Body
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @RequestMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("email") String email) {
        System.out.println("REGISTER IN STADT");
        // block() erhält entweder eine Map oder null (bei Mono.empty())
        Map<String, Object> result = userService.register(password, name,email);

        if (result == null) {
            // 404 ohne Body
            return ResponseEntity.notFound().build();
        }
        // 200 OK mit Body
        return ResponseEntity.ok(result);
    }
}


