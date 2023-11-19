package exceptions;

public class NotExistingTypeException extends RuntimeException{
    public NotExistingTypeException(String message) {
        super(message);
    }
}
