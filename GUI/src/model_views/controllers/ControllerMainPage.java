package model_views.controllers;

import com.amirmjrd.Commands;
import com.amirmjrd.parser.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model_views.utils.MessageTypes;
import views.Main;

import java.util.ArrayList;


public class ControllerMainPage {
    @FXML
    Button sendPublic, showUsers, sendPrivate;
    @FXML
    TextArea messageBoxPublic, messageBoxPrivate;
    @FXML
    ScrollPane messagesView, chats, onlineUsers;
    //selected usernames

    ArrayList<String> privateUsernames = new ArrayList<>();
    Color privateMessage = Color.rgb(190, 225, 252);
    Color publicMessage = Color.rgb(135, 255, 165);
    Color serverMessage = Color.rgb(135, 255, 225);
    Color onlineUser = Color.rgb(0, 200, 255);
    Color selectedOnlineUser = Color.rgb(122, 226, 255);
    Color privateChat = Color.rgb(109, 9, 231);

    /**
     * set message fo client by public conditions
     * and send message by client
     * clear textFields
     * then update the message box
     */
    @FXML
    private void sendPublicMessage() {
        Main.user.getClient().setMessage(messageBoxPublic.getText(), Commands.PUBLIC_MESSAGE);
        Main.user.getClient().sendPublicMessage();
        messageBoxPublic.clear();
        update();
    }

    /**
     * set message fo client by private conditions
     * and send message by client
     * clear textFields
     * then update the message box
     */
    @FXML
    private void sendPrivateMessage() {
        if (privateUsernames.isEmpty()) return;
        Main.user.getClient().setMessage(messageBoxPrivate.getText(), Commands.PRIVATE_MESSAGE);
        Main.user.getClient().setSelectedUsernames(this.privateUsernames);
        Main.user.getClient().sendPrivateMessage();
        addPrivateChat();
        messageBoxPrivate.clear();
        actionTexts(Main.user.getClient().getLastMessage());
        update();
    }

    /**
     * generate label for private chats that client has connection with them
     *
     * @param name username
     * @return for set in vbox(chats)
     */
    private Parent generatePrivateLabel(String name) {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(privateChat);
        rectangle.setWidth(400);
        rectangle.setHeight(100);
        /////////////////////////////////////////////////////
        Text text = new Text(name);
        /////////////////////////////////////////////////////
        StackPane stack = new StackPane(rectangle, text);
        stack.setPadding(new Insets(10));
        return stack;
    }

    /**
     * add private chats by username in chats area
     * first clear the list, then add labels
     */
    private void addPrivateChat() {
        VBox vBox = (VBox) chats.getContent();
        vBox.getChildren().clear();
        this.privateUsernames.forEach(s -> vBox.getChildren().add(generatePrivateLabel(s)));
    }

    /**
     * default ControllerMainPage
     * start client program thread and update the ui
     */
    public ControllerMainPage() {
        Main.user.getClient().start();
        update();
    }

