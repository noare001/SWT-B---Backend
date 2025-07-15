package backend.stadt.rest;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.OfferBuilder;
import backend.stadt.modells.Provider;
import backend.stadt.repositorys.DatabaseService;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.modells.Offer;
import backend.stadt.user.AppUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stadt")
public class ApplicationController {

    private DatabaseService databaseService;
    @Autowired
    public ApplicationController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/login")
    public ResponseEntity<AppUserDTO> login(@RequestParam("name") String name,
                                            @RequestParam("password") String password) {
        AppUserDTO user = databaseService.login(name, password);
        if(user == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/register")
    public ResponseEntity<AppUserDTO> register(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("email") String email) {
        AppUserDTO user = databaseService.register(name, password, email);
        if(user == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/registrations")
    public ResponseEntity<Map<String, RegistrationStatus>> getRegistrations() {
        return ResponseEntity.ok(databaseService.getRegistrations());
    }

    @GetMapping("/offer")
    public ResponseEntity<Map<String, Offer>> getCache() throws JsonProcessingException {
        return ResponseEntity.ok(databaseService.getCache());
    }

    @PostMapping("/offer")
    public ResponseEntity<Map<String,Offer>> addOffer(@RequestParam("id") int userId, @RequestBody Offer newOffer) {
        Offer offer = OfferBuilder
                .fromTemplate(newOffer)
                .withProvider(databaseService.getUser(userId).orElseThrow())
                .withDefaultStatus()
                .build();

        databaseService.saveOffer(offer);
        return ResponseEntity.ok(Map.of(databaseService.getOfferKey(offer), offer));
    }

    @DeleteMapping("/offer")
    public ResponseEntity<Void> deleteOffer(
            @RequestParam("providerId") int providerId,
            @RequestParam("userId") int userId,
            @RequestParam("offerId") int offerId
    ){
        Offer offer = databaseService.getOfferById(offerId);
        AppUser user = databaseService.getUser(userId).orElseThrow();
        Provider provider = databaseService.getProviderById(providerId);
        if(provider != null && user.getProvider().getId() == providerId && offer.getProvider().getId() == providerId){
            databaseService.deleteOffer(offerId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getOffer(){
        return ResponseEntity.ok(databaseService.getOffers());
    }
}
