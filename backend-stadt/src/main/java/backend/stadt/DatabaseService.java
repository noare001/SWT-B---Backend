package backend.stadt;

import backend.stadt.modells.*;
import backend.stadt.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    private OfferRepository offerRepository;
    private ProviderRepository providerRepository;

    @Autowired
    DatabaseService(OfferRepository offerRepository, ProviderRepository providerRepository) {
        this.offerRepository = offerRepository;
        this.providerRepository = providerRepository;
    }

    public Iterable<Offer> getOffers() { return offerRepository.findAll(); }
    public Offer getOfferById(int id) { return offerRepository.findByOfferId(id); }

    public Iterable<Provider> getProviders() { return providerRepository.findAll(); }
    public Provider getProviderById(int id) { return providerRepository.findById(id); }
}
