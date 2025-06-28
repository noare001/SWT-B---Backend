package backend.stadt.services;

import backend.stadt.enums.OfferStatus;
import backend.stadt.modells.Offer;
import backend.stadt.repositorys.OfferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class OfferService {

    private ObjectMapper mapper;
    private OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    @PostConstruct
    public void init(){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void addOffer(String offferJSON){
        try{
            Offer newOffer = mapper.readValue(offferJSON, Offer.class);
            newOffer.setStatus(OfferStatus.PROCESSING);
            offerRepository.save(newOffer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(Long id, OfferStatus status) {
        Offer offer = offerRepository.findById(id).orElseThrow();
        offer.setStatus(status);
        offerRepository.save(offer);
    }
}
