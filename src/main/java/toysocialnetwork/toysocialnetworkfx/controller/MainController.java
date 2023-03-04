package toysocialnetwork.toysocialnetworkfx.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import toysocialnetwork.toysocialnetworkfx.MainApplication;
import toysocialnetwork.toysocialnetworkfx.domain.Message;
import toysocialnetwork.toysocialnetworkfx.domain.Sender;
import toysocialnetwork.toysocialnetworkfx.domain.Status;
import toysocialnetwork.toysocialnetworkfx.domain.User;
import toysocialnetwork.toysocialnetworkfx.domain.dto.FriendshipDTO;
import toysocialnetwork.toysocialnetworkfx.domain.dto.UserDTO;
import toysocialnetwork.toysocialnetworkfx.domain.validators.ValidationException;
import toysocialnetwork.toysocialnetworkfx.service.FriendshipService;
import toysocialnetwork.toysocialnetworkfx.service.MessageService;
import toysocialnetwork.toysocialnetworkfx.service.ServiceException;
import toysocialnetwork.toysocialnetworkfx.service.UserService;
import toysocialnetwork.toysocialnetworkfx.utils.Constants;
import toysocialnetwork.toysocialnetworkfx.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Controller for Main View
 */
public class MainController implements Observer {
    @FXML
    public TableView<UserDTO> tableViewFriends;
    @FXML
    public TableColumn<UserDTO, String> tableViewFriendsUsername;
    @FXML
    public TableColumn<UserDTO, String> tableViewFriendsFirstName;
    @FXML
    public TableColumn<UserDTO, String> tableViewFriendsLastName;
    @FXML
    public TableColumn<UserDTO, Void> tableViewFriendsActionUnfriend;

    @FXML
    public TableView<FriendshipDTO> tableViewRequests;
    @FXML
    public TableColumn<FriendshipDTO, String> tableViewRequestsFrom;
    @FXML
    public TableColumn<FriendshipDTO, String> tableViewRequestsDate;
    @FXML
    public TableColumn<FriendshipDTO, Status> tableViewRequestsStatus;
    @FXML
    public TableColumn<FriendshipDTO, Void> tableViewRequestsActionAccept;
    @FXML
    public TableColumn<FriendshipDTO, Void> tableViewRequestsActionDecline;

    @FXML
    public TableView<User> tableViewUsers;
    @FXML
    public TableColumn<User, String> tableViewUsersUsername;
    @FXML
    public TableColumn<User, String> tableViewUsersFirstName;
    @FXML
    public TableColumn<User, String> tableViewUsersLastName;

    @FXML
    public TableView<FriendshipDTO> tableViewSentRequests;
    @FXML
    public TableColumn<FriendshipDTO, String> tableViewSentRequestsTo;
    @FXML
    public TableColumn<FriendshipDTO, String> tableViewSentRequestsDate;
    @FXML
    public TableColumn<FriendshipDTO, Status> tableViewSentRequestsStatus;
    @FXML
    public TableColumn<FriendshipDTO, Void> tableViewSentRequestsActionDelete;

    @FXML
    public ListView<Message> listViewMessages;
    @FXML
    public TextField textFieldMessage;

    @FXML
    public Label welcomeLabel;
    @FXML
    public TextField usernameSearchbox;
    @FXML
    public Button buttonSendRequest;
    @FXML
    public Button buttonSendMessage;


    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private User currentUser;

    private final ObservableList<UserDTO> friendsModel = FXCollections.observableArrayList();
    private final ObservableList<FriendshipDTO> requestsModel = FXCollections.observableArrayList();
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();
    private final ObservableList<User> cachedUsersModel = FXCollections.observableArrayList();
    private final ObservableList<FriendshipDTO> sentRequestsModel = FXCollections.observableArrayList();
    private final ObservableList<Message> messagesModel = FXCollections.observableArrayList();

    /**
     * Updates the models
     */
    private void refreshModels() {
        int selectedFriendIndex = tableViewFriends.getSelectionModel().getSelectedIndex();
        UserDTO selectedFriend = tableViewFriends.getSelectionModel().getSelectedItem();

        friendsModel.setAll(userService.findFriendUsersFor(currentUser.getId()));
        requestsModel.setAll(friendshipService.getFriendshipRequestsFor(currentUser.getId()));
        usersModel.setAll(userService.findAvailableUsersFor(currentUser.getId()));
        cachedUsersModel.setAll(usersModel);
        sentRequestsModel.setAll(friendshipService.getFriendshipRequestsFrom(currentUser.getId()));
        usernameSearchbox.setText("");

        if (0 <= selectedFriendIndex && selectedFriendIndex < friendsModel.size()
                && selectedFriend != null && selectedFriend.getId().equals(friendsModel.get(selectedFriendIndex).getId())) {
            tableViewFriends.getSelectionModel().select(selectedFriendIndex);
        }

        refreshMessages();
    }

