package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KidsAppCache {

    private State state;
    private WebClient webClient;

    @Value("${backend.base-url}")
    private String backendBaseUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(backendBaseUrl+"/api/stadt").build();
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
    public Collection<JsonNode> getOffer() {
        Map<String, JsonNode> data = state.getOffer();
        if(state instanceof CacheLoading){
            if(!data.isEmpty()){
                System.out.println("\u001B[32mCache erfolgreich geladen!\u001B[0m");
                state = new CacheReady(data);
            }
            return data.values();
        }
        return data.values();
    }
    public List<JsonNode> getProviderOffer(String providerId) {
        if (providerId == null || providerId.isBlank()) {
            return List.of();
        }
        return state.getOffer().entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("-"+providerId)).map(Map.Entry::getValue)
                .toList();
    }
    public JsonNode getOffer(int id) {
        Map<String, JsonNode> offerMap = state.getOffer();
        String searchKey = offerMap.keySet().stream().filter(key -> key.startsWith(id+"-")).findFirst().orElse(null);
        if(offerMap.containsKey(searchKey)){
            return offerMap.get(searchKey);
        }
        return null;
    }

    public void delete(String key){
        state.getOffer().remove(key);
    }

}
