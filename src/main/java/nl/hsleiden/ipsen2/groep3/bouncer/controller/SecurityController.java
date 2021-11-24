package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import nl.hsleiden.ipsen2.groep3.bouncer.model.*;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.UserRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.security.UserPrincipal;
import nl.hsleiden.ipsen2.groep3.bouncer.service.AuthenticationService;
import nl.hsleiden.ipsen2.groep3.bouncer.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * this is the SecurityController used for the Login of the relevant sort user
 *
 * @author Youp van Leeuwen
 * @author Loek Appel
 */

@RestController
@EnableWebSecurity
public class SecurityController {
    private final AuthenticationService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public SecurityController(
            AuthenticationService userDetailsService,
            JwtService jwtService,
            UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@ModelAttribute AuthenticationRequest authenticationRequest, BCryptPasswordEncoder passwordEncoder) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        if (userDetails == null || !passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username/password");
        }

        final String jwt = this.jwtService.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt, (UserAccount) ((UserPrincipal) userDetails).getUser()), HttpStatus.OK);
    }

    @PostMapping("/login/uid")
    public ResponseEntity<AuthenticationResponse> loginWithUID(@ModelAttribute AuthenticationRequestWithUID authenticationRequest) {
        final UserDetails userDetails = userDetailsService.loadByUID(authenticationRequest.getUid());

        final String jwt = this.jwtService.generateToken(userDetails);
        Worker worker = (Worker) this.userRepository.findFirstByUsernameEquals(authenticationRequest.getUid());

        return new ResponseEntity<>(new AuthenticationResponse(jwt, worker), HttpStatus.OK);
    }

    @GetMapping("/register")
    public ResponseEntity<UserAccount> register (BCryptPasswordEncoder passwordEncoder) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("jdoe");
        userAccount.setFirstName("John");
        userAccount.setLastName("Doe");
        userAccount.setEmail("johndoe@gmail.com");
        userAccount.setPassword(passwordEncoder.encode("12345678"));
        userAccount.setRole(Role.MODERATOR);

        userAccount = this.userRepository.save(userAccount);

        return new ResponseEntity<>(userAccount, HttpStatus.CREATED);
    }
}
