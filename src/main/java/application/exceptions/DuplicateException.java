package application.exceptions;

public class DuplicateException extends RuntimeException{
    public DuplicateException (String message, Throwable e){
        super(message, e);
    }
}
