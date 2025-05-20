package backend.stadt.user;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {



    public UserService(){

    }

    public String login(Map<String, Object> payload){
        return "JWT String";
    }
}
