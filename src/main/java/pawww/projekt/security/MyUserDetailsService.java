package pawww.projekt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pawww.projekt.model.Role;
import pawww.projekt.model.Uzytkownik;
import pawww.projekt.repository.RoleRepository;
import pawww.projekt.repository.UzytkownikRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UzytkownikRepository uzytkownikRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Uzytkownik u = uzytkownikRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Role rola = roleRepository.findById(u.getIdRoli()).orElseThrow();

        return User.builder()
                .username(u.getLogin())
                .password(u.getHaslo())
                .roles(rola.getNazwa())
                .build();
    }
}