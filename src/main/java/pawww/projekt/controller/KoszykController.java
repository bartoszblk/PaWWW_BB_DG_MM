package pawww.projekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pawww.projekt.model.Glos;
import pawww.projekt.model.Projekt;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.GlosRepository;
import pawww.projekt.repository.ProjektRepository;
import pawww.projekt.repository.UzytkownikRepository;
import pawww.projekt.session.KoszykGlosow;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class KoszykController {

    private final KoszykGlosow koszykGlosow;
    private final ProjektRepository projektRepository;
    private final GlosRepository glosRepository;
    private final UzytkownikRepository uzytkownikRepository;

    @PostMapping("/koszyk/dodaj")
    public String dodajDoKoszyka(@RequestParam Long projektId, @RequestParam String decyzja, Principal principal, RedirectAttributes redirectAttributes) {

        Uzytkownik uzytkownik = uzytkownikRepository.findByLogin(principal.getName()).orElseThrow();
        Projekt projekt = projektRepository.findById(projektId).orElseThrow();

        if (glosRepository.existsByUzytkownikAndProjekt(uzytkownik, projekt)) {
            redirectAttributes.addFlashAttribute("komunikatBlad", "Błąd: Już wcześniej oddałeś swój głos na ten projekt.");
            return "redirect:/projekty";
        }

        if (koszykGlosow.getGlosy().containsKey(projektId)) {
            koszykGlosow.dodajGlos(projektId, decyzja);
            redirectAttributes.addFlashAttribute("komunikat", "Zmieniono głos w koszyku na: " + decyzja);
            return "redirect:/projekty";
        }

        koszykGlosow.dodajGlos(projektId, decyzja);
        redirectAttributes.addFlashAttribute("komunikat", "Pomyślnie dodano głos " + decyzja + " na projekt do koszyka.");
        return "redirect:/projekty";
    }

    @PostMapping("/koszyk/usun")
    public String usunZKoszyka(@RequestParam Long projektId) {
        koszykGlosow.usunGlos(projektId);
        return "redirect:/koszyk";
    }

    @GetMapping("/koszyk")
    public String pokazKoszyk(Model model) {
        Map<Projekt, String> zawartosc = new HashMap<>();
        for (Map.Entry<Long, String> wpis : koszykGlosow.getGlosy().entrySet()) {
            projektRepository.findById(wpis.getKey()).ifPresent(p -> zawartosc.put(p, wpis.getValue()));
        }
        model.addAttribute("zawartoscKoszyka", zawartosc);
        return "koszyk";
    }

    @PostMapping("/koszyk/zatwierdz")
    public String zatwierdzKoszyk(Principal principal, RedirectAttributes redirectAttributes) {
        Uzytkownik uzytkownik = uzytkownikRepository.findByLogin(principal.getName()).orElseThrow();

        for (Map.Entry<Long, String> wpis : koszykGlosow.getGlosy().entrySet()) {
            Projekt projekt = projektRepository.findById(wpis.getKey()).orElseThrow();

            Glos glos = new Glos();
            glos.setProjekt(projekt);
            glos.setUzytkownik(uzytkownik);
            glos.setRodzajGlosu(wpis.getValue());
            glos.setDataOddania(LocalDateTime.now());

            try {
                glosRepository.save(glos);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("komunikatBlad", "Błąd: Oddałeś już głos na jeden z tych projektów!");
                return "redirect:/koszyk";
            }
        }

        koszykGlosow.wyczysc();
        redirectAttributes.addFlashAttribute("komunikatSukces", "Twoje głosy zostały pomyślnie zapisane w systemie!");
        return "redirect:/projekty";
    }
}