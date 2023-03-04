package toysocialnetwork.toysocialnetworkfx.repository;

import toysocialnetwork.toysocialnetworkfx.domain.Entity;

import java.util.Optional;

/**
 * Generic CRUD repository on generic entities with a generic id
 *
 * @param <ID> the type of the id
 * @param <E>  the type of the entity, it must extend Entity
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Finds an entity from the repository by id
     *
     * @param id the id, must not be null
     * @return an {@code Optional} containing the entity if found or null otherwise
     * @throws IllegalArgumentException if the id is null
     */
    Optional<E> findById(ID id);

    /**
     * Finds an entity with the corresponding id from the repository by the values contained in the entity
     * other than the id
     *
     * @param entity the entity without the id, must not be null
     * @return an {@code Optional} containing the complete entity if found or null otherwise
     * @throws IllegalArgumentException if the entity is null
     */
    Optional<E> findByValue(E entity);

    /**
     * Gets all the entities stored in the repository
     *
     * @return the entities
     */
    Iterable<E> findAll();

    /**
     * Stores an entity in the repository
     *
     * @param entity the entity, must not be null
     * @return an {@code Optional} containing null if saved successfully or the entity otherwise
     * (if it already exists or if the id is duplicated)
     * @throws IllegalArgumentException                                                  if the entity is null
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the entity is invalid
     */
    Optional<E> save(E entity);

    /**
     * Updates an entity from the repository
     *
     * @param entity the entity containing the new information
     * @return an {@code Optional} containing null if updated successfully or the entity otherwise
     * (if there is no entity with the given id or if the operation would result in a duplicated entity)
     * @throws IllegalArgumentException                                                  if the entity is null
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the entity is invalid
     */
    Optional<E> update(E entity);

    /**
     * Deletes an entity from the repository
     *
     * @param id the id of the entity to be deleted
     * @return an {@code Optional} containing the deleted entity if the operation was successful or null otherwise
     * @throws IllegalArgumentException if the id is null
     */
    Optional<E> delete(ID id);

    /**
     * Gets the id of the newest saved entity
     *
     * @return the id
     */
    ID getLastId();
}
