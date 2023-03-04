package toysocialnetwork.toysocialnetworkfx.service;

import javafx.util.Pair;
import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.dto.UserDTO;
import toysocialnetwork.toysocialnetworkfx.repository.database.FriendshipDBRepository;
import toysocialnetwork.toysocialnetworkfx.repository.database.UserDBRepository;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observable;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for operations on users
 */
public class UserService implements Observable {
    private final UserDBRepository userRepository;
    private final FriendshipDBRepository friendshipRepository;
    private final List<Observer> observerList;

    /**
     * Constructor for UserService
     *
     * @param userRepository       reference to the user repository
     * @param friendshipRepository reference to the friendship repository
     */
    public UserService(UserDBRepository userRepository, FriendshipDBRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
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
     * Finds all the users stored in the application
     *
     * @return the users
     */
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Verifies the login of the user given the username and password
     *
     * @param username the username
     * @param password the password
     * @return an {@code Optional} containing the associated user object if the login was successful or null otherwise
     */
    public Optional<User> login(String username, String password) {
        Optional<User> foundUser = userRepository.findByValue(new User(username, "", "", password));

        if (foundUser.isEmpty())
            return Optional.empty();

        if (!foundUser.get().getPassword().equals(password))
            return Optional.empty();

        return foundUser;
    }

    /**
     * Finds all the users that are not friends with the given user by id
     *
     * @param userId the id of the user
     * @return the list of found users
     */
    public List<User> findAvailableUsersFor(Long userId) {
        List<Long> userIds = userRepository.findAvailableUsersFor(userId);

        return userIds.stream()
                .map(id -> userRepository.findById(id).get())
                .collect(Collectors.toList());
    }

    /**
     * Finds all the friends for a given user by id
     *
     * @param userId the id of the user
     * @return the list of the friends as a list of UserDTO objects
     */
    public List<UserDTO> findFriendUsersFor(Long userId) {
        List<Pair<Long, Long>> idPairs = userRepository.findFriendUsersFor(userId);

        return idPairs.stream()
                .map(pair -> {
                    User user = userRepository.findById(pair.getKey()).get();

                    return new UserDTO(pair.getKey(), pair.getValue(),
                            user.getUsername(), user.getFirstName(), user.getLastName());
                })
                .collect(Collectors.toList());
    }

    /**
     * Adds a new user in the application, the id will be generated automatically
     *
     * @param username  the username of the new user
     * @param firstName the first name of the new user
     * @param lastName  the last name of the new user
     * @param password  the password of the new user
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the user is invalid
     * @throws ServiceException                                                          if the user could not be added
     */
    public void addUser(String username, String firstName, String lastName, String password) {
        User user = new User(username, firstName, lastName, password);
        user.setId(userRepository.getLastId() + 1);

        Optional<User> newUser = userRepository.save(user);

        if (newUser.isPresent())
            throw new ServiceException("User already exists\n");

        notifyObservers();
    }

    /**
     * Modifies an existing user in the application
     *
     * @param id        the id of the user to be modified
     * @param firstName the new first name of the user
     * @param lastName  the new last name of the user
     * @param password  the new password of the user
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the new data provided is invalid
     * @throws ServiceException                                                          if the user could not be updated
     */
    public void modifyUser(Long id, String firstName, String lastName, String password) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty())
            throw new ServiceException("The user with the given id does not exist\n");

        User user = new User(existingUser.get().getUsername(), firstName, lastName, password);
        user.setId(id);
        user.setFriends(existingUser.get().getFriends());

        Optional<User> updatedUser = userRepository.update(user);

        if (updatedUser.isPresent())
            throw new ServiceException("User could not be updated\n");

        notifyObservers();
    }

    /**
     * Deletes a user from the application
     *
     * @param id the id of the user
     * @throws ServiceException if the user could not be deleted
     */
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            throw new ServiceException("Invalid user id\n");

        for (User other : userRepository.findAll())
            other.removeFriend(user.get().getId());

        for (Long otherId : user.get().getFriends()) {
            Long firstUser;
            Long secondUser;

            if (id < otherId) {
                firstUser = id;
                secondUser = otherId;
            } else { // clearly id != otherId
                firstUser = otherId;
                secondUser = id;
            }

            Optional<Friendship> friendship = friendshipRepository.findByValue(new Friendship(firstUser, secondUser,
                    LocalDateTime.now()));
            friendshipRepository.delete(friendship.get().getId());
        }

        userRepository.delete(id);

        notifyObservers();
    }
}
