package pawww.projekt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pawww.projekt.model.Projekt;
import pawww.projekt.repository.KategoriaRepository;
import pawww.projekt.repository.OsiedleRepository;
import pawww.projekt.repository.ProjektRepository;
import pawww.projekt.repository.UzytkownikRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjektController {

    private final ProjektRepository projektRepository;
    private final KategoriaRepository kategoriaRepository;
    private final OsiedleRepository osiedleRepository;
    private final UzytkownikRepository uzytkownikRepository;

    @GetMapping("/projekty")
    public String wyswietlProjekty(Model model) {
        List<Projekt> projekty = projektRepository.findAll();
        model.addAttribute("listaProjektow", projekty);
        return "projekty";
    }

    @GetMapping("/projekty/nowy")
    public String pokazFormularzNowegoProjektu(Model model) {
        model.addAttribute("projekt", new Projekt());
        model.addAttribute("listaKategorii", kategoriaRepository.findAll());
        model.addAttribute("listaOsiedli", osiedleRepository.findAll());
        return "projekt-form";
    }

    @PostMapping("/projekty/nowy")
    public String zapiszProjekt(@Valid @ModelAttribute("projekt") Projekt projekt,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaKategorii", kategoriaRepository.findAll());
            model.addAttribute("listaOsiedli", osiedleRepository.findAll());
            return "projekt-form";
        }

        if (projekt.getUzytkownikZgl() == null) {
            projekt.setUzytkownikZgl(uzytkownikRepository.findById(1L).orElse(null));
        }

        projektRepository.save(projekt);
        return "redirect:/projekty";
    }
}