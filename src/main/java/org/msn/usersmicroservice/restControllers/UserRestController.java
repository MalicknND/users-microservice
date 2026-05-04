package org.msn.usersmicroservice.restControllers;

import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*") // autorise les requêtes de n'importe quelle origine (utile pour le développement, à restreindre en production)
public class UserRestController {
    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
}
