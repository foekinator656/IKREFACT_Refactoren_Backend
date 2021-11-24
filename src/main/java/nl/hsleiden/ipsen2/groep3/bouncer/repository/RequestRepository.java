package nl.hsleiden.ipsen2.groep3.bouncer.repository;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * the Class RequestRepository is used to find a request based on the username from that request
 */

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findRequestByRequestedByUsername(String username);
    Request findFirstByRequestedByUsername(String username);
}