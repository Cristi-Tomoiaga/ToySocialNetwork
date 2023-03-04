package toysocialnetwork.toysocialnetworkfx.domain.validators;

/**
 * Exception for validation errors
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructor for ValidationException
     *
     * @param message the message of the exception
     */
    public ValidationException(String message) {
        super(message);
    }
}
