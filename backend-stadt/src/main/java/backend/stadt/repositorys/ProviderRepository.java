package backend.stadt.repositorys;

import backend.stadt.modells.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    @Override
    List<Provider> findAll();

    Provider findById(Integer Id);
}
