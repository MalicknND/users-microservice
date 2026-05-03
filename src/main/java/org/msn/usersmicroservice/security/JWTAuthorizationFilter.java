package org.msn.usersmicroservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Filtre exécuté à chaque requête HTTP.
 * Son rôle est de :
 * - vérifier la présence d’un JWT
 * - valider ce JWT
 * - authentifier l’utilisateur dans le contexte Spring Security
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 🔹 1. Récupération du header Authorization
        String jwt = request.getHeader("Authorization");

        // 🔹 2. Vérification du format du token
        // Si le token est absent ou ne commence pas par "Bearer "
        // on laisse passer la requête sans authentification
        if (jwt == null || !jwt.startsWith(SecParams.PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 3. Création du vérificateur JWT avec la clé secrète
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(SecParams.SECRET))
                .build();

        // 🔹 4. Suppression du préfixe "Bearer "
        jwt = jwt.substring(SecParams.PREFIX.length());

        // 🔹 5. Vérification et décodage du token
        // Vérifie signature + expiration
        DecodedJWT decodedJWT = verifier.verify(jwt);

        // 🔹 6. Extraction du username (subject)
        String username = decodedJWT.getSubject();

        // 🔹 7. Extraction des rôles depuis le token
        List<String> roles = decodedJWT
                .getClaims()
                .get("roles")
                .asList(String.class);

        // 🔹 8. Conversion des rôles en authorities Spring Security
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String r : roles) {
            authorities.add(new SimpleGrantedAuthority(r));
        }

        // 🔹 9. Création de l'objet d'authentification
        // password = null car déjà authentifié via JWT
        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // 🔥 10. Injection dans le contexte de sécurité
        // Permet à Spring de considérer l’utilisateur comme connecté
        SecurityContextHolder.getContext().setAuthentication(user);

        // 🔹 11. Continuer la chaîne de filtres (vers le controller)
        filterChain.doFilter(request, response);
    }
}