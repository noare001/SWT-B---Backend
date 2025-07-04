package backend.stadt;

import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.repositorys.OfferRepository;
import backend.stadt.repositorys.ProviderRepository;
import backend.stadt.user.AppUser;
import backend.stadt.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatabaseService {
    private OfferRepository offerRepository;
    private ProviderRepository providerRepository;
    private UserRepository userRepository;
    private ObjectMapper mapper;

    @Autowired
    DatabaseService(OfferRepository offerRepository, ProviderRepository providerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<AppUser> getUser() {
        return userRepository.findAll();
    }
    public Optional<AppUser> getUser(int id) {
        return userRepository.findById(id);
    }
    public void saveUser(AppUser user){
        userRepository.save(user);
    }
    public List<Offer> getOffers() { return offerRepository.findAll(); }
    public Offer getOfferById(int id) { return offerRepository.findFullyLoaded(id); }

    public void saveOffer(Offer offer){
        offerRepository.save(offer);
    }
    public List<Provider> getProviders() { return providerRepository.findAll(); }
    public Provider getProviderById(int id) { return providerRepository.findById(id); }

    public String getCache() throws JsonProcessingException {
        List<Offer> offerList = offerRepository.findAllFullyLoaded();
        Map<String, Offer> map = new HashMap<>();
        offerList.forEach(offer ->
                {
                    map.put(offer.getOfferId() + "-" + offer.getName(), offer);
                }
        );
        return mapper.writeValueAsString(map);
    }
}
