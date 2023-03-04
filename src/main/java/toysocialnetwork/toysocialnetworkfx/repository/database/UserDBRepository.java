package toysocialnetwork.toysocialnetworkfx.repository.database;

import javafx.util.Pair;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.Repository;

import java.sql.*;
import java.util.*;

/**
 * Database repository for user entities
 */
public class UserDBRepository implements Repository<Long, User> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<User> validator;

    /**
     * Constructor for UserDBRepository
     *
     * @param validator a validator for user entities
     * @param url       the url for the database connection
     * @param user      the database user
     * @param password  the password for the database user
     */
    public UserDBRepository(Validator<User> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long userID = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");

                User user = new User(username, userFirstName, userLastName, password);
                user.setId(userID);
                user.setFriends(findFriendsFor(id));

                return Optional.of(user);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all the friends for a given user by id
     *
     * @param userID the id of the user
     * @return the list of the friends as a list of ids
     */
    private List<Long> findFriendsFor(Long userID) {
        List<Long> friendsList = new ArrayList<>();
        String query = """
                    SELECT id_first_user AS id_user FROM friendships WHERE id_second_user=? AND status='ACCEPTED'
                    UNION
                    SELECT id_second_user AS id_user FROM friendships WHERE id_first_user=? AND status='ACCEPTED';
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, userID);
            statement.setLong(2, userID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id_user");

                    friendsList.add(id);
                }
            }

            return friendsList;
        } catch (SQLException e) {
            return friendsList;
        }
    }

    /**
     * Finds all the friends for a given user by id
     *
     * @param userId the id of the user
     * @return the list of the friends as a list of pairs of user ids and friendship ids
     */
    public List<Pair<Long, Long>> findFriendUsersFor(Long userId) {
        List<Pair<Long, Long>> friendsList = new ArrayList<>();
        String query = """
                    SELECT id_first_user AS id_user, id AS id_friendship FROM friendships WHERE id_second_user=? AND status='ACCEPTED'
                    UNION
                    SELECT id_second_user AS id_user, id AS id_friendship FROM friendships WHERE id_first_user=? AND status='ACCEPTED';
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long idUser = resultSet.getLong("id_user");
                    Long idFriendship = resultSet.getLong("id_friendship");

                    friendsList.add(new Pair<>(idUser, idFriendship));
                }
            }

            return friendsList;
        } catch (SQLException e) {
            return friendsList;
        }
    }

    /**
     * Finds all the users that are not friends with the given user by id
     *
     * @param userId the id of the user
     * @return the list of found users as a list of ids
     */
    public List<Long> findAvailableUsersFor(Long userId) {
        List<Long> availableUsersList = new ArrayList<>();
        String query = """
                    SELECT id AS id_user FROM users WHERE id<>?
                    EXCEPT
                    (
                     SELECT id_first_user AS id_user FROM friendships WHERE id_second_user=?
                     UNION
                     SELECT id_second_user AS id_user FROM friendships WHERE id_first_user=?
                    );
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setLong(3, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id_user");

                    availableUsersList.add(id);
                }
            }

            return availableUsersList;
        } catch (SQLException e) {
            return availableUsersList;
        }
    }

    @Override
    public Iterable<User> findAll() {
        String query = "SELECT * FROM users";
        Set<User> userSet = new TreeSet<>(Comparator.comparingLong(User::getId));

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long userID = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");

                User user = new User(username, userFirstName, userLastName, password);
                user.setId(userID);
                user.setFriends(findFriendsFor(userID));
                userSet.add(user);
            }

            return userSet;
        } catch (SQLException e) {
            return userSet;
        }
    }

    @Override
    public Optional<User> save(User entity) {
        String query = "INSERT INTO users(username, first_name, last_name, password) VALUES (?, ?, ?, ?)";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getPassword());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        String query = "UPDATE users SET first_name=?, last_name=?, password=? WHERE id=?";
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getPassword());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<User> delete(Long id) {
        String query = "DELETE FROM users WHERE id=?";
        Optional<User> foundUser = findById(id);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

            return foundUser;
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByValue(User entity) {
        String query = "SELECT * FROM users WHERE username=?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, entity.getUsername());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                Long userID = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");

                User user = new User(username, userFirstName, userLastName, password);
                user.setId(userID);
                user.setFriends(findFriendsFor(userID));

                return Optional.of(user);
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long getLastId() {
        Iterable<User> users = findAll();

        Long lastID = 0L;
        for (User u : users) {
            lastID = u.getId();
        }

        return lastID;
    }
}
