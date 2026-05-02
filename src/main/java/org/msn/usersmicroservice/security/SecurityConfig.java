package org.msn.usersmicroservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // indique que cette classe contient une configuration Spring
@EnableWebSecurity // active Spring Security dans l'application
public class SecurityConfig {

    @Autowired
    AuthenticationManager authenticationManager;
    // injection du moteur d'authentification (utilisé pour login plus tard)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🔹 1. Gestion des sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 👉 Pas de session côté serveur (mode JWT)
                // chaque requête doit être authentifiée indépendamment

                // 🔹 2. Désactivation CSRF
                .csrf(csrf -> csrf.disable())
                // 👉 inutile pour API REST (utilisé surtout avec cookies)

                // 🔹 3. Gestion des autorisations
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers("/login").permitAll()
                                // 👉 route publique (pas besoin d’être connecté)

                                .anyRequest().authenticated()
                        // 👉 toutes les autres routes nécessitent une authentification
                );

        // 🔹 construit et retourne la configuration de sécurité
        return http.build();
    }
}