package toysocialnetwork.toysocialnetworkfx.domain.validators;

import toysocialnetwork.toysocialnetworkfx.domain.Friendship;

/**
 * Validator for a Friendship
 */
public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getId() == null || entity.getId() <= 0)
            errorMessage += "Invalid id\n";
        if (entity.getFirstUser() == null || entity.getFirstUser() <= 0)
            errorMessage += "Invalid first user id\n";
        if (entity.getSecondUser() == null || entity.getSecondUser() <= 0)
            errorMessage += "Invalid second user id\n";
        if (entity.getFriendsFrom() == null)
            errorMessage += "Invalid date for friends from\n";
        if (entity.getFirstUser() != null && entity.getSecondUser() != null) {
            Long first = entity.getFirstUser();
            Long second = entity.getSecondUser();
            if (first > 0 && second > 0 && first >= second)
                errorMessage += "First id must be strictly less than the second id\n";
        }

        if (errorMessage.length() > 0)
            throw new ValidationException(errorMessage);
    }
}
