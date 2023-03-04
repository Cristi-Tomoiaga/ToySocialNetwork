package toysocialnetwork.toysocialnetworkfx.repository.memory;

import toysocialnetwork.toysocialnetworkfx.domain.Entity;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Generic in memory repository for generic entities with a generic id
 *
 * @param <ID> the type of the id
 * @param <E>  the type of the entity, it must extend Entity
 */
public abstract class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;
    private final Validator<E> validator;
    private ID lastId;

    /**
     * Constructor for InMemoryRepository
     *
     * @param validator a validator for the entities stored in the repository
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    @Override
    public Optional<E> findById(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null");

        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Optional<E> findByValue(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        for (E e : entities.values()) {
            if (e.equals(entity))
                return Optional.of(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(entity);
        if (!entities.containsKey(entity.getId()) && this.findByValue(entity).isEmpty()) {
            entities.put(entity.getId(), entity);
            lastId = entity.getId();

            return Optional.empty();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(entity);
        if (entities.containsKey(entity.getId()) &&
                (this.findByValue(entity).isEmpty() || this.findByValue(entity).get().getId().equals(entity.getId()))) {
            entities.put(entity.getId(), entity);

            return Optional.empty();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null");

        if (entities.containsKey(id))
            return Optional.ofNullable(entities.remove(id));

        return Optional.empty();
    }

    @Override
    public ID getLastId() {
        return lastId;
    }
}
