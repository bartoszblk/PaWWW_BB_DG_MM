package pawww.projekt.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Kategorie_Projektow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kategorii")
    private Long idKategorii;

    @NotBlank(message = "Nazwa jest wymagana.")
    @Size(min = 3, max = 20, message = "Nazwa musi mieć od 3 do 20 znaków.")
    @Column(name = "nazwa", nullable = false, unique = true)
    private String nazwa;
}