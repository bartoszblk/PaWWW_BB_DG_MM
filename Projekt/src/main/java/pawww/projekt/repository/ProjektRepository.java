package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.Projekt;

public interface ProjektRepository extends JpaRepository<Projekt, Long> {
}