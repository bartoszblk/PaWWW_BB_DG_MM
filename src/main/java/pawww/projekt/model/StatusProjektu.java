package pawww.projekt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Statusy_Projektow")
@Data
public class StatusProjektu {

    @Id
    @Column(name = "id_statusu")
    private Long idStatusu;

    @Column(name = "nazwa")
    private String nazwa;
}