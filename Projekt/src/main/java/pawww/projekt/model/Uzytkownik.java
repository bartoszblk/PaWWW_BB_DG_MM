package pawww.projekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Uzytkownicy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uzytkownik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uzytkownika")
    private Long id;

    @NotBlank(message = "Login jest wymagany.")
    @Size(min = 3, max = 50, message = "Login musi mieć od 3 do 50 znaków.")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Hasło jest wymagane.")
    @Column(name = "haslo", nullable = false)
    private String haslo;

    @NotBlank(message = "PESEL jest wymagany.")
    @Pattern(regexp = "^\\d{11}$", message = "PESEL musi składać sie dokładnie z 11 cyfr.")
    @Column(name = "pesel", nullable = false, unique = true, length = 11)
    private String pesel;

    @NotBlank(message = "Imię jest wymagane.")
    @Size(max = 50, message = "Imię może mieć maksymalnie 50 znaków.")
    @Column(name = "imie", nullable = false)
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane.")
    @Size(max = 50, message = "Nazwisko może mieć maksymalnie 50 znaków.")
    @Column(name = "nazwisko", nullable = false)
    private String nazwisko;

    @Column(name = "id_roli", nullable = false)
    private Long idRoli;
}