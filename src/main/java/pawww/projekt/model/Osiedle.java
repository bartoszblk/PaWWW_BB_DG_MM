package pawww.projekt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Osiedla")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Osiedle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_osiedla")
    private Long idOsiedla;

    @Column(name = "nazwa", nullable = false, unique = true)
    private String nazwa;
}