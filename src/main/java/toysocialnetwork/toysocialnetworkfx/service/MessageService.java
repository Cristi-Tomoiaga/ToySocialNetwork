package toysocialnetwork.toysocialnetworkfx.service;

import toysocialnetwork.toysocialnetworkfx.domain.Message;
import toysocialnetwork.toysocialnetworkfx.repository.database.MessageDBRepository;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observable;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for operations on friendships
 */
public class MessageService implements Observable {
    private final MessageDBRepository messageRepository;
    private final List<Observer> observerList;

    /**
     * Constructor for MessageService
     *
     * @param messageRepository reference to the message repository
     */
    public MessageService(MessageDBRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.observerList = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observerList.forEach(Observer::update);
    }

    /**
     * Finds all the messages stored in the application
     *
     * @return the messages
     */
    public Iterable<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Gets all the messages sent between two users given by id
     *
     * @param firstUserId  the id of the first user
     * @param secondUserId the id of the second user
     * @return the list of messages
     */
    public List<Message> getMessagesBetween(Long firstUserId, Long secondUserId) {
        return messageRepository.getMessagesBetween(firstUserId, secondUserId);
    }

    /**
     * Adds a new message in the application, the id will be generated automatically
     *
     * @param fromUser    the id of the user that sent the message
     * @param toUser      the id of the user that received the message
     * @param messageBody the body of the message
     * @param sentDate    the date when the message was sent
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the message is invalid
     * @throws ServiceException                                                          if the message could not be added
     */
    public void addMessage(Long fromUser, Long toUser, String messageBody, LocalDateTime sentDate) {
        Message message = new Message(fromUser, toUser, messageBody, sentDate);
        message.setId(messageRepository.getLastId() + 1);

        Optional<Message> newMessage = messageRepository.save(message);
        if (newMessage.isPresent())
            throw new ServiceException("Message could not be added\n");

        notifyObservers();
    }
}
