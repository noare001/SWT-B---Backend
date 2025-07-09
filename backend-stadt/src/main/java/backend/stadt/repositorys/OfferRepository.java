package backend.stadt.repositorys;

import backend.stadt.modells.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    @Override
    List<Offer> findAll();

    @Query("""
    SELECT DISTINCT o FROM Offer o
    LEFT JOIN FETCH o.offerTypes
    LEFT JOIN FETCH o.targetGroups
    LEFT JOIN FETCH o.filters
    LEFT JOIN FETCH o.eventSchedule
    LEFT JOIN FETCH o.languages
    LEFT JOIN FETCH o.provider
""")
    List<Offer> findAllFullyLoaded();


    @Query("""
    SELECT o FROM Offer o
    LEFT JOIN FETCH o.offerTypes
    LEFT JOIN FETCH o.targetGroups
    LEFT JOIN FETCH o.filters
    LEFT JOIN FETCH o.eventSchedule
    LEFT JOIN FETCH o.languages
    LEFT JOIN FETCH o.provider
    WHERE o.offerId = :id
""")
    Offer findFullyLoaded(@Param("id") int id);

}
