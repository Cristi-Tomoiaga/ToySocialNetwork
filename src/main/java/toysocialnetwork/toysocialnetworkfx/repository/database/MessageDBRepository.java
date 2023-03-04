package toysocialnetwork.toysocialnetworkfx.repository.database;

import toysocialnetwork.toysocialnetworkfx.domain.Message;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Database repository for message entities
 */
public class MessageDBRepository implements Repository<Long, Message> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Message> validator;

    /**
     * Constructor for MessageDBRepository
     *
     * @param validator a validator for message entities
     * @param url       the url for the database connection
     * @param user      the database user
     * @param password  the password for the database user
     */
    public MessageDBRepository(Validator<Message> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Message> findById(Long id) {
        String query = "SELECT * FROM messages WHERE id=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long messageID = resultSet.getLong("id");
                Long messageIDFromUser = resultSet.getLong("id_from_user");
                Long messageIDToUser = resultSet.getLong("id_to_user");
                String messageBody = resultSet.getString("message_body");
                LocalDateTime messageSentDate = resultSet.getTimestamp("sent_date")
                        .toLocalDateTime();

                Message message = new Message(messageIDFromUser, messageIDToUser, messageBody, messageSentDate);
                message.setId(messageID);

                return Optional.of(message);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Message> findAll() {
        String query = "SELECT * FROM messages";
        Set<Message> messageSet = new TreeSet<>(Comparator.comparingLong(Message::getId));

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long messageID = resultSet.getLong("id");
                Long messageIDFromUser = resultSet.getLong("id_from_user");
                Long messageIDToUser = resultSet.getLong("id_to_user");
                String messageBody = resultSet.getString("message_body");
                LocalDateTime messageSentDate = resultSet.getTimestamp("sent_date")
                        .toLocalDateTime();

                Message message = new Message(messageIDFromUser, messageIDToUser, messageBody, messageSentDate);
                message.setId(messageID);

                messageSet.add(message);
            }

            return messageSet;
        } catch (SQLException e) {
            return messageSet;
        }
    }

    /**
     * Gets all the messages sent between two users given by id
     *
     * @param firstUserId  the id of the first user
     * @param secondUserId the id of the second user
     * @return the list of messages
     */
    public List<Message> getMessagesBetween(Long firstUserId, Long secondUserId) {
        String query = "SELECT * FROM messages WHERE (id_from_user=? AND id_to_user=?) OR (id_to_user=? AND id_from_user=?)";
        List<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, firstUserId);
            statement.setLong(2, secondUserId);
            statement.setLong(3, firstUserId);
            statement.setLong(4, secondUserId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long messageID = resultSet.getLong("id");
                    Long messageIDFromUser = resultSet.getLong("id_from_user");
                    Long messageIDToUser = resultSet.getLong("id_to_user");
                    String messageBody = resultSet.getString("message_body");
                    LocalDateTime messageSentDate = resultSet.getTimestamp("sent_date")
                            .toLocalDateTime();

                    Message message = new Message(messageIDFromUser, messageIDToUser, messageBody, messageSentDate);
                    message.setId(messageID);

                    messages.add(message);
                }
            }

            return messages;
        } catch (SQLException e) {
            return messages;
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        String query = "INSERT INTO messages(id_from_user, id_to_user, message_body, sent_date) VALUES (?, ?, ?, ?)";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getFromUser());
            statement.setLong(2, entity.getToUser());
            statement.setString(3, entity.getMessageBody());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getSentDate()));
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        String query = "UPDATE messages SET message_body=? WHERE id=?";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getMessageBody());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Message> delete(Long id) {
        String query = "DELETE FROM messages WHERE id=?";
        Optional<Message> foundMessage = findById(id);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

            return foundMessage;
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> findByValue(Message entity) {
        String query = "SELECT * FROM messages WHERE id_from_user=? AND id_to_user=? AND message_body=? AND sent_date=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getFromUser());
            statement.setLong(2, entity.getToUser());
            statement.setString(3, entity.getMessageBody());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getSentDate()));
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long messageID = resultSet.getLong("id");
                Long messageIDFromUser = resultSet.getLong("id_from_user");
                Long messageIDToUser = resultSet.getLong("id_to_user");
                String messageBody = resultSet.getString("message_body");
                LocalDateTime messageSentDate = resultSet.getTimestamp("sent_date")
                        .toLocalDateTime();

                Message message = new Message(messageIDFromUser, messageIDToUser, messageBody, messageSentDate);
                message.setId(messageID);

                return Optional.of(message);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long getLastId() {
        Iterable<Message> messages = findAll();

        Long lastID = 0L;
        for (Message m : messages) {
            lastID = m.getId();
        }

        return lastID;
    }
}
