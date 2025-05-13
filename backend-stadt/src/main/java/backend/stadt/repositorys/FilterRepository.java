package backend.stadt.repositorys;

import backend.stadt.modells.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    Filter findByOfferId(Integer offerId);
}
