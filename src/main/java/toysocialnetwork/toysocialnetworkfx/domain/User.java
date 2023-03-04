package toysocialnetwork.toysocialnetworkfx.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User entity with a Long id
 */
public class User extends Entity<Long> {
    private final String username;
    private String firstName;
    private String lastName;
    private String password;
    private List<Long> friends;

    /**
     * Constructor for User
     *
     * @param username  the username of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param password  the password of the user
     */
    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.friends = new ArrayList<>();
    }

    /**
     * Getter for first name
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first name
     *
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for last name
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name
     *
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for the friends list
     *
     * @return the friends list
     */
    public List<Long> getFriends() {
        return friends;
    }

    /**
     * Setter for the friends list
     *
     * @param friends the friends list
     */
    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    /**
     * Adds a friend id to the friends list
     * The friend id will be not added if it is duplicated
     *
     * @param userId the user id of the friend
     */
    public void addFriend(Long userId) {
        if (!friends.contains(userId))
            friends.add(userId);
    }

    /**
     * Removes the friend id from the friends list if it exists
     *
     * @param userId the user id of the friend
     */
    public void removeFriend(Long userId) {
        friends.remove(userId);
    }

    /**
     * Getter for username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                '}';
    }
}
