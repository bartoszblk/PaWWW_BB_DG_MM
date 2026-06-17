package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pawww.projekt.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}