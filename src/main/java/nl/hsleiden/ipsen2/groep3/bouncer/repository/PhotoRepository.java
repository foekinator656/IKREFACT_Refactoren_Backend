package nl.hsleiden.ipsen2.groep3.bouncer.repository;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByFilename (String fiilename);
}
