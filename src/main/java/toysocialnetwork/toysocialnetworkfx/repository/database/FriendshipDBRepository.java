package toysocialnetwork.toysocialnetworkfx.repository.database;

import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Database repository for friendship entities
 */
public class FriendshipDBRepository implements Repository<Long, Friendship> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Friendship> validator;

    /**
     * Constructor for FriendshipDBRepository
     *
     * @param validator a validator for friendship entities
     * @param url       the url for the database connection
     * @param user      the database user
     * @param password  the password for the database user
     */
    public FriendshipDBRepository(Validator<Friendship> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Friendship> findById(Long id) {
        String query = "SELECT * FROM friendships WHERE id=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long friendshipID = resultSet.getLong("id");
                Long friendshipIDFirstUser = resultSet.getLong("id_first_user");
                Long friendshipIDSecondUser = resultSet.getLong("id_second_user");
                LocalDateTime friendshipFriendsFrom = resultSet.getTimestamp("friends_from")
                        .toLocalDateTime();
                Status status = Status.valueOf(resultSet.getString("status"));
                Sender sentBy = Sender.valueOf(resultSet.getString("sent_by"));

                Friendship friendship = new Friendship(friendshipIDFirstUser,
                        friendshipIDSecondUser, friendshipFriendsFrom);
                friendship.setId(friendshipID);
                friendship.setStatus(status);
                friendship.setSentBy(sentBy);

                return Optional.of(friendship);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets all the friendship requests that were sent to the given user (by id)
     *
     * @param userId the id of the user
     * @return the list of friendship requests
     */
    public List<Friendship> getFriendshipRequestsFor(Long userId) {
        String query = """
                    SELECT * FROM friendships WHERE id_second_user=? AND sent_by='FIRST'
                    UNION
                    SELECT * FROM friendships WHERE id_first_user=? AND sent_by='SECOND';
                """;
        List<Friendship> friendshipRequests = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long friendshipID = resultSet.getLong("id");
                    Long friendshipIDFirstUser = resultSet.getLong("id_first_user");
                    Long friendshipIDSecondUser = resultSet.getLong("id_second_user");
                    LocalDateTime friendshipFriendsFrom = resultSet.getTimestamp("friends_from")
                            .toLocalDateTime();
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Sender sentBy = Sender.valueOf(resultSet.getString("sent_by"));

                    Friendship friendship = new Friendship(friendshipIDFirstUser,
                            friendshipIDSecondUser, friendshipFriendsFrom);
                    friendship.setId(friendshipID);
                    friendship.setStatus(status);
                    friendship.setSentBy(sentBy);

                    friendshipRequests.add(friendship);
                }
            }

            return friendshipRequests;
        } catch (SQLException e) {
            return friendshipRequests;
        }
    }

    /**
     * Gets all the friendship requests that were sent by the given user (by id)
     *
     * @param userId the id of the user
     * @return the list of friendship requests
     */
    public List<Friendship> getFriendshipRequestsFrom(Long userId) {
        String query = """
                    SELECT * FROM friendships WHERE id_first_user=? AND sent_by='FIRST'
                    UNION
                    SELECT * FROM friendships WHERE id_second_user=? AND sent_by='SECOND';
                """;
        List<Friendship> friendshipRequests = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long friendshipID = resultSet.getLong("id");
                    Long friendshipIDFirstUser = resultSet.getLong("id_first_user");
                    Long friendshipIDSecondUser = resultSet.getLong("id_second_user");
                    LocalDateTime friendshipFriendsFrom = resultSet.getTimestamp("friends_from")
                            .toLocalDateTime();
                    Status status = Status.valueOf(resultSet.getString("status"));
                    Sender sentBy = Sender.valueOf(resultSet.getString("sent_by"));

                    Friendship friendship = new Friendship(friendshipIDFirstUser,
                            friendshipIDSecondUser, friendshipFriendsFrom);
                    friendship.setId(friendshipID);
                    friendship.setStatus(status);
                    friendship.setSentBy(sentBy);

                    friendshipRequests.add(friendship);
                }
            }

            return friendshipRequests;
        } catch (SQLException e) {
            return friendshipRequests;
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        String query = "SELECT * FROM friendships";
        Set<Friendship> friendshipSet = new TreeSet<>(Comparator.comparingLong(Friendship::getId));

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long friendshipID = resultSet.getLong("id");
                Long friendshipIDFirstUser = resultSet.getLong("id_first_user");
                Long friendshipIDSecondUser = resultSet.getLong("id_second_user");
                LocalDateTime friendshipFriendsFrom = resultSet.getTimestamp("friends_from")
                        .toLocalDateTime();
                Status status = Status.valueOf(resultSet.getString("status"));
                Sender sentBy = Sender.valueOf(resultSet.getString("sent_by"));

                Friendship friendship = new Friendship(friendshipIDFirstUser,
                        friendshipIDSecondUser, friendshipFriendsFrom);
                friendship.setId(friendshipID);
                friendship.setStatus(status);
                friendship.setSentBy(sentBy);

                friendshipSet.add(friendship);
            }

            return friendshipSet;
        } catch (SQLException e) {
            return friendshipSet;
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String query = "INSERT INTO friendships(id_first_user, id_second_user, friends_from, status, sent_by) VALUES (?, ?, ?, ?::status, ?::sender)";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getFirstUser());
            statement.setLong(2, entity.getSecondUser());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            statement.setString(4, entity.getStatus().toString());
            statement.setString(5, entity.getSentBy().toString());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        String query = "UPDATE friendships SET friends_from=?, status=?::status WHERE id=?";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getFriendsFrom()));
            statement.setString(2, entity.getStatus().toString());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Friendship> delete(Long id) {
        String query = "DELETE FROM friendships WHERE id=?";
        Optional<Friendship> foundFriendship = findById(id);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

            return foundFriendship;
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Friendship> findByValue(Friendship entity) {
        String query = "SELECT * FROM friendships WHERE id_first_user=? AND id_second_user=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, entity.getFirstUser());
            statement.setLong(2, entity.getSecondUser());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long friendshipID = resultSet.getLong("id");
                Long friendshipIDFirstUser = resultSet.getLong("id_first_user");
                Long friendshipIDSecondUser = resultSet.getLong("id_second_user");
                LocalDateTime friendshipFriendsFrom = resultSet.getTimestamp("friends_from")
                        .toLocalDateTime();
                Status status = Status.valueOf(resultSet.getString("status"));
                Sender sentBy = Sender.valueOf(resultSet.getString("sent_by"));

                Friendship friendship = new Friendship(friendshipIDFirstUser,
                        friendshipIDSecondUser, friendshipFriendsFrom);
                friendship.setId(friendshipID);
                friendship.setStatus(status);
                friendship.setSentBy(sentBy);

                return Optional.of(friendship);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long getLastId() {
        Iterable<Friendship> friendships = findAll();

        Long lastID = 0L;
        for (Friendship f : friendships) {
            lastID = f.getId();
        }

        return lastID;
    }
}
