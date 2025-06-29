package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class CacheReady extends State {
    private Map<String, JsonNode> cache;

    public CacheReady(Map<String, JsonNode> cache) {
        this.cache = cache;
    }

    @Override
    public String getOffer(String key) {
        //ToDo alle die zum key passen. Ist also nicht nur das hier.
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(cache);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
