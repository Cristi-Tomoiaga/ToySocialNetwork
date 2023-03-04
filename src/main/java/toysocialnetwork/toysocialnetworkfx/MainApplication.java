package toysocialnetwork.toysocialnetworkfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import toysocialnetwork.toysocialnetworkfx.config.ApplicationContext;
import toysocialnetwork.toysocialnetworkfx.controller.LoginController;
import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.Message;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.validators.FriendshipValidator;
import toysocialnetwork.toysocialnetworkfx.domain.validators.MessageValidator;
import toysocialnetwork.toysocialnetworkfx.domain.validators.UserValidator;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.database.FriendshipDBRepository;
import toysocialnetwork.toysocialnetworkfx.repository.database.MessageDBRepository;
import toysocialnetwork.toysocialnetworkfx.repository.database.UserDBRepository;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.MessageService;
import toysocialnetwork.toysocialnetworkfx.service.UserService;

import java.io.IOException;

/**
 * Main application class for GUI
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 397, 400);

        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Validator<Message> messageValidator = new MessageValidator();

        UserDBRepository userRepository = new UserDBRepository(userValidator,
                ApplicationContext.getConfigProperties().getProperty("data.db.url"),
                ApplicationContext.getConfigProperties().getProperty("data.db.user"),
                ApplicationContext.getConfigProperties().getProperty("data.db.password"));
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository(friendshipValidator,
                ApplicationContext.getConfigProperties().getProperty("data.db.url"),
                ApplicationContext.getConfigProperties().getProperty("data.db.user"),
                ApplicationContext.getConfigProperties().getProperty("data.db.password"));
        MessageDBRepository messageDBRepository = new MessageDBRepository(messageValidator,
                ApplicationContext.getConfigProperties().getProperty("data.db.url"),
                ApplicationContext.getConfigProperties().getProperty("data.db.user"),
                ApplicationContext.getConfigProperties().getProperty("data.db.password"));

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);
        MessageService messageService = new MessageService(messageDBRepository);

        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(userService, friendshipService, messageService);

        stage.setTitle("ToySocialNetwork");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}