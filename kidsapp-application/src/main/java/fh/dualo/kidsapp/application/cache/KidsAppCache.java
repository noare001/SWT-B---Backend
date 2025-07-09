package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;
import java.util.stream.Collectors;

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
            System.out.println("\u001B[32mCache erfolgreich geladen!\u001B[0m");
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
    public Map<String, JsonNode> getProviderOffer(String providerId) {
        if (providerId == null || providerId.isBlank()) {
            return Map.of();
        }
        return state.getOffer().entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("-"+providerId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public JsonNode getOffer(int id) {
        Map<String, JsonNode> offerMap = state.getOffer();
        String searchKey = offerMap.keySet().stream().filter(key -> key.startsWith(id+"-")).findFirst().orElse(null);
        if(offerMap.containsKey(searchKey)){
            return offerMap.get(searchKey);
        }
        return null;
    }




}
