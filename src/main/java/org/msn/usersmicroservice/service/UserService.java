package org.msn.usersmicroservice.service;

import org.msn.usersmicroservice.entities.Role;
import org.msn.usersmicroservice.entities.User;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
}
