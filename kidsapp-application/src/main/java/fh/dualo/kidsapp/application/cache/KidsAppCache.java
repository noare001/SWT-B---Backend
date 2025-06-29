package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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


    private KidsAppDataService dataService;
    private State state;
    private WebClient webClient;

    @Autowired
    public KidsAppCache(KidsAppDataService dataService){
        this.dataService = dataService;
        state = new CacheLoading(dataService);
    }
    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl("http://localhost:8082/api/stadt/cache").build();
        fillCache();
    }

    public void fillCache(){
        ObjectMapper mapper = new ObjectMapper();
        state = new CacheLoading(dataService);
        webClient.get().retrieve()
                .bodyToMono(String.class)
                .onErrorResume(ex -> {
                    System.err.println("Cache-Service nicht erreichbar: " + ex.getMessage());
                    return Mono.just("{}");
                })
                .map(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<Map<String, JsonNode>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe(jsonMap -> {
                    state = new CacheReady(jsonMap);
                    System.out.println("\u001B[32mCache Erfolgreich geladen!\u001B[0m");
                });

    }

    public String getOffer(String key) {
        return state.getOffer(key);
    }
}
