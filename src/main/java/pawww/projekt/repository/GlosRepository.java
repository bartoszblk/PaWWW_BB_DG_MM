package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.Glos;
import pawww.projekt.model.Projekt;
import pawww.projekt.model.Uzytkownik;
import java.util.List;

public interface GlosRepository extends JpaRepository<Glos, Long> {
    boolean existsByUzytkownikAndProjekt(Uzytkownik uzytkownik, Projekt projekt);
    List<Glos> findByProjekt(Projekt projekt);

    List<Glos> findByUzytkownik(Uzytkownik uzytkownik);
}