    /**
     * Updates the messages
     */
    private void refreshMessages() {
        UserDTO selectedFriend = tableViewFriends.getSelectionModel().getSelectedItem();

        if (selectedFriend != null) {
            messagesModel.setAll(messageService.getMessagesBetween(currentUser.getId(), selectedFriend.getId()));
        } else {
            messagesModel.clear();
        }
    }

    @Override
    public void update() {
        refreshModels();
    }

    @FXML
    public void initialize() {
        buttonSendRequest.disableProperty().bind(Bindings.isEmpty(tableViewUsers.getSelectionModel().getSelectedItems()));

        tableViewFriendsUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableViewFriendsFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableViewFriendsLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewFriendsActionUnfriend.setCellFactory(new Callback<>() {
            @Override
            public TableCell<UserDTO, Void> call(TableColumn<UserDTO, Void> param) {
                return new TableCell<>() {
                    final Button buttonUnfriend = new Button("Unfriend");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            buttonUnfriend.setOnAction(event ->
                                    unfriendButtonOnAction(getTableView().getItems().get(getIndex())));
                            setGraphic(buttonUnfriend);
                            setText(null);
                        }
                    }
                };
            }
        });
        tableViewFriends.setItems(friendsModel);

        tableViewRequestsFrom.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOtherUser().getUsername()));
        tableViewRequestsDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFriendsFrom().format(Constants.DATE_TIME_FORMATTER)));
        tableViewRequestsStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequestsActionAccept.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendshipDTO, Void> call(TableColumn<FriendshipDTO, Void> param) {
                return new TableCell<>() {
                    final Button buttonAccept = new Button("Accept");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            FriendshipDTO friendshipDTO = getTableView().getItems().get(getIndex());
                            buttonAccept.setOnAction(event ->
                                    acceptFriendRequestButtonOnAction(friendshipDTO));
                            buttonAccept.setDisable(friendshipDTO.getStatus() == Status.ACCEPTED);
                            setGraphic(buttonAccept);
                            setText(null);
                        }
                    }
                };
            }
        });
        tableViewRequestsActionDecline.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendshipDTO, Void> call(TableColumn<FriendshipDTO, Void> param) {
                return new TableCell<>() {
                    final Button buttonDecline = new Button("Decline");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            FriendshipDTO friendshipDTO = getTableView().getItems().get(getIndex());
                            buttonDecline.setOnAction(event ->
                                    deletePendingFriendRequest(friendshipDTO));
                            buttonDecline.setDisable(friendshipDTO.getStatus() == Status.ACCEPTED);
                            setGraphic(buttonDecline);
                            setText(null);
                        }
                    }
                };
            }
        });
        tableViewRequests.setItems(requestsModel);

        tableViewUsersUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableViewUsersFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableViewUsersLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewUsers.setItems(usersModel);

        tableViewSentRequestsTo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOtherUser().getUsername()));
        tableViewSentRequestsDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFriendsFrom().format(Constants.DATE_TIME_FORMATTER)));
        tableViewSentRequestsStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewSentRequestsActionDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendshipDTO, Void> call(TableColumn<FriendshipDTO, Void> param) {
                return new TableCell<>() {
                    final Button buttonDelete = new Button("Delete");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            FriendshipDTO friendshipDTO = getTableView().getItems().get(getIndex());
                            buttonDelete.setOnAction(event ->
                                    deletePendingFriendRequest(friendshipDTO));
                            buttonDelete.setDisable(friendshipDTO.getStatus() == Status.ACCEPTED);
                            setGraphic(buttonDelete);
                            setText(null);
                        }
                    }
                };
            }
        });
        tableViewSentRequests.setItems(sentRequestsModel);

        buttonSendMessage.disableProperty().bind(Bindings.isEmpty(textFieldMessage.textProperty()));
        textFieldMessage.disableProperty().bind(Bindings.isEmpty(tableViewFriends.getSelectionModel().getSelectedItems()));
        listViewMessages.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<>() {
                    final HBox parent = new HBox();
                    final VBox content = new VBox();
                    final Text message = new Text();
                    final Text date = new Text();

                    {
                        parent.getChildren().add(content);
                        content.getChildren().add(message);
                        content.getChildren().add(date);
                        getStyleClass().add("custom-list-cell");
                        date.getStyleClass().add("date-text");
                    }

                    @Override
                    protected void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            message.setText(item.getMessageBody());
                            date.setText(item.getSentDate().format(Constants.DATE_TIME_FORMATTER));
                            content.setAlignment(currentUser.getId().equals(item.getFromUser()) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                            setText(null);
                            setGraphic(content);
                            updateSelected(false);
                        }
                    }
                };
            }
        });
        listViewMessages.setItems(messagesModel);
        tableViewFriends.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> refreshMessages());

        usernameSearchbox.textProperty().addListener((observable, oldValue, newValue) -> {
            usersModel.setAll(cachedUsersModel);

            usersModel.setAll(usersModel.stream()
                    .filter(user -> user.getUsername().contains(newValue.trim()))
                    .collect(Collectors.toList())
            );
        });
    }

    /**
     * Sets the services and current user for the controller
     *
     * @param userService       the user service
     * @param friendshipService the friendship service
     * @param messageService    the message service
     * @param user              the current user
     */
    public void setServicesUser(UserService userService, FriendshipService friendshipService, MessageService messageService, User user) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.currentUser = user;

        this.userService.addObserver(this);
        this.friendshipService.addObserver(this);
        this.messageService.addObserver(this);
        refreshModels();
        welcomeLabel.setText("ToySocialNetwork. Welcome, " + currentUser.getFirstName() + "!");
    }

    /**
     * Handles the click on the unfriend button
     *
     * @param userDTO the selected UserDTO object
     */
    public void unfriendButtonOnAction(UserDTO userDTO) {
        if (userDTO != null) {
            friendshipService.deleteFriendship(userDTO.getFriendshipId());

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Unfriended the selected user", "");
        }
    }

    /**
     * Handles the click on the accept friend request button
     *
     * @param friendshipDTO the selected FriendshipDTO object
     */
    public void acceptFriendRequestButtonOnAction(FriendshipDTO friendshipDTO) {
        if (friendshipDTO != null && friendshipDTO.getStatus() == Status.PENDING) {
            friendshipService.modifyFriendship(friendshipDTO.getId(), LocalDateTime.now(), Status.ACCEPTED);
        }
    }

    /**
     * Deletes a pending friend request from the application
     *
     * @param friendshipDTO the FriendshipDTO object representing the friend request to be deleted
     */
    public void deletePendingFriendRequest(FriendshipDTO friendshipDTO) {
        if (friendshipDTO != null && friendshipDTO.getStatus() == Status.PENDING) {
            friendshipService.deleteFriendship(friendshipDTO.getId());
        }
    }

    /**
     * Handles the click on the send friend request button
     */
    public void sendFriendRequestOnAction() {
        User user = tableViewUsers.getSelectionModel().getSelectedItem();

        if (user != null) {
            if (currentUser.getId() < user.getId())
                friendshipService.addFriendship(currentUser.getId(), user.getId(),
                        LocalDateTime.now(), Status.PENDING, Sender.FIRST);
            else
                friendshipService.addFriendship(user.getId(), currentUser.getId(),
                        LocalDateTime.now(), Status.PENDING, Sender.SECOND);

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sent friend request", "");
        }
    }

    /**
     * Handles the click on the send message button
     */
    public void sendMessageButtonOnAction() {
        UserDTO selectedFriend = tableViewFriends.getSelectionModel().getSelectedItem();

        if (textFieldMessage.getText() != null && selectedFriend != null) {
            try {
                messageService.addMessage(currentUser.getId(), selectedFriend.getId(),
                        textFieldMessage.getText(), LocalDateTime.now());
            } catch (ValidationException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }

            textFieldMessage.setText("");
        }
    }

    /**
     * Handles the click on the new login button
     */
    public void newLoginButtonOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 397, 400);

            LoginController loginController = fxmlLoader.getController();
            loginController.setServices(userService, friendshipService, messageService);

            Stage stage = new Stage();
            stage.setTitle("ToySocialNetwork");
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the click on the logout button
     */
    public void logoutButtonOnAction() {
        this.currentUser = null;
        this.userService.removeObserver(this);
        this.friendshipService.removeObserver(this);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 397, 400);

            LoginController loginController = fxmlLoader.getController();
            loginController.setServices(userService, friendshipService, messageService);

            Stage stage = new Stage();
            stage.setTitle("ToySocialNetwork");
            stage.setScene(scene);

            usernameSearchbox.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
