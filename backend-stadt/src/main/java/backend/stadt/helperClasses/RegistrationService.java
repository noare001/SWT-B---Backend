package backend.stadt.helperClasses;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.modells.Offer;
import backend.stadt.modells.OfferRegistration;
import backend.stadt.repositorys.OfferRegistrationRepository;
import backend.stadt.repositorys.OfferRepository;
import backend.stadt.repositorys.UserRepository;
import backend.stadt.user.AppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferRegistrationRepository registrationRepository;

    public RegistrationService(UserRepository userRepository,
                               OfferRepository offerRepository,
                               OfferRegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.registrationRepository = registrationRepository;
    }

    public boolean registerUserToOffer(Long userId, Integer offerId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<Offer> offerOpt = offerRepository.findById(offerId);

        if (userOpt.isPresent() && offerOpt.isPresent()) {
            AppUser user = userOpt.get();
            Offer offer = offerOpt.get();

            OfferRegistrationKey key = new OfferRegistrationKey(user.getId(), offer.getOfferId());

            if (registrationRepository.existsById(key)) {
                return false;
            }

            OfferRegistration registration = new OfferRegistration();
            registration.setId(key);
            registration.setUser(user);
            registration.setOffer(offer);
            registration.setStatus(RegistrationStatus.PENDING);
            registration.setRegisteredAt(LocalDate.now());

            registrationRepository.save(registration);
            return true;
        }

        return false;
    }

    public boolean changeStatus(OfferRegistrationKey key, RegistrationStatus newStatus) {
        return registrationRepository.findById(key).map(registration -> {
            registration.setStatus(newStatus);
            registrationRepository.save(registration);
            return true;
        }).orElse(false);
    }
}
