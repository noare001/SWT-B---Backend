package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class CacheLoading extends State {

    private WebClient webClient;
    private ObjectMapper mapper;
    public CacheLoading(WebClient webClient){
        this.webClient = webClient;
        mapper = new ObjectMapper();
    }

    @Override
    public Map<String, JsonNode> getOffer() {
        return webClient.get().uri("/cache").retrieve()
                .bodyToMono(String.class)
                .onErrorResume(ex -> {
                    System.err.println("Cache-Service nicht erreichbar: " + ex.getMessage());
                    return Mono.just("{}");
                })
                .<Map<String, JsonNode>>handle((json, sink) -> {
                    try {
                        sink.next(mapper.readValue(json, new TypeReference<Map<String, JsonNode>>() {
                        }));
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException(e));
                    }
                }).block();
    }
}
