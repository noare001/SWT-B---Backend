package backend.stadt.repositorys;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.modells.Offer;
import backend.stadt.modells.OfferRegistration;
import backend.stadt.modells.Provider;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatabaseService {
    private OfferRepository offerRepo;
    private ProviderRepository providerRepo;
    private UserRepository userRepo;
    private OfferRegistrationRepository registrationRepo;
    @Autowired
    DatabaseService(OfferRepository offerRepo, ProviderRepository providerRepo, UserRepository userRepo,OfferRegistrationRepository registrationRepo) {
        this.offerRepo = offerRepo;
        this.providerRepo = providerRepo;
        this.userRepo = userRepo;
        this.registrationRepo = registrationRepo;
    }

    //User
    public List<AppUser> getUser() {
        return userRepo.findAll();
    }
    public Optional<AppUser> getUser(Integer id) {
        return userRepo.findById(id);
    }
    public void saveUser(AppUser user){
        userRepo.save(user);
    }
    public void deleteUser(Integer id){
        userRepo.deleteById(id);
    }
    public AppUserDTO login(String username, String password) {
        AppUser user =  userRepo.getAppUserByNameAndPassword(username, sha256Hash(password));
        return new AppUserDTO(user);
    }
    public AppUserDTO register(String username, String password, String email) {
        if (userRepo.existsByName(username)) {
            throw new IllegalArgumentException("Benutzername bereits vergeben");
        }

        // Passwort hashen
        String hashedPassword = sha256Hash(password);

        // Neues Benutzerobjekt erstellen
        AppUser user = new AppUser();
        user.setName(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setRole(Role.USER);
        userRepo.save(user);

        return new AppUserDTO(user);
    }
    public static String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Byte-Array in Hex-String umwandeln
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithmus nicht verfügbar", e);
        }
    }
    //Offer
    public List<Offer> getOffers() { return offerRepo.findAll(); }
    public Offer getOfferById(int id) { return offerRepo.findFullyLoaded(id); }
    public void saveOffer(Offer offer){
        offerRepo.save(offer);
    }
    public void deleteOffer(int id){
        offerRepo.deleteById(id);
    }
    public String getOfferKey(Offer offer){
        return offer.getOfferId() + "-" + offer.getProvider().getId();
    }
    public Map<String, Offer> getCache() throws JsonProcessingException {
        List<Offer> offerList = offerRepo.findAllFullyLoaded();
        Map<String, Offer> map = new HashMap<>();
        offerList.forEach(offer ->
                map.put(getOfferKey(offer), offer)
        );
        return map;
    }
    //Registrations
    public Map<String, RegistrationStatus> getRegistrations(){
        List<OfferRegistration> registrations = registrationRepo.findAll();
        Map<String, RegistrationStatus> map = new HashMap<>();
        registrations.forEach(reg ->
                map.put(reg.getOfferId() + "-" + reg.getUser().getId(), reg.getStatus())
        );
        return map;
    }
    public OfferRegistration registerUserToOffer(Integer userId, Integer offerId) {
        Optional<AppUser> userOpt = getUser(userId);
        Offer offer = getOfferById(offerId);

        if (userOpt.isPresent() && offer != null) {
            AppUser user = userOpt.get();

            OfferRegistrationKey key = new OfferRegistrationKey(user.getId(), offer.getOfferId());

            OfferRegistration registration = new OfferRegistration();
            registration.setId(key);
            registration.setUser(user);
            registration.setOffer(offer);
            registration.setStatus(RegistrationStatus.ACCEPTED);
            registration.setRegisteredAt(LocalDate.now());

            registrationRepo.save(registration);
            return registration;
        }
        return null;
    }
    public void deleteRegistration(int userId, int offerId){
        Optional<OfferRegistration> registration = registrationRepo.findByUser_IdAndOffer_OfferId(userId,offerId);
        registration.ifPresent(offerRegistration -> registrationRepo.delete(offerRegistration));
    }
    //Provider
    public List<Provider> getProviders() { return providerRepo.findAll(); }
    public Provider getProviderById(int id) { return providerRepo.findById(id); }
    public void saveProvider(Provider provider){
        providerRepo.save(provider);
    }
    @Transactional
    public void deleteProvider(int id){
        Provider provider = providerRepo.findById(id);
        List<AppUser> users = userRepo.findAllByProviderId(id);
        for (AppUser user : users) {
            user.setProvider(null);
            user.setRole(Role.USER);
        }
        userRepo.saveAll(users);
        providerRepo.delete(provider);
    }
}
