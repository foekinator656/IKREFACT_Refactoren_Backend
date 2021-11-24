package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import com.google.zxing.NotFoundException;
import nl.hsleiden.ipsen2.groep3.bouncer.exception.InvalidFileTypeException;
import nl.hsleiden.ipsen2.groep3.bouncer.model.*;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.*;
import nl.hsleiden.ipsen2.groep3.bouncer.security.UserPrincipal;
import nl.hsleiden.ipsen2.groep3.bouncer.service.FileService;
import nl.hsleiden.ipsen2.groep3.bouncer.service.QrCodeService;
import nl.hsleiden.ipsen2.groep3.bouncer.service.StorageService;
import nl.hsleiden.ipsen2.groep3.bouncer.service.StorageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The RequestController manages all requests
 * it manages adding requests and reviewing them.
 *
 * It also manages file uploading for a request.
 *
 * @author Wouter van der Neut
 * @author Youp van Leeuwen
 */

@RestController
@RequestMapping("/request")
public class RequestController {
    private final StorageServiceInterface storageServiceInterface;
    private final FileService fileService;
    private final RequestRepository requestRepository;
    private final RequestUpdateRepository requestUpdateRepository;
    private final QrCodeService qrCodeService;
    private final QrCodeRepository qrCodeRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public RequestController(FileService fileService, StorageService storageService, RequestRepository requestRepository,
                             RequestUpdateRepository requestUpdateRepository, QrCodeService qrCodeService,
                             QrCodeRepository qrCodeRepository, UserRepository userRepository,
                             PhotoRepository photoRepository) {
        this.fileService = fileService;
        this.storageServiceInterface = storageService;
        this.requestRepository = requestRepository;
        this.requestUpdateRepository = requestUpdateRepository;
        this.qrCodeService = qrCodeService;
        this.qrCodeRepository = qrCodeRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Request>> index () {
        return new ResponseEntity<>(this.requestRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> FileUpload(@ModelAttribute CreateRequestRequest body) {
        UserPrincipal userAccountPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        Worker user = (Worker) userAccountPrincipal.getUser();

        try {
            String qrCodeString = this.readQrCode(body.getFile());
            Optional<QrCode> qrCode = qrCodeRepository.findByCode(qrCodeString);

            if (qrCode.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Request request = qrCode.get().getRequest();

            if (!Objects.equals(request.getRequestedBy().getId(), user.getId())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            user.seteMail(body.geteMail());
            user.setfName(body.getfName());
            user.setlName(body.getlName());
            user.setBirthday(body.getBirthday());

            this.userRepository.save(user);

            Photo photo = storeFileAndCreatePhoto(body.getFile());
            request.addPhoto(photo);

            photoRepository.save(photo);

            this.requestRepository.save(request);

            RequestUpdate requestUpdate = new RequestUpdate();
            requestUpdate.setRequest(request);
            requestUpdate.setNewState(Status.PENDING);

            this.requestUpdateRepository.save(requestUpdate);
        } catch (InvalidFileTypeException | NotFoundException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private String readQrCode (MultipartFile file) throws NotFoundException, IOException {
        return this.qrCodeService.readQrCode(file);
    }

    private Photo storeFileAndCreatePhoto (MultipartFile file) throws InvalidFileTypeException, FileNotFoundException {
        fileService.setFile(file);
        fileService.validateFile();
        String newName = fileService.generateFileName();
        storageServiceInterface.store(file, newName);

        Photo photo = new Photo();
        photo.setFilename(newName);

        return photo;
    }

    @GetMapping("/{id}/{status}/review")
    public ResponseEntity<RequestUpdate> requestUpdate(@PathVariable("id") Long id, @PathVariable("status") int status) {
        Optional<Request> optionalRequest = this.requestRepository.findById(id);
        UserPrincipal userAccountPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (optionalRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserAccount user = (UserAccount) userAccountPrincipal.getUser();

        if (!user.isGranted(Role.MODERATOR) && !user.isGranted(Role.SITE_ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Request request = optionalRequest.get();
        RequestUpdate requestupdate = new RequestUpdate();
        requestupdate.setRequest(request);
        requestupdate.setNewState(Status.values()[status]);
        requestupdate.setUpdatedBy(user);
        requestUpdateRepository.save(requestupdate);

        return new ResponseEntity<>(requestupdate, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> findById (@PathVariable("id") Long id) {
        Optional<Request> optionalRequest = this.requestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalRequest.get(), HttpStatus.OK);
    }

    @PostMapping("/{id}/photo/")
    public ResponseEntity<?> addPhoto(@PathVariable("id") Long id){
        Optional<Request> optionalRequest = this.requestRepository.findById(id);
        Request request = requestRepository.getById(id);

        if (optionalRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return null;

    }

}
