package toysocialnetwork.toysocialnetworkfx.service;

import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.dto.FriendshipDTO;
import toysocialnetwork.toysocialnetworkfx.repository.database.FriendshipDBRepository;
import toysocialnetwork.toysocialnetworkfx.repository.database.UserDBRepository;
import toysocialnetwork.toysocialnetworkfx.service.graph.UserGraph;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observable;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for operations on friendships
 */
public class FriendshipService implements Observable {
    private final FriendshipDBRepository friendshipRepository;
    private final UserDBRepository userRepository;
    private final List<Observer> observerList;

    /**
     * Constructor for FriendshipService
     *
     * @param friendshipRepository reference to the friendship repository
     * @param userRepository       reference to the user repository
     */
    public FriendshipService(FriendshipDBRepository friendshipRepository, UserDBRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
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
     * Finds all the friendships stored in the application
     *
     * @return the friendships
     */
    public Iterable<Friendship> findAllFriendships() {
        return friendshipRepository.findAll();
    }

    /**
     * Gets all the friendship requests that were sent to the given user (by id)
     *
     * @param userId the id of the user
     * @return the list of friendship requests (as FriendshipDTO)
     */
    public List<FriendshipDTO> getFriendshipRequestsFor(Long userId) {
        List<Friendship> friendships = friendshipRepository.getFriendshipRequestsFor(userId);

        return friendships.stream()
                .map(friendship -> {
                    User otherUser;
                    if (userId.equals(friendship.getFirstUser()))
                        otherUser = userRepository.findById(friendship.getSecondUser()).get();
                    else
                        otherUser = userRepository.findById(friendship.getFirstUser()).get();

                    return new FriendshipDTO(friendship.getId(), otherUser,
                            friendship.getFriendsFrom(), friendship.getStatus(), friendship.getSentBy());
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets all the friendship requests that were sent by the given user (by id)
     *
     * @param userId the id of the user
     * @return the list of friendship requests (as FriendshipDTO)
     */
    public List<FriendshipDTO> getFriendshipRequestsFrom(Long userId) {
        List<Friendship> friendships = friendshipRepository.getFriendshipRequestsFrom(userId);

        return friendships.stream()
                .map(friendship -> {
                    User otherUser;
                    if (userId.equals(friendship.getFirstUser()))
                        otherUser = userRepository.findById(friendship.getSecondUser()).get();
                    else
                        otherUser = userRepository.findById(friendship.getFirstUser()).get();

                    return new FriendshipDTO(friendship.getId(), otherUser,
                            friendship.getFriendsFrom(), friendship.getStatus(), friendship.getSentBy());
                })
                .collect(Collectors.toList());
    }

    /**
     * Adds a new friendship in the application, the id will be generated automatically
     *
     * @param firstUser   the id of the first user of the friendship
     * @param secondUser  the id of the second user of the friendship
     * @param friendsFrom the date of the creation of the friendship
     * @param status      the status of the friendship request
     * @param sentBy      the sender of the friendship request
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the friendship is invalid
     * @throws ServiceException                                                          if the friendship could not be added
     */
    public void addFriendship(Long firstUser, Long secondUser, LocalDateTime friendsFrom, Status status, Sender sentBy) {
        Optional<User> user1 = userRepository.findById(firstUser);
        Optional<User> user2 = userRepository.findById(secondUser);

        if (user1.isEmpty() || user2.isEmpty())
            throw new ServiceException("Invalid user ids\n");

        Friendship friendship = new Friendship(firstUser, secondUser, friendsFrom);
        friendship.setId(friendshipRepository.getLastId() + 1);
        friendship.setStatus(status);
        friendship.setSentBy(sentBy);

        Optional<Friendship> newFriendship = friendshipRepository.save(friendship);

        if (newFriendship.isPresent())
            throw new ServiceException("Friendship already exists\n");

        user1.get().addFriend(secondUser);
        user2.get().addFriend(firstUser);

        notifyObservers();
    }

    /**
     * Modifies an existing friendship in the application
     *
     * @param id          the id of the friendship
     * @param friendsFrom the new date
     * @param status      the new status of the friendship request
     * @throws toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException if the new data is invalid
     * @throws ServiceException                                                          if the friendship could not be updated
     */
    public void modifyFriendship(Long id, LocalDateTime friendsFrom, Status status) {
        Optional<Friendship> existingFriendship = friendshipRepository.findById(id);
        if (existingFriendship.isEmpty())
            throw new ServiceException("The friendship with the given id does not exist\n");

        Friendship friendship = new Friendship(existingFriendship.get().getFirstUser(),
                existingFriendship.get().getSecondUser(), friendsFrom);
        friendship.setId(id);
        friendship.setStatus(status);

        Optional<Friendship> updatedFriendship = friendshipRepository.update(friendship);

        if (updatedFriendship.isPresent())
            throw new ServiceException("Friendship could not be updated\n");

        notifyObservers();
    }

    /**
     * Deletes a friendship from the application
     *
     * @param id the id of the friendship
     * @throws ServiceException if the friendship could not be deleted
     */
    public void deleteFriendship(Long id) {
        Optional<Friendship> friendship = friendshipRepository.delete(id);

        if (friendship.isEmpty())
            throw new ServiceException("Invalid friendship id\n");

        Optional<User> user1 = userRepository.findById(friendship.get().getFirstUser());
        Optional<User> user2 = userRepository.findById(friendship.get().getSecondUser());

        user1.get().removeFriend(friendship.get().getSecondUser());
        user2.get().removeFriend(friendship.get().getFirstUser());

        notifyObservers();
    }

    /**
     * Gets the total number of communities in the application
     *
     * @return the number of communities
     */
    public int getCommunitiesCount() {
        UserGraph graph = new UserGraph(userRepository.findAll());

        List<UserGraph> connectedComponents = graph.getConnectedComponents();
        return connectedComponents.size();
    }

    /**
     * Gets the most sociable community in the application
     *
     * @return the list of the users from that community
     */
    public List<User> getMostSociableCommunity() {
        UserGraph graph = new UserGraph(userRepository.findAll());

        UserGraph community = graph.getConnectedComponentMaxPath();
        if (community == null)
            return new ArrayList<>();

        Iterable<User> users = community.getUsers();
        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);

        return userList;
    }
}
