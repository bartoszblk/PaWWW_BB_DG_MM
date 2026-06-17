package pawww.projekt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "V_Ranking_Projektow")
@Immutable
@Data
public class RankingProjektow {

    @Id
    @Column(name = "id_projektu")
    private Long idProjektu;

    @Column(name = "tytul")
    private String tytul;

    @Column(name = "kategoria")
    private String kategoria;

    @Column(name = "osiedle")
    private String osiedle;

    @Column(name = "liczba_glosow")
    private Long liczbaGlosow;
}