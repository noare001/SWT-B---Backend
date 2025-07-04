package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class KidsAppCache {

    private State state;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl("http://localhost:8082/api/stadt").build();
        state = new CacheLoading(webClient);
        new Thread(this::fillCache).start();
    }


    public void fillCache(){
        Map<String,JsonNode> data = new CacheLoading(webClient).getOffer();
        if(!data.isEmpty()){
            state = new CacheReady(data);
        }
    }

    public String addOffer(JsonNode jsonNode, int id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/offer").queryParam("id", id).build())
                    .bodyValue(jsonNode)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            update(json);

            return json;

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Senden des Offers an den Backend-Service", e);
        }
    }

    public void update(String newOffer){
        System.out.println("HELLOOOO");
        if(state instanceof CacheReady){
            ((CacheReady) state).update(newOffer);
        }
    }
    public Map<String, JsonNode> getOffer() {
        return state.getOffer();
    }


}
