package toysocialnetwork.toysocialnetworkfx.service;

/**
 * Exception for service errors
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructor for ServiceException
     *
     * @param message the message of the exception
     */
    public ServiceException(String message) {
        super(message);
    }
}
