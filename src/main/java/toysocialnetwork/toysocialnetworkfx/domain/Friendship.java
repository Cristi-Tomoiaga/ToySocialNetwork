package toysocialnetwork.toysocialnetworkfx.domain;

import toysocialnetwork.toysocialnetworkfx.utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Friendship entity with a Long id
 */
public class Friendship extends Entity<Long> {
    private final Long firstUser;
    private final Long secondUser;
    private LocalDateTime friendsFrom;
    private Status status;
    private Sender sentBy;

    /**
     * Constructor for Friendship
     *
     * @param firstUser   the id of first user from the relation
     * @param secondUser  the id of second user from the relation
     * @param friendsFrom the date of the creation of the friendship
     */
    public Friendship(Long firstUser, Long secondUser, LocalDateTime friendsFrom) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.friendsFrom = friendsFrom;
    }

    /**
     * Getter for first user
     *
     * @return the first user
     */
    public Long getFirstUser() {
        return firstUser;
    }

    /**
     * Getter for second user
     *
     * @return the second user
     */
    public Long getSecondUser() {
        return secondUser;
    }

    /**
     * Getter for friends from date
     *
     * @return the date
     */
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /**
     * Setter for friends from date
     *
     * @param friendsFrom the date
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
     * @param sentBy the sender
     */
    public void setSentBy(Sender sentBy) {
        this.sentBy = sentBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(getFirstUser(), that.getFirstUser())
                && Objects.equals(getSecondUser(), that.getSecondUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstUser(), getSecondUser());
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + getId() +
                ", firstUser=" + getFirstUser() +
                ", secondUser=" + getSecondUser() +
                ", friendsFrom=" + getFriendsFrom().format(Constants.DATE_TIME_FORMATTER) +
                ", status=" + getStatus() +
                ", sentBy=" + getSentBy() +
                '}';
    }
}
