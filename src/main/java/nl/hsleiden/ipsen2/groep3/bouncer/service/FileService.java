package nl.hsleiden.ipsen2.groep3.bouncer.service;

import nl.hsleiden.ipsen2.groep3.bouncer.exception.InvalidFileTypeException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Service class that validates uploaded image files,
 * with necessary getters and setters
 *
 * @author Youp van Leeuwen
 * @author Wouter van der Neut
 */

@Service
public class FileService {
    private MultipartFile file;
    private final ArrayList<String> EXTENTIONS = new ArrayList<>();
    private static final int MEGABYTE = 1_048_576;

    public FileService(Environment environment) {
        this.EXTENTIONS.addAll(Arrays.asList(environment.getProperty("acceptedFileTypes").split(",")));
    }

    public void validateFile() throws InvalidFileTypeException, FileNotFoundException {
        validateFileType();
        validateFileSize();
    }

    private void validateFileType() throws InvalidFileTypeException {
        String fileExtension = FilenameUtils.getExtension(this.file.getOriginalFilename());

        if (!EXTENTIONS.contains(fileExtension.toLowerCase())) {
            throw new InvalidFileTypeException(fileExtension);
        }
    }

    private void validateFileSize() throws FileNotFoundException {
        if (this.file.getSize() > MEGABYTE * 5) {
            throw new FileNotFoundException("This file is to large");
        }
    }

    public String calculateFileHash(MultipartFile file) throws IOException {
        return null;
    }

    /**
     * Geeft percentage terug hoeveel de fotos op elkaar lijken
     *
     * @param hash van afbeelding waarme vergeleken moet worden
     * @return float
     */
    public float compareHash(String hash){
        return 0;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String generateFileName() {
        String fileExtension = FilenameUtils.getExtension(this.file.getOriginalFilename());

        return UUID.randomUUID() + "." + fileExtension.toLowerCase();
    }
}
