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
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    public Worker CheckIfuserIsValid(){
        UserPrincipal userAccountPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        Worker user = (Worker) userAccountPrincipal.getUser();
        return user;
    }

    public Boolean CheckIfReuestIsValid(CreateRequest newRequest,Worker user) throws NotFoundException, IOException {
        String qrCodeString = this.readQrCode(newRequest.getFile());
        Optional<QrCode> qrCode = qrCodeRepository.findByCode(qrCodeString);
        if (qrCode.isEmpty()) {
            return false;
        }
        Request request = qrCode.get().getRequest();
        if (!Objects.equals(request.getRequestedBy().getId(), user.getId())) {
            return false;
        }
        return true;
    }

    public Request getReuqest(CreateRequest newRequest){
        String qrCodeString = null;
        try {
            qrCodeString = this.readQrCode(newRequest.getFile());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<QrCode> qrCode = qrCodeRepository.findByCode(qrCodeString);
        Request request = qrCode.get().getRequest();
        return request;
    }
    public void SaveUser(CreateRequest newRequest){
        Worker user = CheckIfuserIsValid();
        user.setEMail(newRequest.geteMail());
        user.setfName(newRequest.getfName());
        user.setlName(newRequest.getlName());
        user.setBirthday(newRequest.getBirthday());
        this.userRepository.save(user);
    }

    public void SavePhoto(CreateRequest newCreateRequest){
        Request newRequest = getReuqest(newCreateRequest);
        Photo photo = null;
        try {
            photo = storeFileAndCreatePhoto(newCreateRequest.getFile());
        } catch (InvalidFileTypeException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        newRequest.setPhotos((Set<Photo>) photo);
        photoRepository.save(photo);
    }

    @PostMapping("")
    public ResponseEntity<String> CreateNewRequest(@ModelAttribute CreateRequest newCreateRequest) {
        Worker user = CheckIfuserIsValid();
        Request newRequest = getReuqest(newCreateRequest);
        try {
            if (!CheckIfReuestIsValid(newCreateRequest,user)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"QR Code is niet gevonden of je hebt niet genoegRechten!");
            }
            SaveUser(newCreateRequest);
            SavePhoto(newCreateRequest);
            this.requestRepository.save(newRequest);
            RequestUpdate requestUpdate = new RequestUpdate();
            requestUpdate.setRequest(newRequest);
            requestUpdate.setNewState(Status.PENDING);
            this.requestUpdateRepository.save(requestUpdate);
        } catch (NotFoundException | IOException e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Er ging iets fout met het opstuuren van de Request! Check je Request nog een keer dat je alles hebt ingevuld! ");
        }
        throw new ResponseStatusException(HttpStatus.CREATED,"Request is gemaakt!");
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
