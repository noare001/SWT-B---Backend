package backend.stadt.rest;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.helperClasses.RegistrationService;
import backend.stadt.repositorys.DatabaseService;
import backend.stadt.enums.OfferStatus;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.modells.Offer;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import backend.stadt.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stadt")
public class ApplicationController {

    private UserService userService;
    private DatabaseService databaseService;

    private final RegistrationService registrationService;

    @Autowired
    public ApplicationController(UserService userService, DatabaseService databaseService, RegistrationService registrationService) {
        this.userService = userService;
        this.databaseService = databaseService;
        this.registrationService = registrationService;
    }

    @GetMapping
    @RequestMapping("/login")
    public ResponseEntity<AppUserDTO> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        return ResponseEntity.ok(userService.login(name,password));
    }
    @GetMapping
    @RequestMapping("/register")
    public ResponseEntity<AppUserDTO> register(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("email") String email) {
        return ResponseEntity.ok(userService.register(name,password,email));
    }

    @GetMapping
    @RequestMapping("/cache")
    public ResponseEntity<String> getCache() throws JsonProcessingException {
        return ResponseEntity.ok(databaseService.getCache());
    }

    @PostMapping
    @RequestMapping("/offer")
    public ResponseEntity<Map<String,Offer>> addOffer(@RequestParam("id") int userId, @RequestBody Offer newOffer) {
        AppUser user = databaseService.getUser(userId).orElseThrow();
        if(!user.getRole().equals(Role.AUTHOR) || user.getProvider() == null){
            ResponseEntity.badRequest().build();
        }
        newOffer.setStatus(OfferStatus.PROCESSING);
        newOffer.setProvider(user.getProvider());
        databaseService.saveOffer(newOffer);
        return ResponseEntity.ok(Map.of(newOffer.getOfferId() + "-" + newOffer.getName(), newOffer));
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getOffer(){
        return ResponseEntity.ok(databaseService.getOffers());
    }

    @PostMapping("/offer/register")
    public ResponseEntity<String> registerToOffer(@RequestParam("userId") Integer userId,
                                                  @RequestParam("offer") Integer offerId) {
        boolean success = registrationService.registerUserToOffer(Long.valueOf(userId), offerId);

        if (!success) {
            return ResponseEntity.badRequest().body("Registrierung fehlgeschlagen (bereits registriert oder ungültige IDs).");
        }

        return ResponseEntity.ok("Registrierung erfolgreich.");
    }

    @PostMapping("/offer/status")
    public ResponseEntity<String> changeRegistrationStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("offerId") Integer offerId,
            @RequestParam("status") RegistrationStatus newStatus) {

        OfferRegistrationKey key = new OfferRegistrationKey(userId, offerId);
        boolean updated = registrationService.changeStatus(key, newStatus);

        if (updated) {
            return ResponseEntity.ok("Status erfolgreich geändert.");
        } else {
            return ResponseEntity.badRequest().body("Registrierung nicht gefunden.");
        }
    }
}
