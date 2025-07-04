package backend.stadt.rest;

import backend.stadt.DatabaseService;
import backend.stadt.enums.OfferStatus;
import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.mqtt.MessageRouter;
import backend.stadt.mqtt.StadtMqttClient;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final MessageRouter router;
    private DatabaseService databaseService;
    private ObjectMapper mapper;

    @Autowired
    public AdminController(MessageRouter mqttClient, DatabaseService databaseService) {
        this.router = mqttClient;
        this.databaseService = databaseService;
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        if(router.isConnected()){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user")
    public List<AppUserDTO> getAllUsers() {
        return databaseService.getUser().stream().map(AppUserDTO::new).toList();
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody AppUserDTO update) {
        AppUser user = databaseService.getUser(id).orElseThrow();
        user.setRole(update.getRole());
        if(update.getRole() == Role.AUTHOR && update.getProviderId() != null){
            user.setProvider(databaseService.getProviderById(update.getProviderId()));
        }else{
            user.setProvider(null);
        }
        databaseService.saveUser(user);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/offer")
    public ResponseEntity<List<Offer>> getOffer() {
        return ResponseEntity.ok(databaseService.getOffers());
    }
    @PostMapping("/offer")
    public ResponseEntity<String> processOffer(
            @RequestParam("accepted") boolean accepted,
            @RequestParam("id") int id) throws JsonProcessingException, MqttException {
        Offer offer = databaseService.getOfferById(id);
        if (offer == null) {
            return ResponseEntity.notFound().build();
        }
        offer.setStatus(accepted ? OfferStatus.ACCEPTED : OfferStatus.REJECTED);
        databaseService.saveOffer(offer);

        String key = offer.getOfferId() + "-" + offer.getName();

        JsonNode jsonNode = mapper.valueToTree(Map.of(key, offer));

        router.sendMessage("offer/processed", mapper.writeValueAsString(jsonNode));
        return ResponseEntity.ok().build();
    }
    @GetMapping("/provider")
    public ResponseEntity<List<Provider>> getProvider() {
        return ResponseEntity.ok(databaseService.getProviders());
    }
    @PutMapping("/offer/{id}/status/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") int id, @PathVariable("status") String newStatus) {
        Offer offer = databaseService.getOfferById(id);
        offer.setStatus(OfferStatus.valueOf(newStatus));
        databaseService.saveOffer(offer);
        return ResponseEntity.ok().build();
    }
}