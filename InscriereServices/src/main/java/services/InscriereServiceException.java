package services;

public class InscriereServiceException  extends Exception{
    public InscriereServiceException() {
    }

    public InscriereServiceException(String message) {
        super(message);
    }

    public InscriereServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
