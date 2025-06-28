package backend.stadt.mqtt;

import backend.stadt.services.OfferService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MessageRouter {

    private OfferService offerService;

    @Autowired
    public MessageRouter(
            @Lazy OfferService offerService){
        this.offerService = offerService;
    }

    public void processMessage(String topic, String payload){
        switch (topic) {
            case "offer/add": offerService.addOffer(payload); break;
            default: System.out.println("Unknown topic: " + topic);
        }
    }
}
