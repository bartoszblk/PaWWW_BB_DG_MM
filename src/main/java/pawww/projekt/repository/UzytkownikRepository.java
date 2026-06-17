package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.Uzytkownik;
import java.util.Optional;

public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Long> {

    Optional<Uzytkownik> findByLogin(String login);

    Optional<Uzytkownik> findByPesel(String pesel);
}