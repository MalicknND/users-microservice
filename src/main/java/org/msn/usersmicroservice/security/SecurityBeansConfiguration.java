package org.msn.usersmicroservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // indique que cette classe sert à définir des beans Spring
public class SecurityBeansConfiguration {

    @Bean
    public BCryptPasswordEncoder getBCE() {
        return new BCryptPasswordEncoder();
    }
    // 🔹 crée un bean BCryptPasswordEncoder
    // 👉 utilisé pour :
    // - encoder les mots de passe (hash)
    // - vérifier un mot de passe lors du login

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // 🔹 crée un bean AuthenticationManager
    // 👉 c’est le moteur d’authentification de Spring Security
    // 👉 il sert à :
    // - vérifier username + password
    // - appeler UserDetailsService
    // - comparer les mots de passe (avec BCrypt)
}