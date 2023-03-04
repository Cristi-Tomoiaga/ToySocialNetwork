package toysocialnetwork.toysocialnetworkfx.domain.validators;

import toysocialnetwork.toysocialnetworkfx.domain.Message;

/**
 * Validator for a Message
 */
public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getId() == null || entity.getId() <= 0)
            errorMessage += "Invalid id\n";
        if (entity.getFromUser() == null || entity.getFromUser() <= 0)
            errorMessage += "Invalid from user id\n";
        if (entity.getToUser() == null || entity.getToUser() <= 0)
            errorMessage += "Invalid to user id\n";
        if (entity.getMessageBody() == null || "".equals(entity.getMessageBody()))
            errorMessage += "Invalid message body\n";
        if (entity.getSentDate() == null)
            errorMessage += "Invalid sent date\n";

        if (errorMessage.length() > 0)
            throw new ValidationException(errorMessage);
    }
}
