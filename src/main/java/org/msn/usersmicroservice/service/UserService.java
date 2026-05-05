package org.msn.usersmicroservice.service;

import org.msn.usersmicroservice.entities.Role;
import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.register.RegistrationRequest;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    List<User> findAllUsers();

    User registerUser(RegistrationRequest request);
    public void sendEmailUser(User u, String code);

    public User validateToken(String code);
}
