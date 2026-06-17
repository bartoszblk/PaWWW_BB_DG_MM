package pawww.projekt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.UzytkownikRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordMigrationRunner implements CommandLineRunner {

    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.findAll();

        for (Uzytkownik u : uzytkownicy) {
            if (u.getHaslo() != null && !u.getHaslo().startsWith("$2a$")) {
                String zahashowaneHaslo = passwordEncoder.encode(u.getHaslo());
                u.setHaslo(zahashowaneHaslo);
                uzytkownikRepository.save(u);
            }
        }
    }
}