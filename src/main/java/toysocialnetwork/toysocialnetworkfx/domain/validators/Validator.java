package toysocialnetwork.toysocialnetworkfx.domain.validators;

/**
 * Generic validator for an entity
 *
 * @param <E> the entity type
 */
public interface Validator<E> {
    /**
     * Validates the entity object
     *
     * @param entity the entity to be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(E entity) throws ValidationException;
}
