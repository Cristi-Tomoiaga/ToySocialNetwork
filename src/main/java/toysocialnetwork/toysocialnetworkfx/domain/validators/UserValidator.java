package toysocialnetwork.toysocialnetworkfx.domain.validators;

import toysocialnetwork.toysocialnetworkfx.domain.User;

/**
 * Validator for a user
 */
public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getId() == null || entity.getId() <= 0)
            errorMessage += "Invalid id\n";
        if (entity.getFirstName() == null || "".equals(entity.getFirstName()))
            errorMessage += "Invalid first name\n";
        if (entity.getLastName() == null || "".equals(entity.getLastName()))
            errorMessage += "Invalid last name\n";
        if (entity.getUsername() == null || "".equals(entity.getUsername()))
            errorMessage += "Invalid user name\n";
        if (entity.getPassword() == null || "".equals(entity.getPassword()))
            errorMessage += "Invalid password\n";

        if (errorMessage.length() > 0)
            throw new ValidationException(errorMessage);
    }
}
