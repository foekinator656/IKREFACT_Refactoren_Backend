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

    @PostMapping("/login/uid")
    public ResponseEntity<AuthenticationResponse> loginWithUID(@ModelAttribute AuthenticationRequestWithUID authenticationRequest) {
        final UserDetails userDetails = userDetailsService.loadByUID(authenticationRequest.getUid());

        final String jwt = this.jwtService.generateToken(userDetails);
        Worker worker = (Worker) this.userRepository.findFirstByUsernameEquals(authenticationRequest.getUid());

        return new ResponseEntity<>(new AuthenticationResponse(jwt, worker), HttpStatus.OK);
    }
}
