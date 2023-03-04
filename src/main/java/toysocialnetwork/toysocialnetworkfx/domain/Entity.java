package toysocialnetwork.toysocialnetworkfx.domain;

/**
 * Generic Entity with a generic id
 *
 * @param <ID> the type of the id of the entity
 */
public class Entity<ID> {
    private ID id;

    /**
     * Getter for id
     *
     * @return the id
     */
    public ID getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id the id
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
