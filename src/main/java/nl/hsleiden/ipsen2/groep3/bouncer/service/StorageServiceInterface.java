package nl.hsleiden.ipsen2.groep3.bouncer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * the interface StorageServiceInterface stores and load's files
 */
public interface StorageServiceInterface {
    void init();

    void store(MultipartFile file, String fileName);

    Stream<Path> loadAll();

    Resource load(String filename);

    void deleteAll();
}
