package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

public class CacheReady extends State {
    //Der Schlüssel ist {offerId}-{providerId}
    private Map<String, JsonNode> cache;

    public CacheReady(Map<String, JsonNode> cache) {
        this.cache = cache;
    }

    @Override
    public Map<String, JsonNode> getOffer() {
            return cache.entrySet().stream()
                    .filter(entry -> {
                        JsonNode statusNode = entry.getValue().get("status");
                        return statusNode != null && "ACCEPTED".equals(statusNode.asText());
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
