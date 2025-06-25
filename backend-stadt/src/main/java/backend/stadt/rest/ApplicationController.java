package backend.stadt.rest;

import backend.stadt.DatabaseService;
import backend.stadt.modells.Offer;
import backend.stadt.user.AppUser;
import backend.stadt.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<AppUser> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        return ResponseEntity.ok(userService.login(name,password));
    }
    @GetMapping
    @RequestMapping("/register")
    public ResponseEntity<AppUser> register(@RequestParam("name") String name, @RequestParam("password") String password,@RequestParam("email") String email) {
        System.out.println("REGISTER IN STADT");
        return ResponseEntity.ok(userService.register(name,password,email));
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getOffer(){
        return ResponseEntity.ok(databaseService.getOffers());
    }
}
