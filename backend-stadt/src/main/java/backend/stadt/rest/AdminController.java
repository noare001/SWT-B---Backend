package backend.stadt.rest;

import backend.stadt.DatabaseService;
import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.modells.AppUserDTO;
import backend.stadt.mqtt.StadtMqttClient;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
            AppUserDTO dto = new AppUserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setProviderId(user.getProvider() != null ? user.getProvider().getId() : null);
            return dto;
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
}