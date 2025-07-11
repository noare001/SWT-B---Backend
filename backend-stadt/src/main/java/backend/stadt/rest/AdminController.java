package backend.stadt.rest;

import backend.stadt.repositorys.DatabaseService;
import backend.stadt.enums.OfferStatus;
import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.mqtt.MessageRouter;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import backend.stadt.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    public AdminController(MessageRouter mqttClient, DatabaseService databaseService) {
        this.router = mqttClient;
        this.databaseService = databaseService;
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

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        try{
            databaseService.deleteUser(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
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
    public ResponseEntity<String> updateOffer(
            @RequestParam("accepted") boolean accepted,
            @RequestParam("id") int id) throws JsonProcessingException, MqttException {
        Offer offer = databaseService.getOfferById(id);
        if (offer == null) {
            return ResponseEntity.notFound().build();
        }
        offer.setStatus(accepted ? OfferStatus.ACCEPTED : OfferStatus.PROCESSING);
        databaseService.saveOffer(offer);

        sendUpdateOfferMessage(offer);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/offer")
    public ResponseEntity<String> deleteOffer(
            @RequestParam("id") int id) throws MqttException, JsonProcessingException {
        Offer deletedOffer = databaseService.getOfferById(id);
        databaseService.deleteOffer(id);
        deletedOffer.setStatus(OfferStatus.REJECTED);
        sendUpdateOfferMessage(deletedOffer);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/offer/{id}/status/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") int id, @PathVariable("status") String newStatus) throws MqttException, JsonProcessingException {
        Offer offer = databaseService.getOfferById(id);
        offer.setStatus(OfferStatus.valueOf(newStatus));
        databaseService.saveOffer(offer);
        sendUpdateOfferMessage(offer);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/provider")
    public ResponseEntity<List<Provider>> getProvider() {
        return ResponseEntity.ok(databaseService.getProviders());
    }
    @PostMapping("/provider")
    public ResponseEntity<List<Provider>> addProvider(@RequestBody Provider provider) {
        databaseService.saveProvider(provider);
        return ResponseEntity.ok(databaseService.getProviders());
    }
    @DeleteMapping("/provider/{id}")
    public ResponseEntity<List<Provider>> deleteProvider(@PathVariable("id") long id) {
        databaseService.deleteProvider(id);
        return ResponseEntity.ok(databaseService.getProviders());
    }


    private void sendUpdateOfferMessage(Offer offer) throws JsonProcessingException, MqttException {
        ObjectMapper mapper = MapperUtil.getObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(Map.of(databaseService.getOfferKey(offer), offer));
        router.sendMessage("offer/processed", mapper.writeValueAsString(jsonNode));
    }
}