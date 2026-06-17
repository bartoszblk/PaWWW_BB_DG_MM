package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.StatusProjektu;

public interface StatusRepository extends JpaRepository<StatusProjektu, Long> {
}