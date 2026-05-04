package org.msn.usersmicroservice.service;

import jakarta.transaction.Transactional;
import org.msn.usersmicroservice.entities.Role;
import org.msn.usersmicroservice.entities.User;
import org.msn.usersmicroservice.entities.VerificationToken;
import org.msn.usersmicroservice.exceptions.EmailAlreadyExistsException;
import org.msn.usersmicroservice.register.RegistrationRequest;
import org.msn.usersmicroservice.repos.RoleRepository;
import org.msn.usersmicroservice.repos.UserRepository;
import org.msn.usersmicroservice.repos.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional // veut dire q
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRole(rolename);

        user.getRoles().add(role);
//        userRepository.save(user); on a mis @Transactional pour eviter de faire le save a chaque fois
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email déja existant");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setEnabled(false);
        userRepository.save(newUser);
//ajouter à newUser le role par défaut USER
        Role r = roleRepository.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        newUser.setRoles(roles);
        userRepository.save(newUser);

        //génére le code secret
        String code = this.generateCode();
        VerificationToken token = new VerificationToken(code, newUser);
        verificationTokenRepository.save(token);

        return newUser;
    }

    private String generateCode() {
        Random random = new Random();
        Integer code = 100000 + random.nextInt(900000);
        return code.toString();
    }
}
