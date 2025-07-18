package fh.dualo.kidsapp.application.user;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class KidsAppUserService {

    private WebClient webClient;
    @Getter
    private JwtUtil jwtUtil;
    @Value("${backend.base-url}")
    private String backendBaseUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(backendBaseUrl+"/api/stadt").build();
        jwtUtil = new JwtUtil();
    }

    public Map<String, Object> register(String password, String username, String email) {
        Map<String, Object> result = registerRequest(password, username, email)
                .block();
        return handleResponse(result);
    }

    private Mono<Map<String, Object>> registerRequest(String password, String username, String email) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/register")
                        .queryParam("name", username)
                        .queryParam("password", password)
                        .queryParam("email", email)
                        .build())
                .exchangeToMono(response -> {
                    if (!response.statusCode().equals(HttpStatus.OK)) {
                        return Mono.empty();
                    }
                    return response.bodyToMono(new ParameterizedTypeReference<>() {
                    });
                });
    }

    public Map<String, Object> login(String password, String username) {
        Map<String, Object> result = loginRequest(password, username)
                .block();
        return handleResponse(result);
    }

    /**
     * Ruft /login?name={username}&password={password} auf.
     * Gibt bei 404 ein leeres Mono zurück.
     */
    private Mono<Map<String, Object>> loginRequest(String password, String username) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/login")
                        .queryParam("name", username)
                        .queryParam("password", password)
                        .build())
                .exchangeToMono(response -> {
                    if (!response.statusCode().equals(HttpStatus.OK)) {
                        return Mono.empty();
                    }
                    return response.bodyToMono(new ParameterizedTypeReference<>() {
                    });
                });
    }

    private Map<String, Object> handleResponse(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        map.put("jwt", jwtUtil.generateToken(map));
        return map;
    }

}
