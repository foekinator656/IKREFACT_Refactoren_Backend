package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Role;
import nl.hsleiden.ipsen2.groep3.bouncer.model.User;
import nl.hsleiden.ipsen2.groep3.bouncer.model.UserAccount;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.UserRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@EnableWebSecurity
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> index () {
        List<User> users = null;

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role role = ((User) userPrincipal.getUser()).getRole();

        if (role == Role.WORKER || role == Role.MODERATOR) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (role == Role.SITE_ADMIN) {
            users = this.userRepository.findUserByRoleGreaterThanAndRoleLessThanEqual(Role.WORKER, Role.SITE_ADMIN);
        } else if (role == Role.ADMIN) {
            users = this.userRepository.findUserByRoleGreaterThanAndRoleLessThanEqual(Role.WORKER, Role.ADMIN);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show (@PathVariable("id") Long id) {
        Optional<User> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<User> update (
            @PathVariable("id") Long id,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email) {
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserAccount userAccount = (UserAccount) optionalUser.get();

        System.out.println(firstName);

        userAccount.setFirstName(firstName);
        userAccount.setLastName(lastName);
        userAccount.setEmail(email);

        userAccount = this.userRepository.save(userAccount);

        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }
}
