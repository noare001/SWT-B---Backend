package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class KidsAppCache extends KidsAppData {

    private Map<String,String> cache;

    private KidsAppDataService dataService;

    private State state;
    private WebClient webClient;

    @Autowired
    public KidsAppCache(KidsAppDataService dataService){
        this.dataService = dataService;
    }
    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl("http://localhost:8082/api/stadt/cache").build();
        fillCache();
    }

    @PostConstruct
    public void setup(){
        cache = Collections.synchronizedMap(new HashMap<>());
    }

    public void fillCache(){
        Mono<String> cacheMono  = webClient.get().retrieve()
                .bodyToMono(String.class)
                .onErrorResume(ex -> {
            System.err.println("Cache-Service nicht erreichbar: " + ex.getMessage());
            return Mono.just("[]");
        });
        ObjectMapper objectMapper = new ObjectMapper();
        cacheMono.subscribe(jsonString -> {
            try {
                cache = objectMapper.readValue(jsonString, new TypeReference<>() {});
                System.out.println("Empfangener Cache: " + cache);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void setCache(Map<String,String> newCache){
        cache = Collections.synchronizedMap(newCache);
    }

    public String getOffer(String key) {
        return state.getOffer(key);
    }
}
