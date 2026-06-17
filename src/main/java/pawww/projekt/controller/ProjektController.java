package pawww.projekt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import pawww.projekt.model.Glos;
import pawww.projekt.model.Projekt;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProjektController {

    private final ProjektRepository projektRepository;
    private final KategoriaRepository kategoriaRepository;
    private final OsiedleRepository osiedleRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final StatusRepository statusRepository;
    private final GlosRepository glosRepository;

    @GetMapping("/projekty")
    public String wyswietlProjekty(
            @RequestParam(required = false) Long kategoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataOd,
            @RequestParam(required = false) String sort,
            Model model,
            java.security.Principal principal) {

        List<Projekt> projekty = projektRepository.findAll();

        if (kategoriaId != null) {
            projekty = projekty.stream().filter(p -> p.getKategoria().getIdKategorii().equals(kategoriaId)).collect(Collectors.toList());
        }

        if (dataOd != null) {
            projekty = projekty.stream().filter(p -> p.getDataRozpoczecia() != null && !p.getDataRozpoczecia().isBefore(dataOd)).collect(Collectors.toList());
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "koszt_asc": projekty.sort(Comparator.comparing(Projekt::getSzacowanyKoszt, Comparator.nullsLast(Comparator.naturalOrder()))); break;
                case "koszt_desc": projekty.sort(Comparator.comparing(Projekt::getSzacowanyKoszt, Comparator.nullsLast(Comparator.naturalOrder())).reversed()); break;
                case "data_asc": projekty.sort(Comparator.comparing(Projekt::getDataRozpoczecia, Comparator.nullsLast(Comparator.naturalOrder()))); break;
                case "data_desc": projekty.sort(Comparator.comparing(Projekt::getDataRozpoczecia, Comparator.nullsLast(Comparator.naturalOrder())).reversed()); break;
            }
        } else {
            projekty.sort(Comparator.comparingInt(p -> {
                Long sId = p.getIdStatusu();
                if (sId == 4L) return 1;
                if (sId == 2L) return 2;
                if (sId == 1L) return 3;
                if (sId == 5L) return 4;
                if (sId == 3L) return 5;
                return 6;
            }));
        }

        Map<Long, Long> statystykiZa = new HashMap<>();
        Map<Long, Long> statystykiPrzeciw = new HashMap<>();
        for (Projekt p : projekty) {
            if (p.getIdStatusu() == 5L) {
                List<Glos> glosy = glosRepository.findByProjekt(p);
                statystykiZa.put(p.getId(), glosy.stream().filter(g -> "ZA".equals(g.getRodzajGlosu())).count());
                statystykiPrzeciw.put(p.getId(), glosy.stream().filter(g -> "PRZECIW".equals(g.getRodzajGlosu())).count());
            }
        }

        Set<Long> zaglosowaneProjekty = new HashSet<>();
        if (principal != null) {
            Uzytkownik u = uzytkownikRepository.findByLogin(principal.getName()).orElse(null);
            if (u != null) {
                glosRepository.findByUzytkownik(u).forEach(g -> zaglosowaneProjekty.add(g.getProjekt().getId()));
            }
        }

        model.addAttribute("listaProjektow", projekty);
        model.addAttribute("listaKategorii", kategoriaRepository.findAll());
        model.addAttribute("wybranaKategoria", kategoriaId);
        model.addAttribute("wybranaDataOd", dataOd);
        model.addAttribute("wybraneSortowanie", sort);
        model.addAttribute("statystykiZa", statystykiZa);
        model.addAttribute("statystykiPrzeciw", statystykiPrzeciw);
        model.addAttribute("zaglosowaneProjekty", zaglosowaneProjekty);

        return "projekty";
    }

    @GetMapping("/projekty/nowy")
    public String pokazFormularzNowegoProjektu(Model model) {
        model.addAttribute("projekt", new Projekt());
        model.addAttribute("listaKategorii", kategoriaRepository.findAll());
        model.addAttribute("listaOsiedli", osiedleRepository.findAll());
        model.addAttribute("listaStatusow", statusRepository.findAll());
        return "projekt-form";
    }

    @PostMapping("/projekty/nowy")
    public String zapiszProjekt(@Valid @ModelAttribute("projekt") Projekt projekt, BindingResult bindingResult, Model model, java.security.Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaKategorii", kategoriaRepository.findAll());
            model.addAttribute("listaOsiedli", osiedleRepository.findAll());
            model.addAttribute("listaStatusow", statusRepository.findAll());
            return "projekt-form";
        }

        Uzytkownik zalogowanyUzytkownik = uzytkownikRepository.findByLogin(principal.getName()).orElseThrow();
        projekt.setUzytkownikZgl(zalogowanyUzytkownik);

        projekt.setIdStatusu(1L);
        projekt.setDataRozpoczecia(null);
        projekt.setDataZakonczenia(null);

        projektRepository.save(projekt);
        return "redirect:/projekty";
    }

    @GetMapping("/projekty/edytuj/{id}")
    public String edytujProjekt(@PathVariable Long id, org.springframework.ui.Model model) {
        Projekt projekt = projektRepository.findById(id).orElseThrow();
        model.addAttribute("projekt", projekt);
        model.addAttribute("listaKategorii", kategoriaRepository.findAll());
        model.addAttribute("listaOsiedli", osiedleRepository.findAll());
        model.addAttribute("listaStatusow", statusRepository.findAll());
        return "projekt-form";
    }

    @PostMapping("/projekty/edytuj/{id}")
    public String zapiszEdycjeProjektu(@PathVariable Long id, @Valid @ModelAttribute("projekt") Projekt projekt, BindingResult bindingResult, Model model, Authentication authentication) {
        Projekt istniejacy = projektRepository.findById(id).orElseThrow();
        boolean isUrzednik = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_URZEDNIK"));

        if (isUrzednik) {
            if (projekt.getDataRozpoczecia() != null && projekt.getDataZakonczenia() != null) {
                if (projekt.getDataZakonczenia().isBefore(projekt.getDataRozpoczecia())) {
                    bindingResult.rejectValue("dataZakonczenia", "error.dataZakonczenia", "Data zakończenia nie może być wcześniejsza niż data rozpoczęcia.");
                }
            }
            if (istniejacy.getIdStatusu() == 1L && projekt.getIdStatusu() == 1L) {
                bindingResult.rejectValue("idStatusu", "error.idStatusu", "Rozpatrując projekt musisz zmienić jego status.");
            }
            if (projekt.getIdStatusu() == 5L && istniejacy.getIdStatusu() != 4L && istniejacy.getIdStatusu() != 5L) {
                bindingResult.rejectValue("idStatusu", "error.idStatusu", "Zakończyć można wyłącznie projekt, który był w fazie Głosowania.");
            }
            if (projekt.getIdStatusu() == 2L && projekt.getDataRozpoczecia() == null) {
                bindingResult.rejectValue("dataRozpoczecia", "error.dataRozpoczecia", "Przy zatwierdzaniu projektu musisz podać datę rozpoczęcia głosowania.");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaKategorii", kategoriaRepository.findAll());
            model.addAttribute("listaOsiedli", osiedleRepository.findAll());
            model.addAttribute("listaStatusow", statusRepository.findAll());
            model.addAttribute("komunikatBlad", "Formularz zawiera błędy, popraw zaznaczone pola na czerwono.");
            return "projekt-form";
        }

        if (isUrzednik) {
            if (projekt.getIdStatusu() == 2L && projekt.getDataZakonczenia() == null) {
                projekt.setDataZakonczenia(projekt.getDataRozpoczecia().plusDays(30));
            } else if (projekt.getIdStatusu() == 3L) {
                projekt.setDataRozpoczecia(null);
                projekt.setDataZakonczenia(null);
            } else if (projekt.getIdStatusu() == 4L) {
                if (istniejacy.getIdStatusu() != 4L) {
                    projekt.setDataRozpoczecia(LocalDate.now());
                }
                if (projekt.getDataZakonczenia() == null) {
                    projekt.setDataZakonczenia(projekt.getDataRozpoczecia().plusDays(30));
                }
            } else if (projekt.getIdStatusu() == 5L) {
                projekt.setDataRozpoczecia(istniejacy.getDataRozpoczecia());

                if (istniejacy.getIdStatusu() == 4L) {
                    projekt.setDataZakonczenia(LocalDate.now());
                } else {
                    projekt.setDataZakonczenia(istniejacy.getDataZakonczenia());
                }
            }
        } else {
            projekt.setIdStatusu(istniejacy.getIdStatusu());
            projekt.setDataRozpoczecia(istniejacy.getDataRozpoczecia());
            projekt.setDataZakonczenia(istniejacy.getDataZakonczenia());
            projekt.setCzyTajne(istniejacy.getCzyTajne());
        }

        projekt.setId(id);
        projekt.setUzytkownikZgl(istniejacy.getUzytkownikZgl());
        projektRepository.save(projekt);
        return "redirect:/projekty";
    }

    @PostMapping("/projekty/usun/{id}")
    @Transactional
    public String usunProjekt(@PathVariable Long id) {
        projektRepository.usunHistorieProjektu(id);
        projektRepository.usunGlosyProjektu(id);
        projektRepository.deleteById(id);
        return "redirect:/projekty";
    }

    @GetMapping("/projekty/{id}/poparcie")
    public String listaPoparcia(@PathVariable Long id, Model model) {
        Projekt projekt = projektRepository.findById(id).orElseThrow();
        if (projekt.getCzyTajne() == 1) return "redirect:/projekty";
        model.addAttribute("projekt", projekt);
        model.addAttribute("glosy", glosRepository.findByProjekt(projekt));
        return "lista-poparcia";
    }
}