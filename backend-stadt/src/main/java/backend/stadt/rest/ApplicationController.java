package backend.stadt.rest;

import backend.stadt.DatabaseService;
import backend.stadt.enums.OfferStatus;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.modells.Offer;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import backend.stadt.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    public ApplicationController(UserService userService,DatabaseService databaseService){
        this.userService = userService;
        this.databaseService = databaseService;
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
        if(!user.getRole().equals(Role.AUTHOR)){
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
}
