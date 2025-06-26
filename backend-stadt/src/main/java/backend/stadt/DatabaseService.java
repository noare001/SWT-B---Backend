package backend.stadt;

import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.repositorys.OfferRepository;
import backend.stadt.repositorys.ProviderRepository;
import backend.stadt.user.AppUser;
import backend.stadt.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseService {
    private OfferRepository offerRepository;
    private ProviderRepository providerRepository;
    private UserRepository userRepository;

    @Autowired
    DatabaseService(OfferRepository offerRepository, ProviderRepository providerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
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
    public Offer getOfferById(int id) { return offerRepository.findByOfferId(id); }

    public List<Provider> getProviders() { return providerRepository.findAll(); }
    public Provider getProviderById(int id) { return providerRepository.findById(id); }
}
