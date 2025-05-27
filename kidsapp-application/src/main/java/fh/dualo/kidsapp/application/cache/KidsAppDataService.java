package fh.dualo.kidsapp.application.cache;

import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KidsAppDataService extends KidsAppData{

    private KidsAppMqttClient mqttSubscriber;

    @Autowired
    public KidsAppDataService(KidsAppMqttClient mqttSubscriber){
        this.mqttSubscriber = mqttSubscriber;
    }

    @Override
    public String getOffer(String key) {
        return null;
    }
}
