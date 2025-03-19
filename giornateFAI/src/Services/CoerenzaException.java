package Services;

public class CoerenzaException extends Exception {
    public CoerenzaException(String message, Throwable cause) {
        super(message, cause); // Il costruttore di Exception accetta una causa
    }
}
