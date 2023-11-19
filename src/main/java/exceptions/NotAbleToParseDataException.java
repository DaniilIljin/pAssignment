package exceptions;

public class NotAbleToParseDataException extends RuntimeException{
    public NotAbleToParseDataException(String message) {
        super(message);
    }
}
