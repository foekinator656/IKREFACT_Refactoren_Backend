package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Photo;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.PhotoRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.service.StorageServiceInterface;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Manages uploading photos and saving images to
 * the photoRepository from the frontend via the endpoints.
 *
 * @author Youp van Leeuwen
 */

@RestController
@RequestMapping("/photo")
public class PhotoController {
    private final StorageServiceInterface storageServiceInterface;
    private final PhotoRepository photoRepository;

    public PhotoController(
            StorageServiceInterface storageServiceInterface,
            PhotoRepository photoRepository) {
        this.storageServiceInterface = storageServiceInterface;
        this.photoRepository = photoRepository;
    }

    /**
     * The Get mapping returns the image with the entered ID
     *
     * @param id
     * @return Image and Httpstatus
     * @throws IOException
     */

    @GetMapping(value = "/{fileName}")
    public @ResponseBody ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) throws IOException {
        Optional<Photo> optionalPhoto = photoRepository.findByFilename(fileName);
        HttpHeaders httpHeaders = new HttpHeaders();

        if (optionalPhoto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Photo photo = optionalPhoto.get();
        Resource image = storageServiceInterface.load(photo.getFilename());

        String mime = Files.probeContentType(image.getFile().toPath());
        httpHeaders.add("Content-Type", mime);

        return new ResponseEntity<>(image, httpHeaders, HttpStatus.OK);
    }
}
