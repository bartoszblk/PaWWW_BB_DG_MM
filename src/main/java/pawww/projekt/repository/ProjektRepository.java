package pawww.projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pawww.projekt.model.Projekt;

public interface ProjektRepository extends JpaRepository<Projekt, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM HistoriaStatusow h WHERE h.idProjektu = :id")
    void usunHistorieProjektu(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Glos g WHERE g.projekt.id = :id")
    void usunGlosyProjektu(@Param("id") Long id);
}