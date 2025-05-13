package backend.stadt.repositorys;

import backend.stadt.modells.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<Languages, Long> {
    Languages findByOfferId(Integer offerId);
}
