package fh.dualo.kidsapp.application.mqtt;

import fh.dualo.kidsapp.application.cache.KidsAppCache;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class MessageRouter {

    private KidsAppCache cache;

    @Autowired
    public MessageRouter(KidsAppCache cache){
        this.cache = cache;
    }

    public void processMessage(String topic, String payload){
        switch (topic){
            case "cache/data" -> cache.fillCache(payload);
        }
    }
}
