package toysocialnetwork.toysocialnetworkfx.domain.dto;

/**
 * Data Transfer Object for User
 */
public class UserDTO {
    private Long id;
    private Long friendshipId;
    private String username;
    private String firstName;
    private String lastName;

    /**
     * Constructor for UserDTO
     *
     * @param id           the id of the user
     * @param friendshipId the id of the friendship
     * @param username     the username of the user
     * @param firstName    the first name of the user
     * @param lastName     the last name of the user
     */
    public UserDTO(Long id, Long friendshipId, String username, String firstName, String lastName) {
        this.id = id;
        this.friendshipId = friendshipId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getter for id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for friendshipId
     *
     * @return the friendshipId
     */
    public Long getFriendshipId() {
        return friendshipId;
    }

    /**
     * Setter for friendshipId
     *
     * @param friendshipId the friendshipId
     */
    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
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
     * Setter for username
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for firstName
     *
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName
     *
     * @param firstName the firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName
     *
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName
     *
     * @param lastName the lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
