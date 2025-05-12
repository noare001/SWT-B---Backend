package fh.dualo.kidsapp.application.cache;

import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class KidsAppCache {

    private Map<String,String> cache;

    private KidsAppMqttClient mqttSubscriber;

    @Autowired
    public KidsAppCache(KidsAppMqttClient mqttSubscriber){
        this.mqttSubscriber = mqttSubscriber;
    }

    @PostConstruct
    public void setup(){
        cache = Collections.synchronizedMap(new HashMap<>());
        new Thread(this::loadData).start();
    }

    public void loadData(){

    }

}
