package pawww.projekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pawww.projekt.repository.RankingProjektowRepository;

@Controller
@RequiredArgsConstructor
public class RankingController {

    private final RankingProjektowRepository rankingProjektowRepository;

    @GetMapping("/ranking")
    public String wyswietlRanking(Model model) {
        model.addAttribute("ranking", rankingProjektowRepository.findAll());
        return "ranking";
    }
}