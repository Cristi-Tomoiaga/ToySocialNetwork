package toysocialnetwork.toysocialnetworkfx.domain.dto;

import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.User;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Friendship
 */
public class FriendshipDTO {
    private Long id;
    private User otherUser;
    private LocalDateTime friendsFrom;
    private Status status;
    private Sender sentBy;

    /**
     * Constructor for FriendshipDTO
     *
     * @param id          the id of the friendship
     * @param otherUser   the other user from the friendship
     * @param friendsFrom the date of the creation of the friendship
     * @param status      the status of the friendship request
     * @param sentBy      the sender of the friendship request
     */
    public FriendshipDTO(Long id, User otherUser, LocalDateTime friendsFrom, Status status, Sender sentBy) {
        this.id = id;
        this.otherUser = otherUser;
        this.friendsFrom = friendsFrom;
        this.status = status;
        this.sentBy = sentBy;
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
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for otherUser
     *
     * @return the otherUser
     */
    public User getOtherUser() {
        return otherUser;
    }

    /**
     * Setter for otherUser
     *
     * @param otherUser the otherUser
     */
    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    /**
     * Getter for friendsFrom
     *
     * @return friendsFrom date
     */
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /**
     * Setter for friendsFrom
     *
     * @param friendsFrom the new date for friendsFrom
     */
    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    /**
     * Getter for status
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Setter for status
     *
     * @param status the status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Getter for sentBy
     *
     * @return the sender
     */
    public Sender getSentBy() {
        return sentBy;
    }

    /**
     * Setter for sentBy
     *
     * @param sentBy the new sender
     */
    public void setSentBy(Sender sentBy) {
        this.sentBy = sentBy;
    }
}
