package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.Kategoria;

public interface KategoriaRepository extends JpaRepository<Kategoria, Long> {
}