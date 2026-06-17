package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.RankingProjektow;

public interface RankingProjektowRepository extends JpaRepository<RankingProjektow, Long> {
}