package toysocialnetwork.toysocialnetworkfx;

import toysocialnetwork.toysocialnetworkfx.config.ApplicationContext;
import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.validators.FriendshipValidator;
import toysocialnetwork.toysocialnetworkfx.domain.validators.UserValidator;
import toysocialnetwork.toysocialnetworkfx.domain.validators.Validator;
import toysocialnetwork.toysocialnetworkfx.repository.database.FriendshipDBRepository;
import toysocialnetwork.toysocialnetworkfx.repository.database.UserDBRepository;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.UserService;
import toysocialnetwork.toysocialnetworkfx.ui.ConsoleUI;

/**
 * Main application class
 */
public class Main {

    /**
     * Runs the application in CLI mode
     */
    private static void runCLI() {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();

        UserDBRepository userRepository = new UserDBRepository(userValidator,
                ApplicationContext.getConfigProperties().getProperty("data.db.url"),
                ApplicationContext.getConfigProperties().getProperty("data.db.user"),
                ApplicationContext.getConfigProperties().getProperty("data.db.password"));
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository(friendshipValidator,
                ApplicationContext.getConfigProperties().getProperty("data.db.url"),
                ApplicationContext.getConfigProperties().getProperty("data.db.user"),
                ApplicationContext.getConfigProperties().getProperty("data.db.password"));

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);
        ConsoleUI cli = new ConsoleUI(userService, friendshipService);

        cli.run();
    }

    /**
     * Runs the application in GUI mode
     *
     * @param args: array of command line arguments
     */
    private static void runGUI(String[] args) {
        MainApplication.main(args);
    }

    public static void main(String[] args) {
        //runCLI();
        runGUI(args);
    }
}