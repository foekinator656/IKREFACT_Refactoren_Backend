package nl.hsleiden.ipsen2.groep3.bouncer.exception;

/**
 * the InvalidFileTypeException is called when a wrong filetype is uploaded
 */

public class InvalidFileTypeException extends Exception {
    public InvalidFileTypeException(String format) {
        super(String.format("%s is not one of our accepted file formats", format));
    }
}
