package backend.stadt.mqtt;

import backend.stadt.enums.OfferStatus;
import backend.stadt.helperClasses.OfferIdentifier;
import backend.stadt.modells.Offer;
import backend.stadt.repositorys.OfferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class MessageHandler {
    private ObjectMapper mapper;
    private OfferRepository offerRepository;
    public MessageHandler(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    @PostConstruct
    public void init(){
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Offer addOffer(String offferJSON){
        try{
            Offer newOffer = mapper.readValue(offferJSON, Offer.class);
            newOffer.setStatus(OfferStatus.RECEIVED);
            offerRepository.save(newOffer);
            return newOffer;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
