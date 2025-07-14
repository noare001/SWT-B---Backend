package backend.stadt.repositorys;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.modells.Offer;
import backend.stadt.modells.OfferRegistration;
import backend.stadt.user.AppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RegistrationService {

    private final OfferRegistrationRepository registrationRepo;
    private final UserRepository userRepo;
    private final OfferRepository offerRepo;

    public RegistrationService(OfferRegistrationRepository registrationRepo, UserRepository userRepo, OfferRepository offerRepo) {
        this.registrationRepo = registrationRepo;
        this.userRepo = userRepo;
        this.offerRepo = offerRepo;
    }

    public Map<String, RegistrationStatus> getAllRegistrations() {
        List<OfferRegistration> registrations = registrationRepo.findAll();
        Map<String, RegistrationStatus> map = new HashMap<>();
        for (OfferRegistration reg : registrations) {
            String key = reg.getOfferId() + "-" + reg.getUser().getId();
            map.put(key, reg.getStatus());
        }
        return map;
    }

    public OfferRegistration registerUserToOffer(Integer userId, Integer offerId) {
        Optional<AppUser> userOpt = userRepo.findById(userId);
        Offer offer = offerRepo.findFullyLoaded(offerId);

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

    public void deleteRegistration(int userId, int offerId) {
        Optional<OfferRegistration> reg = registrationRepo.findByUser_IdAndOffer_OfferId(userId, offerId);
        reg.ifPresent(registrationRepo::delete);
    }
}
