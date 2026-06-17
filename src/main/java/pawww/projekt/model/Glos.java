package pawww.projekt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Glosy")
@Data
public class Glos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_glosu")
    private Long idGlosu;

    @ManyToOne
    @JoinColumn(name = "id_projektu", nullable = false)
    private Projekt projekt;

    @ManyToOne
    @JoinColumn(name = "id_uzytkownika", nullable = false)
    private Uzytkownik uzytkownik;

    @Column(name = "rodzaj_glosu", nullable = false)
    private String rodzajGlosu;

    @Column(name = "data_oddania", nullable = false)
    private LocalDateTime dataOddania;
}