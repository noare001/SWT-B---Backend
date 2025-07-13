package backend.stadt.repositorys;

import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.modells.OfferRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRegistrationRepository extends JpaRepository<OfferRegistration, OfferRegistrationKey> {
    Optional<OfferRegistration> findByUser_IdAndOffer_OfferId(int userId, int offerId);
}
