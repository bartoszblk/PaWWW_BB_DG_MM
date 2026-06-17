package pawww.projekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.UzytkownikRepository;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UzytkownikRepository uzytkownikRepository;

    @ModelAttribute("zalogowanyUzytkownik")
    public Uzytkownik dodajZalogowanegoUzytkownika(Principal principal) {
        if (principal != null) {
            return uzytkownikRepository.findByLogin(principal.getName()).orElse(null);
        }
        return null;
    }
}