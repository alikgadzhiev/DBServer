package application.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
