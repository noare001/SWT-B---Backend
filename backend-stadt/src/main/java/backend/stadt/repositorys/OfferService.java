package backend.stadt.repositorys;

import backend.stadt.modells.Offer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OfferService {
    private final OfferRepository offerRepo;

    public OfferService(OfferRepository offerRepo) {
        this.offerRepo = offerRepo;
    }

    public List<Offer> findAllOffers() {
        return offerRepo.findAll();
    }

    public Offer findOfferFullyLoaded(int id) {
        return offerRepo.findFullyLoaded(id);
    }

    public void saveOffer(Offer offer) {
        offerRepo.save(offer);
    }

    public void deleteOffer(int id) {
        offerRepo.deleteById(id);
    }

    public String generateOfferKey(Offer offer) {
        return offer.getOfferId() + "-" + offer.getProvider().getId();
    }

    public Map<String, Offer> getOfferCache() {
        return offerRepo.findAllFullyLoaded().stream()
            .collect(Collectors.toMap(this::generateOfferKey, o -> o));
    }
}
