package backend.stadt.rest;

import backend.stadt.user.AppUser;
import backend.stadt.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/stadt")
public class ApplicationController {

    private UserService userService;
    public ApplicationController(UserService userService){
        this.userService = userService;
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
}
