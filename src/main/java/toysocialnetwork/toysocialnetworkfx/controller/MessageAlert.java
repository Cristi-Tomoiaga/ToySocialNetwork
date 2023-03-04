package toysocialnetwork.toysocialnetworkfx.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Helper class for showing alert messages
 */
public class MessageAlert {
    /**
     * Shows a Message
     *
     * @param owner  the stage that owns this alert message
     * @param type   the type of the alert message
     * @param header the header text of the alert message
     * @param text   the body of the alert message
     */
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.initOwner(owner);

        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    /**
     * Shows an Error Message
     *
     * @param owner the stage that owns this alert message
     * @param text  the body of the alert message
     */
    public static void showErrorMessage(Stage owner, String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);

        message.setTitle("Error message");
        message.setContentText(text);
        message.showAndWait();
    }
}
