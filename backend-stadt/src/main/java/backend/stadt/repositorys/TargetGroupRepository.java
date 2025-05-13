package backend.stadt.repositorys;

import backend.stadt.modells.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetGroupRepository extends JpaRepository<TargetGroup, Long> {
    TargetGroup findByOfferId(Integer offerId);
}