    /**
     * run a thread with infinite loop
     * if client messages is not empty
     * set action text
     */
    @FXML
    private void update() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!Main.user.getClient().isEmpty())
                    actionTexts(Main.user.getClient().getLastMessage());
                else break;
            }
        });
        Platform.runLater(thread);
    }

    /**
     * send GET_LIST request to server
     * get message that received
     */

    @FXML
    private void showUsers() {
        Main.user.getClient().getList();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message message = Main.user.getClient().getLastMessage();
        if (message == null) return;
        if (!message.getCommands().equals(Commands.GET_LIST)) return;
        VBox vBox = (VBox) onlineUsers.getContent();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().clear();
        message.getUsernames().forEach(s -> {
            // create rectangle
            Rectangle rectangle = makeRectangle(onlineUser, 293, 50, 15, 15);
            // create stack pane
            StackPane stackPane = new StackPane(rectangle);
            stackPane.setPadding(new Insets(10, 0, 10, 100));
            // create label , if username is equal to this client username , add ME as in first of label text
            Label label = new Label();
            if (s.equals(Main.user.getUsername()))
                label.setText(String.format("ME as %s", s));
            else label.setText(s);
            stackPane.getChildren().add(label);
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setOnMouseClicked(mouseEvent -> {
                if (!label.getText().contains("ME")) {
                    selectUsername(rectangle);
                    this.privateUsernames.add(s);
                }
            });
            vBox.getChildren().add(stackPane);
        });

    }

    /**
     * return selected rectangle as different color
     *
     * @param rectangle
     */
    private void selectUsername(Rectangle rectangle) {
        rectangle.setFill(selectedOnlineUser);
    }

    /**
     * Private:
     * - if message is from this client, add message from ME type
     * - else add message as OTHER type
     * Public:
     * - if message is from this client, add message from ME type
     * - else add message as OTHER type
     * Handshake & Exit:
     * - these messages are from server and has different type
     *
     * @param message take it and decide what kind of message is
     */
    @FXML
    private void actionTexts(Message message) {
        if (message == null) return;
        VBox vBox = (VBox) messagesView.getContent();
        switch (message.getCommands()) {
            case PRIVATE_MESSAGE:
                if (message.getUsername().equals(Main.user.getUsername()))
                    vBox.getChildren().add(generateMessage(MessageTypes.ME, message));
                else {
                    vBox.getChildren().add(generateMessage(MessageTypes.OTHER, message));
                }
                break;
            case PUBLIC_MESSAGE:
                if (message.getUsername().equals(Main.user.getUsername())) {
                    vBox.getChildren().add(generateMessage(MessageTypes.ME, message));
                } else {
                    vBox.getChildren().add(generateMessage(MessageTypes.OTHER, message));
                }
                break;
            case HANDSHAKE:
            case EXIT:
                Rectangle rec = makeRectangle(serverMessage, 300, 20, 20, 20);
                StackPane stackPane = new StackPane();
                stackPane.setAlignment(Pos.CENTER);
                Text textNode = new Text(message.getRawMessage());
                textNode.setTextAlignment(TextAlignment.CENTER);
                textNode.setWrappingWidth(300);
                stackPane.getChildren().addAll(rec, textNode);
                stackPane.setPadding(new Insets(10, 0, 0, 10));
                vBox.getChildren().add(stackPane);
                break;
        }
    }

    /**
     * @param usernames these usernames are for private message selected
     * @return generate usernames string by comma separating
     */
    private String getUsernamesAsString(ArrayList<String> usernames) {
        StringBuilder builder = new StringBuilder();
        usernames.forEach(s -> builder.append(s).append(","));
        return builder.toString();
    }

    /**
     * @param person  Other or Me!
     * @param message
     * @return
     */
    private HBox generateMessage(MessageTypes person, Message message) {
        String accessLevel = message.getCommands() == Commands.PRIVATE_MESSAGE ? "Private" : "Public";
        String usernames = getUsernamesAsString(message.getUsernames());
        Rectangle rec = makeRectangle(Color.WHITE, 300, 100, 20, 20);
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setMargin(stackPane, new Insets(20));
        VBox bodyMessage = new VBox();
        Text text = new Text(message.getBodyMessage());
        text.setWrappingWidth(300);
        Text header = new Text();
        header.setFill(Color.rgb(255, 255, 255));
        header.setStyle("-fx-font-weight: bold");
        if (person.equals(MessageTypes.ME) && message.getCommands().equals(Commands.PRIVATE_MESSAGE)) {
            header.setText(String.format("%s Message From Me to %s: ", accessLevel, usernames));
            bodyMessage.getChildren().add(header);
            rec.setFill(publicMessage);
        } else if (person.equals(MessageTypes.ME) && message.getCommands().equals(Commands.PUBLIC_MESSAGE)) {
            header.setText(String.format("%s Message From Me: ", accessLevel));
            bodyMessage.getChildren().add(header);
            rec.setFill(publicMessage);
        } else if (person.equals(MessageTypes.OTHER) && message.getCommands().equals(Commands.PUBLIC_MESSAGE)) {
            header.setText(String.format("%s Message From %s: ", accessLevel, message.getUsername()));
            bodyMessage.getChildren().add(header);
            rec.setFill(privateMessage);
        } else if (person.equals(MessageTypes.OTHER) && message.getCommands().equals(Commands.PRIVATE_MESSAGE)) {
            header.setText(String.format("%s Message From %s: ", accessLevel, message.getUsername()));
            bodyMessage.getChildren().add(header);
            rec.setFill(privateMessage);
        }
        bodyMessage.getChildren().add(text);
        stackPane.getChildren().addAll(rec, bodyMessage);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.getChildren().add(stackPane);
        return hBox;
    }

    private Rectangle makeRectangle(Color color, double width, double height) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(color);
        return rectangle;
    }

    private Rectangle makeRectangle(Color color, double width, double height, double arcWidth, double arcHeight) {
        Rectangle rectangle = makeRectangle(color, width, height);
        rectangle.setArcWidth(arcWidth);
        rectangle.setArcHeight(arcHeight);
        return rectangle;
    }


}
