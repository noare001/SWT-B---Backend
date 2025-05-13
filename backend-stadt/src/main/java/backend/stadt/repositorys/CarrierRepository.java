package backend.stadt.repositorys;

import backend.stadt.modells.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    @Override
    List<Carrier> findAll();

    Carrier findById(Integer Id);
}
