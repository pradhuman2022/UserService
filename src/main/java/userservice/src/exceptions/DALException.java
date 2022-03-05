package userservice.src.exceptions;

public class DALException extends Exception{
    
    public DALException () {
        super();
    }

    public DALException(String message) {
        super(message);
    }

    public DALException(String message, Exception e) {
        super(message, e);
    }
}
