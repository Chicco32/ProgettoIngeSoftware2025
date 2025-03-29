package ServicesAPI;

public class Eccezioni extends Exception {

    public static class CoerenzaException extends Exception {
        public CoerenzaException(String message, Throwable cause) {
            super(message, cause); // Il costruttore di Exception accetta una causa
        }
    }

    public static class DBConnectionException extends Exception {
        public DBConnectionException(String message, Throwable cause) {
            super(message, cause); // Il costruttore di Exception accetta una causa
        } 
    }

    public static class ConfigFilesException extends Exception {
        public ConfigFilesException(String message, Throwable cause) {
            super(message, cause); // Il costruttore di Exception accetta una causa
        } 
    }
}

