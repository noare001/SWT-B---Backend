package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


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
