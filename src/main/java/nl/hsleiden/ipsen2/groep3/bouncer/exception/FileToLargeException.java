package nl.hsleiden.ipsen2.groep3.bouncer.exception;

/**
 * the FileToLargeException is called when a file is lager tha 20mb
 */

public class FileToLargeException extends Exception {
    public FileToLargeException(String message) {
        super(message);
    }
}
