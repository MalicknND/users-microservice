package org.msn.usersmicroservice;

import jakarta.annotation.PostConstruct;
import org.msn.usersmicroservice.entities.Role;
import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UsersMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersMicroserviceApplication.class, args);
    }

    @Autowired
    UserService userService;
/*
    @PostConstruct
    void init() {
        // ajouter les roles et les utilisateurs par défaut
        userService.addRole(new Role(null, "USER"));
        userService.addRole(new Role(null, "ADMIN"));

        // ajouter les users
        userService.saveUser(new User(null, "admin", "123", true, null));
        userService.saveUser(new User(null, "malick", "123", true, null));
        userService.saveUser(new User(null, "bousso", "123", true, null));

        //ajouter les rôles aux users
        userService.addRoleToUser("admin", "ADMIN");
        userService.addRoleToUser("admin", "USER");
        userService.addRoleToUser("malick", "USER");
        userService.addRoleToUser("bousso", "USER");
    }

 */

    @Bean
    BCryptPasswordEncoder getBCE() {
        return new BCryptPasswordEncoder();
    }

}
