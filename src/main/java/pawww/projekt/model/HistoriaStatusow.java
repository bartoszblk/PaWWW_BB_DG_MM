package pawww.projekt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Historia_Statusow")
@Data
public class HistoriaStatusow {

    @Id
    @Column(name = "id_historii")
    private Long idHistorii;

    @Column(name = "id_projektu")
    private Long idProjektu;
}