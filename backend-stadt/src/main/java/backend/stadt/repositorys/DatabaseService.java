package backend.stadt.repositorys;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.modells.Offer;
import backend.stadt.modells.OfferRegistration;
import backend.stadt.modells.Provider;
import backend.stadt.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatabaseService {
    private OfferService offerService;
    private ProviderService providerService;
    private UserService userService;
    private RegistrationService registrationService;
    @Autowired
    DatabaseService(OfferService offerService, ProviderService providerService, UserService userService, RegistrationService registrationService) {
        this.offerService = offerService;
        this.providerService = providerService;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    //User
    public List<AppUser> getUser() {
        return userService.findAll();
    }
    public Optional<AppUser> getUser(Integer id) {
        return userService.findById(id);
    }
    public void saveUser(AppUser user){
        userService.save(user);
    }
    public void deleteUser(Integer id){
        userService.deleteById(id);
    }
    public AppUserDTO login(String username, String password) {
        return userService.login(username,password);
    }
    public AppUserDTO register(String username, String password, String email) {
        return userService.register(username,password,email);
    }
    //Offer
    public List<Offer> getOffers() { return offerService.findAllOffers(); }
    public Offer getOfferById(int id) { return offerService.findOfferFullyLoaded(id); }
    public void saveOffer(Offer offer){
        offerService.saveOffer(offer);
    }
    public void deleteOffer(int id){
        offerService.deleteOffer(id);
    }
    public String getOfferKey(Offer offer){
        return offerService.generateOfferKey(offer);
    }
    public Map<String, Offer> getCache() {
        return offerService.getOfferCache();
    }
    //Registrations
    public Map<String, RegistrationStatus> getRegistrations() {
        return registrationService.getAllRegistrations();
    }

    public OfferRegistration registerUserToOffer(Integer userId, Integer offerId) {
        return registrationService.registerUserToOffer(userId, offerId);
    }

    public void deleteRegistration(int userId, int offerId) {
        registrationService.deleteRegistration(userId, offerId);
    }

    public List<Provider> getProviders() {
        return providerService.findAll();
    }

    public Provider getProviderById(int id) {
        return providerService.findById(id);
    }

    public void saveProvider(Provider provider) {
        providerService.save(provider);
    }

    public void deleteProvider(int id) {
        providerService.deleteProviderAndDetachUsers(id);
    }
}
