package fh.dualo.kidsapp.application.cache;

import java.util.Map;

public class CacheReady extends State {
    private Map<String, String> cache;

    public CacheReady(Map<String, String> cache) {
        this.cache = cache;
    }

    @Override
    public String getOffer(String key) {
        // Implementierung kommt hier hin
        return cache.get(key);
    }
}
