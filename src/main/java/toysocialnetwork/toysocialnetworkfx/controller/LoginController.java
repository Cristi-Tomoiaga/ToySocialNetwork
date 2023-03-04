package toysocialnetwork.toysocialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import toysocialnetwork.toysocialnetworkfx.MainApplication;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.MessageService;
import toysocialnetwork.toysocialnetworkfx.service.UserService;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for Login View
 */
public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

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
        passwordField.setText("");
    }

    /**
     * Handles the click on the login button
     */
    public void loginButtonOnAction() {
        Optional<User> user = userService.login(usernameField.getText().trim(), passwordField.getText().trim());

        if (user.isPresent()) {
            User currentUser = user.get();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 768, 526);

                MainController mainController = fxmlLoader.getController();
                mainController.setServicesUser(userService, friendshipService, messageService, currentUser);

                Stage stage = new Stage();
                stage.setTitle("ToySocialNetwork");
                stage.setScene(scene);

                usernameField.getScene().getWindow().hide();
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            clearFields();
            MessageAlert.showErrorMessage(null, "Invalid user or password");
        }
    }

    /**
     * Handles the click on the register link
     */
    public void registerLinkOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 397, 400);

            RegisterController registerController = fxmlLoader.getController();
            registerController.setServices(userService, friendshipService, messageService);

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
