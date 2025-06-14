package fh.dualo.kidsapp.application.cache;

import fh.dualo.kidsapp.application.mqtt.KidsAppMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KidsAppDataService extends KidsAppData{

    private KidsAppMqttClient mqttSubscriber;

    @Autowired
    public KidsAppDataService(KidsAppMqttClient mqttSubscriber){
        this.mqttSubscriber = mqttSubscriber;
    }

    public void requestCacheData() {
        try{
            mqttSubscriber.sendMessage("cache/request", "");
        }catch (MqttException e){
            e.printStackTrace();
        }
    }



    @Override
    public String getOffer(String key) {
        //ToDO Soll direkt eine Anfrage An die Stadt senden um die passenden offer zu finden. Implementieren wir wenn wir zeit dazu haben
        return null;
    }
}
