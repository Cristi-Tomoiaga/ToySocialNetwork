package toysocialnetwork.toysocialnetworkfx.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Message entity with a Long id
 */
public class Message extends Entity<Long> {
    private final Long fromUser;
    private final Long toUser;
    private String messageBody;
    private final LocalDateTime sentDate;

    /**
     * Constructor for Message
     *
     * @param fromUser    the id of the user that sent the message
     * @param toUser      the id of the user that received the message
     * @param messageBody the body of the message
     * @param sentDate    the date when the message was sent
     */
    public Message(Long fromUser, Long toUser, String messageBody, LocalDateTime sentDate) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageBody = messageBody;
        this.sentDate = sentDate;
    }

    /**
     * Getter for the user that sent the message
     *
     * @return the user id
     */
    public Long getFromUser() {
        return fromUser;
    }

    /**
     * Getter for the user that received the message
     *
     * @return the user id
     */
    public Long getToUser() {
        return toUser;
    }

    /**
     * Getter for the message body
     *
     * @return the message body
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Setter for the message body
     *
     * @param messageBody the message body
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Getter for the date when the message was sent
     *
     * @return the date
     */
    public LocalDateTime getSentDate() {
        return sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(getFromUser(), message.getFromUser())
                && Objects.equals(getToUser(), message.getToUser())
                && Objects.equals(getMessageBody(), message.getMessageBody())
                && Objects.equals(getSentDate(), message.getSentDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromUser(), getToUser(), getMessageBody(), getSentDate());
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", messageBody='" + messageBody + '\'' +
                ", sentDate=" + sentDate +
                '}';
    }
}
