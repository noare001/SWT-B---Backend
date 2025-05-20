package backend.stadt.rest;

import backend.stadt.user.UserService;
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

    @PostMapping
    public String login(@RequestBody Map<String, Object> payload){
        return userService.login(payload);
    }
}
