package nl.hsleiden.ipsen2.groep3.bouncer.repository;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Role;
import nl.hsleiden.ipsen2.groep3.bouncer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * the Class UserRepository is used to find to usename that is the same on the database
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByUsernameEquals(String username);
    List<User> findUserByRoleGreaterThanAndRoleLessThanEqual (Role role, Role roleMax);
}
