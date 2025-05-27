package backend.stadt.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser getAppUserByNameAndPassword(String name, String password);
    boolean existsByName(String username);

}
