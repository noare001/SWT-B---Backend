package backend.stadt.repositorys;

import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.modells.OfferRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRegistrationRepository extends JpaRepository<OfferRegistration, OfferRegistrationKey> {
    List<OfferRegistration> findByOffer_OfferId(Integer offerId);
    List<OfferRegistration> findByUser_Id(Long userId);
}

