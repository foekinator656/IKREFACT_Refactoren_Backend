package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import com.google.zxing.NotFoundException;
import nl.hsleiden.ipsen2.groep3.bouncer.model.QrCode;
import nl.hsleiden.ipsen2.groep3.bouncer.model.Request;
import nl.hsleiden.ipsen2.groep3.bouncer.model.Worker;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.QrCodeRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.RequestRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.UserRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * this is the QrCodeController that generates and validates the QRCodes
 *
 * @author Stef Haring
 * @author Youp van Leeuwen
 */

@RestController
@RequestMapping("/qr")
public class QrCodeController {
    private final RequestRepository requestRepository;
    private final QrCodeRepository qrCodeRepository;
    private final UserRepository userRepository;
    private final QrCodeService qrCodeService;
    private static final int RESPONSE_DELAY = 2;

    public QrCodeController(
            RequestRepository requestRepository,
            QrCodeRepository qrCodeRepository,
            UserRepository userRepository,
            QrCodeService qrCodeService) {
        this.requestRepository = requestRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, String>> generateQRCode() throws Exception {
        try {
            Thread.sleep(1000 * RESPONSE_DELAY);
        } catch (InterruptedException e) {
            throw new Exception(e.getMessage());
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, String> result = new HashMap<>();

        if (!this.canCreateRequest(username)) {
            result.put("error", "You've already requested a QR-Code");
            return new ResponseEntity<>(result, HttpStatus.TOO_MANY_REQUESTS);
        }

        String qrCode = this.createQrCode(username);
        result.put("qrCode", qrCode);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> decodeQrCode (@RequestParam("file") MultipartFile file) {
        try {
            this.qrCodeService.readQrCode(file);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException | IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean canCreateRequest (String username) {
        Request request = this.requestRepository.findFirstByRequestedByUsername(username);

        if (request == null) return true;

        return request.getCreatedAt().isBefore(LocalDateTime.now().minusHours(1));
    }

    private String createQrCode (String username) {
        Worker user = (Worker) userRepository.findFirstByUsernameEquals(username);
        Request request = new Request();
        request.setRequestedBy(user);
        request = this.requestRepository.save(request);

        QrCode qrCode = new QrCode()
                .setCode(qrCodeService.generateCode())
                .setRequest(request)
                .setValidTill(LocalDateTime.now().plusMinutes(15));

        this.qrCodeRepository.save(qrCode);

        return qrCode.getCode();
    }
}
