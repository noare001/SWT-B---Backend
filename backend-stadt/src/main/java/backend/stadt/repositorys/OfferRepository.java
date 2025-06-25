package backend.stadt.repositorys;

import backend.stadt.modells.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Override
    List<Offer> findAll();

    List<Offer> findByPublished(Boolean published);

    Offer findByOfferId(Integer offerId);

}
