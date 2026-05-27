package pawww.projekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "Projekty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projekt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projektu")
    private Long id;

    @NotBlank(message = "Tytuł jest wymagany.")
    @Size(min = 5, max = 150, message = "Tytuł musi mieć od 5 do 150 znaków.")
    @Column(name = "tytul", nullable = false)
    private String title;

    @NotBlank(message = "Opis jest wymagany.")
    @Size(min = 10, max = 2000, message = "Opis musi mieć od 10 do 2000 znaków.")
    @Column(name = "opis", nullable = false)
    private String description;

    @NotNull(message = "Szacowany koszt jest wymagany.")
    @Min(value = 1, message = "Koszt projektu musi byc większy od 0.")
    @Column(name = "szacowany_koszt", nullable = false)
    private Double szacowanyKoszt;

    @NotNull(message = "Data rozpoczęcia jest wymagana.")
    @FutureOrPresent(message = "Data rozpoczęcia nie może byc w przeszłości.")
    @Column(name = "data_rozpoczecia")
    private LocalDate dataRozpoczecia;

    @Column(name = "data_zakonczenia")
    private LocalDate dataZakonczenia;

    @NotNull(message = "Wybierz typ głosowania.")
    @Column(name = "czy_tajne")
    private Integer czyTajne = 0;

    @NotNull(message = "Wybranie kategorii jest obowiązkowe.")
    @ManyToOne
    @JoinColumn(name = "id_kategorii", nullable = false)
    private Kategoria kategoria;

    @NotNull(message = "Wybranie osiedla jest obowiązkowe.")
    @ManyToOne
    @JoinColumn(name = "id_osiedla", nullable = false)
    private Osiedle osiedle;

    @ManyToOne
    @JoinColumn(name = "id_uzytkownika_zgl", nullable = false)
    private Uzytkownik uzytkownikZgl;

    @Column(name = "id_statusu", nullable = false)
    private Long idStatusu = 41L;
}