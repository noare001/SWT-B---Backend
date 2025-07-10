package backend.stadt.repositorys;

import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.modells.Offer;
import backend.stadt.modells.Provider;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import backend.stadt.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatabaseService {
    private OfferRepository offerRepo;
    private ProviderRepository providerRepo;
    private UserRepository userRepo;

    @Autowired
    DatabaseService(OfferRepository offerRepo, ProviderRepository providerRepo, UserRepository userRepo) {
        this.offerRepo = offerRepo;
        this.providerRepo = providerRepo;
        this.userRepo = userRepo;
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
    //Provider
    public List<Provider> getProviders() { return providerRepo.findAll(); }
    public Provider getProviderById(int id) { return providerRepo.findById(id); }

    public String getCache() throws JsonProcessingException {
        List<Offer> offerList = offerRepo.findAllFullyLoaded();
        Map<String, Offer> map = new HashMap<>();
        offerList.forEach(offer ->
                {
                    map.put(getOfferKey(offer), offer);
                }
        );
        return MapperUtil.getObjectMapper().writeValueAsString(map);
    }

    private String getOfferKey(Offer offer){
        return offer.getOfferId() + "-" + offer.getProvider().getId();
    }
}
