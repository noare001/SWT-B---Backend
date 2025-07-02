package fh.dualo.kidsapp.application.controller;

import fh.dualo.kidsapp.application.cache.KidsAppCache;
import fh.dualo.kidsapp.application.model.AnmeldeDTO;
import fh.dualo.kidsapp.application.model.OfferDTO;
import fh.dualo.kidsapp.application.model.VorschauDTO;
import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import fh.dualo.kidsapp.application.mqtt.MessageRouter;
import fh.dualo.kidsapp.application.user.KidsAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<String> searchOffer() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer(""));
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
     * Es Werden alle Offer in einer Liste zurückgegeben, die zum suchschlüssel Passen.
     * 'start' und 'end' legen fest, die wievielten Offer die zur SUchanfrage pasen, zurücgegebn werden sollten.
     * Bsp: localhost:8090/api/search?start=1&end=20
     */
    @PostMapping
    @RequestMapping("/search")
    public ResponseEntity<String> register(@RequestParam("key") String key, @RequestParam("start") int start, @RequestParam("end") int end) {
        //ToDo VorschauDTO muss angepasst werden. Nur die nötigen daten eines Offers
        if((end-start) > 20 || (end-start) < 1){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(appCache.getOffer(""));
    }

    /**
     * Returnt alle Daten zu dem gesuchten Offer
     * Bsp: localhost:8090/api/offer?id=23455
     */
    @GetMapping
    @RequestMapping("/offer")
    public ResponseEntity<OfferDTO> getOffer(@RequestParam("id") String id) {
        //ToDo ja nh
        return ResponseEntity.ok(null);
    }
}


