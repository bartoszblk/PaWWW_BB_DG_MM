package pawww.projekt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawww.projekt.model.Projekt;
import pawww.projekt.repository.ProjektRepository;

import java.util.List;

@RestController
@RequestMapping("/api/projekty")
@RequiredArgsConstructor
public class ProjektRestController {

    private final ProjektRepository projektRepository;

    @GetMapping
    public ResponseEntity<List<Projekt>> getWszystkieProjekty() {
        List<Projekt> projekty = projektRepository.findAll();
        return ResponseEntity.ok(projekty);
    }
}