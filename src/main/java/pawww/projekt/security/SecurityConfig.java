package pawww.projekt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/api/projekty", "/projekty", "/", "/ranking", "/projekty/*/poparcie").permitAll()
                        .requestMatchers("/admin/uzytkownicy/**").hasRole("ADMINISTRATOR")
                        .requestMatchers("/urzednik/**").hasRole("URZEDNIK")
                        .requestMatchers("/admin/kategorie/**", "/admin/osiedla/**").hasAnyRole("URZEDNIK", "ADMINISTRATOR")
                        .requestMatchers("/projekty/edytuj/**", "/projekty/usun/**").hasAnyRole("URZEDNIK", "ADMINISTRATOR")
                        .requestMatchers("/koszyk/**", "/projekty/nowy").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/projekty", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/projekty")
                        .permitAll()
                );

        return http.build();
    }
}