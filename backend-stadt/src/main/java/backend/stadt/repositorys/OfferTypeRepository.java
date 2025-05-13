package backend.stadt.repositorys;

import backend.stadt.modells.OfferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferTypeRepository extends JpaRepository<OfferType, Long> {
    OfferType findByOfferId(Integer offerId);
}
