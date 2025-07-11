package fh.dualo.kidsapp.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import fh.dualo.kidsapp.application.cache.KidsAppCache;
import fh.dualo.kidsapp.application.enums.RegistrationStatus;
import fh.dualo.kidsapp.application.mqtt.MessageRouter;
import fh.dualo.kidsapp.application.user.KidsAppUserService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * Returnt alle Offer
     * Bsp: localhost:8090/api/offer
     */
    @GetMapping("/offer")
    public ResponseEntity<Collection<JsonNode>> getOffer() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer());
    }
    /**
     * Returnt den Offer mit der Id
     * Bsp: localhost:8090/api/offer/{id}
     */
    @GetMapping("/offer/{id}")
    public ResponseEntity<JsonNode> getSingleOffer(@PathVariable("id") int id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer(id));
    }
    /**
     * Deleted den Offer mit der Id
     * Bsp: localhost:8090/api/offer/{id}?jwt={jwt}
     */
    @DeleteMapping("/offer/{id}")
    public Mono<ResponseEntity<Object>> deleteOffer(@PathVariable("id") int id,
                                                    @RequestParam("jwt") String jwt) {
        Map<String,Object> claims = userService.getJwtUtil().getClaims(jwt);
        String providerId = String.valueOf(claims.get("providerId"));
        String userId = String.valueOf(claims.get("id"));

        return WebClient.builder()
                .baseUrl("http://localhost:8082/api/stadt")
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/offer")
                        .queryParam("providerId", providerId)
                        .queryParam("userId", userId)
                        .queryParam("offerId", id)
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        appCache.delete(id + "-" + providerId); // das darf synchron sein
                        return Mono.just(ResponseEntity.ok().build());
                    } else {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                });
    }
    /**
     * Returnt den Offer des Providers zu dem der Autor gehört
     * Bsp: localhost:8090/api/author/offer?jwt=...
     */
    @GetMapping("/author/offer")
    public ResponseEntity<List<JsonNode>> getOfferFromAuthor(@RequestParam("jwt") String jwt) {
        try{
            Map<String,Object> claims = userService.getJwtUtil().getClaims(jwt);
            String providerId = String.valueOf(claims.get("providerId"));
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(appCache.getProviderOffer(providerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    /**
     * Hinzufügen von einem Offer
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

    @PostMapping("/offer/register")
    public ResponseEntity<String> registerOffer(@RequestParam("jwt") String jwt,
                                                @RequestParam("offer") String offerId) {
        try {
            Map<String, Object> claims = userService.getJwtUtil().getClaims(jwt);
            Integer userId = (Integer) claims.get("id");
            Integer parsedOfferId = Integer.parseInt(offerId);

            // JSON-Nachricht bauen
            String payload = String.format("{\"userId\": %d, \"offerId\": %d}", userId, parsedOfferId);

            // MQTT senden
            router.sendMessage("offer/register", payload);

            return ResponseEntity.ok("Registrierung wurde gesendet.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Fehler: " + e.getMessage());
        }
    }

    @PostMapping("/offer/status")
    public ResponseEntity<String> changeStatus(@RequestParam("jwt") String jwt,
                                               @RequestParam("offer") String offerId,
                                               @RequestParam("status") RegistrationStatus status) {

        try {
            Map<String, Object> claims = userService.getJwtUtil().getClaims(jwt);

            Integer userId = (Integer) claims.get("id");
            Integer parsedOfferId = Integer.parseInt(offerId);
            RegistrationStatus parsedStatus = RegistrationStatus.valueOf(status.name());

            var role = claims.get("role");

//            if (!role.equals("AUTHOR")) {
//                return ResponseEntity.badRequest().build();
//            }

            // Status jetzt mit im JSON
            String payload = String.format(
                    "{\"userId\": %d, \"offerId\": %d, \"status\": \"%s\"}",
                    userId, parsedOfferId, parsedStatus.name()
            );

            // Nachricht senden
            router.sendMessage("offer/status", payload);

            return ResponseEntity.ok("Statusänderung wurde gesendet.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Fehler: " + e.getMessage());
        }
    }
}


