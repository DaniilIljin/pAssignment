package exceptions;

public class NotExistingObjectException extends RuntimeException{
    public NotExistingObjectException(String message) {
        super(message);
    }
}
