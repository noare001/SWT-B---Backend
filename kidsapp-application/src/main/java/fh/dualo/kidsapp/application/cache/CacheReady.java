package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fh.dualo.kidsapp.application.enums.RegistrationStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheReady extends State {
    //Der Schlüssel ist {offerId}-{providerId}
    private Map<String, JsonNode> cache;
    //Der Schlüssel ist "{offerId}-{userId}"
    private Map<String, RegistrationStatus> registrationCache;

    public CacheReady(Map<String, JsonNode> cache, Map<String, RegistrationStatus> registrationCache) {
        this.cache = new ConcurrentHashMap<>();
        this.registrationCache = new ConcurrentHashMap<>();
        this.cache.putAll(cache);
        this.registrationCache.putAll(registrationCache);
    }

    @Override
    public Map<String, JsonNode> getOffer() {
            return cache;
    }

    @Override
    public Map<String,RegistrationStatus> getRegistrations() {
            return registrationCache;
    }

    public void update(String newOffer){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(newOffer);
            String key = node.fieldNames().next();
            JsonNode value = node.get(key);
            cache.put(key, value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON: " + newOffer, e);
        }
    }
}
