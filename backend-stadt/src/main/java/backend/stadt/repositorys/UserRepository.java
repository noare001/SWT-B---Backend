package backend.stadt.repositorys;

import backend.stadt.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> getAppUserByNameAndPassword(String name, String password);
    boolean existsByName(String username);

    AppUser getById(Integer id);

    List<AppUser> findAllByProviderId(Integer providerId);
}
