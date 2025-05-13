package backend.stadt;

import backend.stadt.modells.*;
import backend.stadt.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    private CarrierRepository carrierRepository;
    private FilterRepository filterRepository;
    private LanguagesRepository languagesRepository;
    private OfferRepository offerRepository;
    private OfferTypeRepository offerTypeRepository;
    private ProviderRepository providerRepository;
    private TargetGroupRepository targetGroupRepository;

    @Autowired
    DatabaseService(CarrierRepository carrierRepository, FilterRepository filterRepository, LanguagesRepository languagesRepository, OfferRepository offerRepository, OfferTypeRepository offerTypeRepository, ProviderRepository providerRepository, TargetGroupRepository targetGroupRepository) {
        this.carrierRepository = carrierRepository;
        this.filterRepository = filterRepository;
        this.languagesRepository = languagesRepository;
        this.offerRepository = offerRepository;
        this.offerTypeRepository = offerTypeRepository;
        this.providerRepository = providerRepository;
        this.targetGroupRepository = targetGroupRepository;
    }
    public Iterable<Carrier> getCarriers() { return carrierRepository.findAll(); }
    public Carrier getCarrierById(int id) { return carrierRepository.findById(id); }

    public Filter getFilterByOfferId(int id) { return filterRepository.findByOfferId(id); }

    public Languages getLanguagesByOfferId(int id) { return languagesRepository.findByOfferId(id); }

    public Iterable<Offer> getOffers() { return offerRepository.findAll(); }
    public Offer getOfferById(int id) { return offerRepository.findByOfferId(id); }

    public OfferType getOfferTypeByOfferId(int id) { return offerTypeRepository.findByOfferId(id); }

    public Iterable<Provider> getProviders() { return providerRepository.findAll(); }
    public Provider getProviderById(int id) { return providerRepository.findByProviderId(id); }

    public TargetGroup getTargetGroupByOfferId(int id) { return targetGroupRepository.findByOfferId(id); }
}
