package nl.hsleiden.ipsen2.groep3.bouncer.service;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Role;
import nl.hsleiden.ipsen2.groep3.bouncer.model.Worker;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.UserRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * manages the authentication of users.
 * the Login Functionality depends on the Authorization service.
 *
 * @author Youp van Leeuwen
 */

@Service
public class AuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrincipal(this.userRepository.findFirstByUsernameEquals(username));
    }

    public UserDetails loadByUID(String uid) throws UsernameNotFoundException {
        Worker worker = (Worker) this.userRepository.findFirstByUsernameEquals(uid);

        if (worker == null) {
            worker = new Worker();
            worker.setUid(uid);
            worker.setUsername(uid);
            worker.setRole(Role.WORKER);
            worker = this.userRepository.save(worker);
        }

        return new UserPrincipal(worker);
    }
}
