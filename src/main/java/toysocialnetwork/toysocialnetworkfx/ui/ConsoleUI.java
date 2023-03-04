package toysocialnetwork.toysocialnetworkfx.ui;

import toysocialnetwork.toysocialnetworkfx.domain.Friendship;
import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.ServiceException;
import toysocialnetwork.toysocialnetworkfx.service.UserService;
import toysocialnetwork.toysocialnetworkfx.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Implementation of a user interface in the console
 */
public class ConsoleUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for ConsoleUI
     *
     * @param userService       reference to the user service
     * @param friendshipService reference to the friendship service
     */
    public ConsoleUI(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * Runs the console user interface
     */
    public void run() {
        System.out.println("Welcome to ToySocialNetwork!\n");
        printMenu();

        boolean running = true;
        while (running) {
            String option = getOption();

            switch (option) {
                case "printUsers" -> printUsers();
                case "addUser" -> addUser();
                case "modifyUser" -> modifyUser();
                case "deleteUser" -> deleteUser();
                case "printFriendships" -> printFriendships();
                case "addFriendship" -> addFriendship();
                case "modifyFriendship" -> modifyFriendship();
                case "deleteFriendship" -> deleteFriendship();
                case "communities" -> communities();
                case "mostSociable" -> mostSociable();
                case "help" -> printMenu();
                case "exit" -> running = false;
                default -> System.out.println("Invalid command: " + option);
            }
        }
    }

    /**
     * Prints the users stored in the application
     */
    private void printUsers() {
        Iterable<User> users = userService.findAllUsers();

        System.out.println("Users list:");
        users.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Adds a new user to the application
     */
    private void addUser() {
        System.out.println("User's username: ");
        String username = scanner.nextLine().trim();

        System.out.print("User's first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("User's last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.println("User's password: ");
        String password = scanner.nextLine().trim();

        try {
            userService.addUser(username, firstName, lastName, password);
            System.out.println("Added the user successfully\n");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Modifies an existing user from the application
     */
    private void modifyUser() {
        System.out.print("User's id: ");
        String stringId = scanner.nextLine().trim();

        System.out.print("User's first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("User's last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.println("User's password:");
        String password = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(stringId);

            userService.modifyUser(id, firstName, lastName, password);
            System.out.println("User modified successfully\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for id\n");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an existing user from the application
     */
    private void deleteUser() {
        System.out.print("User's id: ");
        String stringId = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(stringId);

            userService.deleteUser(id);
            System.out.println("User deleted successfully\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for id\n");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints the friendships stored in the application
     */
    private void printFriendships() {
        Iterable<Friendship> friendships = friendshipService.findAllFriendships();

        System.out.println("Friendships list:");
        friendships.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Adds a new friendship to the application
     */
    private void addFriendship() {
        System.out.print("Friendship's first user id: ");
        String stringFirstId = scanner.nextLine().trim();

        System.out.print("Friendship's second user id: ");
        String stringSecondId = scanner.nextLine().trim();

        try {
            Long firstUser = Long.parseLong(stringFirstId);
            Long secondUser = Long.parseLong(stringSecondId);


            friendshipService.addFriendship(firstUser, secondUser, LocalDateTime.now(), Status.ACCEPTED, Sender.FIRST);
            System.out.println("Added the friendship successfully\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for id\n");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Modifies an existing friendship from the application
     */
    private void modifyFriendship() {
        System.out.print("Friendship's id: ");
        String stringId = scanner.nextLine().trim();

        System.out.print("Friendship's new friends from date: ");
        String stringFriendsFrom = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(stringId);
            LocalDateTime friendsFrom = LocalDateTime.parse(stringFriendsFrom, Constants.DATE_TIME_FORMATTER);

            friendshipService.modifyFriendship(id, friendsFrom, Status.ACCEPTED);
            System.out.println("Modified friendship successfully\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for id\n");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid value for date\n");
        } catch (ValidationException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an existing friendship from the application
     */
    private void deleteFriendship() {
        System.out.print("Friendship's id: ");
        String stringId = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(stringId);

            friendshipService.deleteFriendship(id);
            System.out.println("Friendship deleted successfully\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for id\n");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints the number of the communities in the network
     */
    private void communities() {
        System.out.println("Community count: " + friendshipService.getCommunitiesCount() + "\n");
    }

    /**
     * Prints the most sociable community in the network
     */
    private void mostSociable() {
        List<User> community = friendshipService.getMostSociableCommunity();

        if (community.size() == 0) {
            System.out.println("No community\n");

            return;
        }

        System.out.println("The most sociable community: ");
        for (User user : community) {
            System.out.println(user);
        }
        System.out.println();
    }

    /**
     * Prints the menu of the application
     */
    private void printMenu() {
        String menu = """
                Menu:
                 [Users]
                  printUsers - prints all the users stored in the application
                  addUser - adds a new user
                  modifyUser - modifies an existing user
                  deleteUser - deletes an existing user
                 
                 [Friendships]
                  printFriendships - prints all the friendships stored in the application
                  addFriendship - adds a new friendship
                  modifyFriendship - modifies an existing friendship
                  deleteFriendship - deletes an existing friendship
                  
                 [Statistics]
                  communities - shows the number of communities in the network
                  mostSociable - shows the most sociable community in the network
                  
                 [Misc]
                  help - prints this menu
                  exit - exits the application
                """;

        System.out.println(menu);
    }

    /**
     * Reads a menu option from the user
     *
     * @return the option chosen by the user
     */
    private String getOption() {
        System.out.print("admin@toy_social_network:$ ");

        return scanner.nextLine().trim();
    }
}
