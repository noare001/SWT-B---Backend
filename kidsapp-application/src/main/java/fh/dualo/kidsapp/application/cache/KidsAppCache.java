package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fh.dualo.kidsapp.application.enums.RegistrationStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        CacheLoading cacheLoading = new CacheLoading(webClient);
        Map<String,JsonNode> data = cacheLoading.getOffer();
        Map<String,RegistrationStatus> registrations = cacheLoading.getRegistrations();
        if(!data.isEmpty()){
            System.out.println("\u001B[32mCache erfolgreich geladen!\u001B[0m");
            state = new CacheReady(data, registrations);
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
        System.out.println("Updating an Offer");
        if(state instanceof CacheReady){
            ((CacheReady) state).update(newOffer);
        }else{
            init();
        }
    }
    public Collection<JsonNode> getOffer() {
        Map<String, JsonNode> data = state.getOffer();
        if(state instanceof CacheLoading){
            if(!data.isEmpty()){
                System.out.println("\u001B[32mCache erfolgreich geladen!\u001B[0m");
                state = new CacheReady(data, state.getRegistrations());
            }
            return data.values().stream()
                    .filter(jsonNode -> {
                        JsonNode status = jsonNode.get("status");
                        return status != null && "ACCEPTED".equals(status.asText());
                    })
                    .toList();
        }
        return data.values().stream()
                .filter(jsonNode -> {
                    JsonNode status = jsonNode.get("status");
                    return status != null && "ACCEPTED".equals(status.asText());
                })
                .toList();
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

    //Registration
    public void updateRegistration(String key, RegistrationStatus status){
        if(state instanceof CacheReady){
            state.getRegistrations().put(key, status);
        }else {
            init();
        }
    }
    public void updateRegistration(String json){
        if(state instanceof CacheReady) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(json);
                String offerId = node.get("offerId").asText();
                String userId = node.get("userId").asText();
                String status = node.get("status").asText();
                state.getRegistrations().put(getRegistrationKey(userId, offerId), RegistrationStatus.valueOf(status));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Invalid JSON: " + json, e);
            }
        }else {
            init();
        }
    }

    public Map<RegistrationStatus, Set<Integer>> getUserRegistrations(String userId) {
        return state.getRegistrations().entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("-" + userId))
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(
                                entry -> Integer.parseInt(entry.getKey().split("-")[0]),
                                Collectors.toSet()
                        )
                ));
    }
    public void deleteRegistration(String key){
        if(state instanceof CacheReady){
            state.getRegistrations().remove(key);
        }
    }
    public String getRegistrationKey(String userId, String offerId){
        return offerId + "-" + userId;
    }

}
