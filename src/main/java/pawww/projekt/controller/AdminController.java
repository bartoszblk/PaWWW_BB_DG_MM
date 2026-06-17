package pawww.projekt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pawww.projekt.model.Kategoria;
import pawww.projekt.model.Osiedle;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.KategoriaRepository;
import pawww.projekt.repository.OsiedleRepository;
import pawww.projekt.repository.RoleRepository;
import pawww.projekt.repository.UzytkownikRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UzytkownikRepository uzytkownikRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final KategoriaRepository kategoriaRepository;
    private final OsiedleRepository osiedleRepository;

    @GetMapping
    public String panelAdmina() {
        return "admin-panel";
    }

    @GetMapping("/uzytkownicy")
    public String listaUzytkownikow(Model model) {
        model.addAttribute("uzytkownicy", uzytkownikRepository.findAll());

        java.util.Map<Long, String> roleMap = roleRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(
                        pawww.projekt.model.Role::getIdRoli,
                        pawww.projekt.model.Role::getNazwa
                ));
        model.addAttribute("roleMap", roleMap);

        return "admin-uzytkownicy";
    }

    @GetMapping("/uzytkownicy/formularz")
    public String formularzUzytkownika(@RequestParam(required = false) Long id, Model model) {
        Uzytkownik uzytkownik = (id != null)
                ? uzytkownikRepository.findById(id).orElseThrow()
                : new Uzytkownik();

        model.addAttribute("uzytkownik", uzytkownik);
        model.addAttribute("role", roleRepository.findAll());
        return "admin-uzytkownik-form";
    }

    @PostMapping("/uzytkownicy/zapisz")
    public String zapiszUzytkownika(@Valid @ModelAttribute("uzytkownik") Uzytkownik uzytkownik, BindingResult result, Model model) {

        if (uzytkownikRepository.findByLogin(uzytkownik.getLogin())
                .filter(u -> !u.getId().equals(uzytkownik.getId())).isPresent()) {
            result.rejectValue("login", "error.login", "Podany login jest już zajęty.");
        }

        if (uzytkownikRepository.findByPesel(uzytkownik.getPesel())
                .filter(u -> !u.getId().equals(uzytkownik.getId())).isPresent()) {
            result.rejectValue("pesel", "error.pesel", "Ten PESEL przypisano już do innego konta.");
        }

        if (result.hasErrors()) {
            model.addAttribute("role", roleRepository.findAll());
            return "admin-uzytkownik-form";
        }

        if (uzytkownik.getId() == null || (uzytkownik.getHaslo() != null && !uzytkownik.getHaslo().startsWith("$2a$"))) {
            uzytkownik.setHaslo(passwordEncoder.encode(uzytkownik.getHaslo()));
        }

        uzytkownikRepository.save(uzytkownik);
        return "redirect:/admin/uzytkownicy";
    }

    @PostMapping("/uzytkownicy/usun/{id}")
    public String usunUzytkownika(@PathVariable Long id) {
        uzytkownikRepository.deleteById(id);
        return "redirect:/admin/uzytkownicy";
    }

    @GetMapping("/kategorie")
    public String listaKategorii(Model model) {
        model.addAttribute("kategorie", kategoriaRepository.findAll());
        return "admin-kategorie";
    }

    @PostMapping("/kategorie/zapisz")
    public String zapiszKategorie(@RequestParam(required = false) Long id, @RequestParam String nazwa) {
        Kategoria k = (id != null) ? kategoriaRepository.findById(id).orElseThrow() : new Kategoria();
        k.setNazwa(nazwa);
        kategoriaRepository.save(k);
        return "redirect:/admin/kategorie";
    }

    @PostMapping("/kategorie/usun/{id}")
    public String usunKategorie(@PathVariable Long id) {
        kategoriaRepository.deleteById(id);
        return "redirect:/admin/kategorie";
    }

    @GetMapping("/osiedla")
    public String listaOsiedli(Model model) {
        model.addAttribute("osiedla", osiedleRepository.findAll());
        return "admin-osiedla";
    }

    @PostMapping("/osiedla/zapisz")
    public String zapiszOsiedle(@RequestParam(required = false) Long id, @RequestParam String nazwa) {
        Osiedle o = (id != null) ? osiedleRepository.findById(id).orElseThrow() : new Osiedle();
        o.setNazwa(nazwa);
        osiedleRepository.save(o);
        return "redirect:/admin/osiedla";
    }

    @PostMapping("/osiedla/usun/{id}")
    public String usunOsiedle(@PathVariable Long id) {
        osiedleRepository.deleteById(id);
        return "redirect:/admin/osiedla";
    }
}