package org.msn.usersmicroservice.security;

import org.springframework.beans.factory.annotation.Autowired; // ✅ AJOUT
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // ✅ AJOUT
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;
    // ✅ AJOUT : nécessaire pour gérer le login (authentification)

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception
    {
        http.sessionManagement( session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Collections.singletonList("Authorization"));
                    return config;
                }))

                .authorizeHttpRequests( requests -> requests

                        // ✅ AJOUT : autoriser le login (sinon impossible de récupérer un token)
                        .requestMatchers("/login", "/register", "verifyEmail").permitAll()

                        // tes routes protégées
                        .requestMatchers("/api/all/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET,"/api/getbyid/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,"/api/addprod/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/updateprod/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/delprod/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )

                // ✅ AJOUT : filtre de login (génère le JWT)
                .addFilterBefore(
                        new JWTAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                )

                // ✅ déjà présent : vérifie le JWT
                .addFilterBefore(
                        new JWTAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}