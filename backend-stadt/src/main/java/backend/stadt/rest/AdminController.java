package backend.stadt.rest;

import backend.stadt.DatabaseService;
import backend.stadt.enums.OfferStatus;
import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.mqtt.StadtMqttClient;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final StadtMqttClient mqttClient;
    private DatabaseService databaseService;

    @Autowired
    public AdminController(StadtMqttClient mqttClient, DatabaseService databaseService) {
        this.mqttClient = mqttClient;
        this.databaseService = databaseService;
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        if(mqttClient.isConnected()){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user")
    public List<AppUserDTO> getAllUsers() {
        return databaseService.getUser().stream().map(user -> {
            return new AppUserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(),user.getProvider().getId(), user.getProvider().getName());
        }).toList();
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