package fh.dualo.kidsapp.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fh.dualo.kidsapp.application.cache.KidsAppCache;
import fh.dualo.kidsapp.application.model.AnmeldeDTO;
import fh.dualo.kidsapp.application.mqtt.MessageRouter;
import fh.dualo.kidsapp.application.services.KidsAppUserService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class KidsAppController {

    private KidsAppCache appCache;
    private MessageRouter router;
    private KidsAppUserService userService;

    @Autowired
    public KidsAppController(KidsAppUserService userService, KidsAppCache appCache, MessageRouter router) {
        this.appCache = appCache;
        this.router = router;
        this.userService = userService;
    }

    /**
     * Nur zum testen. Abfragen des gesamten caches
     * localhost:8090/api/cache
     * @return cache data
     */
    @GetMapping
    @RequestMapping("/cache")
    public ResponseEntity<Map<String, JsonNode>> searchOffer() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer());
    }
    /**
     * Bsp: localhost:8090/api/login?name=lukes&password=lukes
     */
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

    /**
     * Bsp: localhost:8090/api/register?name=lukes&password=lukes&email=lukesmail@mail.de
     */
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

    /**
     * Bsp: localhost:8090/api/anmelden
     */
    @PostMapping
    @RequestMapping("/anmelden")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AnmeldeDTO anmeldeDaten) {
        //ToDo JWT verifizieren und Benutzer ID rauslesen. Prozess für die Anmeldung starten. ID des neuen Anmelde-Eintrags zurückgeben
        Map<String, Object> claims = userService.getJwtUtil().getClaims(anmeldeDaten.getJwt());
        if(claims.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(claims);
    }

    /**
     * Returnt alle Daten zu dem gesuchten Offer
     * Bsp: localhost:8090/api/offer?id=23455
     */
    @GetMapping("/offer")
    public ResponseEntity<Map<String, JsonNode>> getOffer() {
        //ToDo ja nh
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer());
    }
    /**
     * Hinzufügen vcn einem Offer
     * Bsp: localhost:8090/api/offer?jwt={jwt}
     */
    @PostMapping("/offer")
    public ResponseEntity<String> addOffer(@RequestParam("jwt") String jwt, @RequestBody JsonNode jsonNode) throws MqttException {
        Map<String,Object> claims = userService.getJwtUtil().getClaims(jwt);
        if (!"AUTHOR".equals(claims.get("role"))) {
            ResponseEntity.badRequest().build();
        }
        try{
            return ResponseEntity.ok(appCache.addOffer(jsonNode, (Integer) claims.get("id")));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }
}


