package toysocialnetwork.toysocialnetworkfx.repository.file;

import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * File based repository for user entities
 */
public class UserFileRepository extends FileRepository<Long, User> {
    /**
     * Constructor for UserFileRepository
     *
     * @param validator a validator for the users stored in the repository
     * @param filePath  the path for the file containing the users
     */
    public UserFileRepository(Validator<User> validator, String filePath) {
        super(validator, filePath);
    }

    @Override
    protected User extractEntityFrom(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));

        if (attributes.size() > 3) { // if we have a list of friends
            String[] friendIds = attributes.get(5).split(",");
            for (String stringId : friendIds)
                user.addFriend(Long.parseLong(stringId));
        }

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        String friendsListString = entity.getFriends()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        return entity.getId() + ";" + entity.getUsername() + ";" + entity.getFirstName() + ";" + entity.getLastName()
                + ";" + entity.getPassword() + ";" + friendsListString;
    }
}
