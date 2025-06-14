package fh.dualo.kidsapp.application.cache;

import java.util.Map;

public class CacheReady extends State {
    private Map<String, String> cache;

    public CacheReady(Map<String, String> cache) {
        this.cache = cache;
    }

    @Override
    public String getOffer(String key) {
        //ToDo alle die zum key passen. Ist also nicht nur das hier.
        return cache.get(key);
    }
}
