package toysocialnetwork.toysocialnetworkfx.repository.file;

import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File based repository for friendship entities
 */
public class FriendshipFileRepository extends FileRepository<Long, Friendship> {
    /**
     * Constructor for FriendshipFileRepository
     *
     * @param validator a validator for the friendships stored in the repository
     * @param filePath  the path for the file containing the friendships
     */
    public FriendshipFileRepository(Validator<Friendship> validator, String filePath) {
        super(validator, filePath);
    }

    @Override
    protected Friendship extractEntityFrom(List<String> attributes) {
        Friendship friendship = new Friendship(Long.parseLong(attributes.get(1)), Long.parseLong(attributes.get(2)),
                LocalDateTime.parse(attributes.get(3), Constants.DATE_TIME_FORMATTER));
        friendship.setId(Long.parseLong(attributes.get(0)));
        friendship.setStatus(Status.valueOf(attributes.get(4)));
        friendship.setSentBy(Sender.valueOf(attributes.get(5)));

        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getFirstUser() + ";" + entity.getSecondUser()
                + ";" + entity.getFriendsFrom().format(Constants.DATE_TIME_FORMATTER)
                + ";" + entity.getStatus() + ";" + entity.getSentBy();
    }
}
