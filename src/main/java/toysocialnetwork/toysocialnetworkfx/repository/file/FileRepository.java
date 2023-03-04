package toysocialnetwork.toysocialnetworkfx.repository.file;

import toysocialnetwork.toysocialnetworkfx.domain.Entity;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.memory.InMemoryRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generic file based repository for generic entities with a generic id
 *
 * @param <ID> the type of the id
 * @param <E>  the type of the entity, it must extend Entity
 */
public abstract class FileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String filePath;

    /**
     * Constructor for FileRepository
     *
     * @param validator a validator for the entities stored in the repository
     * @param filePath  the path for the file containing the entities
     */
    public FileRepository(Validator<E> validator, String filePath) {
        super(validator);
        this.filePath = filePath;

        this.loadDataFromFile();
    }

    @Override
    public Optional<E> save(E entity) { // se putea folosi un decorator
        Optional<E> e = super.save(entity);
        if (e.isEmpty()) {
            this.saveDataToFile();
        }

        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> e = super.update(entity);
        if (e.isEmpty()) {
            this.saveDataToFile();
        }

        return e;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e = super.delete(id);
        if (e.isPresent()) {
            this.saveDataToFile();
        }

        return e;
    }

    /**
     * Abstract method for the creation of an entity from a list of attributes
     *
     * @param attributes a list of strings representing the attributes
     * @return the entity with the given attributes
     */
    protected abstract E extractEntityFrom(List<String> attributes);

    /**
     * Abstract method for the creation of a CSV string from a given entity
     *
     * @param entity the entity
     * @return the CSV string representation of the entity
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Loads the entities from the associated file
     */
    private void loadDataFromFile() {
        Path entitiesPath = Paths.get(filePath);

        try {
            List<String> lines = Files.readAllLines(entitiesPath);

            for (String line : lines) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E entity = this.extractEntityFrom(attributes);

                this.save(entity);
            }
        } catch (IOException e) {
            System.err.println("Could not read from file " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Saves the entities to the associated file
     */
    private void saveDataToFile() {
        Path entitiesPath = Paths.get(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(entitiesPath)) {
            for (E entity : this.findAll()) {
                writer.write(this.createEntityAsString(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Could not write to file " + filePath);
            e.printStackTrace();
        }
    }
}
