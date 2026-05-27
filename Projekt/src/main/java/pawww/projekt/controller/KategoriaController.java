package pawww.projekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pawww.projekt.repository.KategoriaRepository;

@Controller
@RequiredArgsConstructor
public class KategoriaController {

    private final KategoriaRepository kategoriaRepository;

    @GetMapping("/kategorie")
    public String wyswietlKategorie(Model model) {
        model.addAttribute("listaKategorii", kategoriaRepository.findAll());
        return "kategorie";
    }
}
