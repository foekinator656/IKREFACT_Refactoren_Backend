package nl.hsleiden.ipsen2.groep3.bouncer.repository;

import nl.hsleiden.ipsen2.groep3.bouncer.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
    Optional<QrCode> findByCode (String code);
}
