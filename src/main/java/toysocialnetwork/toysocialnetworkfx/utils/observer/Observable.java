package toysocialnetwork.toysocialnetworkfx.utils.observer;

/**
 * Interface that models an observable object from the observer pattern
 */
public interface Observable {
    /**
     * Adds a new observer object to the list of observing objects
     *
     * @param observer the observer object
     */
    void addObserver(Observer observer);

    /**
     * Removes an existing observer object from the list of observing objects
     *
     * @param observer the observer object
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all attached observer objects of a change in state
     */
    void notifyObservers();
}
