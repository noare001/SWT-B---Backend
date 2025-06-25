package backend.stadt;

import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.repositorys.OfferRepository;
import backend.stadt.repositorys.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    private OfferRepository offerRepository;
    private ProviderRepository providerRepository;

    @Autowired
    DatabaseService(OfferRepository offerRepository, ProviderRepository providerRepository) {
        this.offerRepository = offerRepository;
        this.providerRepository = providerRepository;
    }

    public List<Offer> getOffers() { return offerRepository.findAll(); }
    public Offer getOfferById(int id) { return offerRepository.findByOfferId(id); }

    public Iterable<Provider> getProviders() { return providerRepository.findAll(); }
    public Provider getProviderById(int id) { return providerRepository.findById(id); }
}
