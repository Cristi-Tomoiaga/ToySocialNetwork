package toysocialnetwork.toysocialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import toysocialnetwork.toysocialnetworkfx.MainApplication;
import toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.MessageService;
import toysocialnetwork.toysocialnetworkfx.service.ServiceException;
import toysocialnetwork.toysocialnetworkfx.service.UserService;

import java.io.IOException;

/**
 * Controller for Register View
 */
public class RegisterController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField firstNameField;

    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    /**
     * Sets the services for the controller
     *
     * @param userService       the user service
     * @param friendshipService the friendship service
     * @param messageService    the message service
     */
    public void setServices(UserService userService, FriendshipService friendshipService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
    }

    /**
     * Clears all the fields
     */
    private void clearFields() {
        usernameField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        passwordField.setText("");
    }

    /**
     * Handles the click on the register button
     */
    public void registerButtonOnAction() {
        try {
            userService.addUser(usernameField.getText().trim(), firstNameField.getText().trim(),
                    lastNameField.getText().trim(), passwordField.getText().trim());

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,
                    "The registration was successful", "To continue, please login");
            loginLinkOnAction();
        } catch (ValidationException | ServiceException e) {
            clearFields();
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    /**
     * Handles the click on the login link
     */
    public void loginLinkOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 397, 400);

            LoginController loginController = fxmlLoader.getController();
            loginController.setServices(userService, friendshipService, messageService);

            Stage stage = new Stage();
            stage.setTitle("ToySocialNetwork");
            stage.setScene(scene);

            usernameField.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
