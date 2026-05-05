package org.msn.usersmicroservice.security;

import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
// 🔹 Cette classe permet à Spring Security de charger un utilisateur depuis la DB
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;
    // 👉 service pour récupérer l'utilisateur en base

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 🔹 1. récupérer l'utilisateur depuis la base
        User user = userService.findByUsername(username);

        // 🔹 2. si l'utilisateur n'existe pas → erreur
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }

        // 🔹 3. transformer les rôles en authorities (format Spring Security)
        List<GrantedAuthority> auths = new ArrayList<>();

        user.getRoles().forEach(role -> {
            GrantedAuthority authority =
                    new SimpleGrantedAuthority(role.getRole());
            auths.add(authority);
        });

        // 🔹 4. retourner un objet UserDetails (format attendu par Spring)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),   // username
                user.getPassword(),   // password (déjà hashé)
                user.getEnabled(),
                true,
                true,
                true,
                auths                 // rôles / permissions
        );
    }
}