package org.msn.usersmicroservice.restControllers;

import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.register.RegistrationRequest;
import org.msn.usersmicroservice.repos.UserRepository;
import org.msn.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*") // autorise les requêtes de n'importe quelle origine (utile pour le développement, à restreindre en production)
public class UserRestController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request) {
        return userService.registerUser(request);
    }
}
