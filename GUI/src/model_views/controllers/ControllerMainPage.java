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

    @FXML
    private void sendPublicMessage() {
        Main.user.getClient().setMessage(messageBoxPublic.getText(), Commands.PUBLIC_MESSAGE);
        Main.user.getClient().sendPublicMessage();
        messageBoxPublic.clear();
        update();
    }

    @FXML
    private void sendPrivateMessage() {
        Main.user.getClient().setMessage(messageBoxPrivate.getText(), Commands.PRIVATE_MESSAGE);
        Main.user.getClient().setSelectedUsernames(this.privateUsernames);
        Main.user.getClient().sendPrivateMessage();
        addPrivateChat();
        messageBoxPrivate.clear();
        actionTexts(Main.user.getClient().getLastMessage());
        update();
    }

    private Parent generatePrivateLabel(String name) {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        rectangle.setFill(Color.rgb(53, 255, 184));
        rectangle.setWidth(400);
        rectangle.setHeight(20);
        /////////////////////////////////////////////////////
        Text text = new Text(name);
        /////////////////////////////////////////////////////
        StackPane stack = new StackPane(rectangle, text);
        stack.setPadding(new Insets(10));
//        stack.setOnMouseClicked(mouseEvent -> {
//
//        });
        return stack;
    }

    private void addPrivateChat() {
        VBox vBox = (VBox) chats.getContent();
        this.privateUsernames.forEach(s -> {

            vBox.getChildren().add(generatePrivateLabel(s));
        });
    }

    public ControllerMainPage() {
        Main.user.getClient().start();
        update();
    }

    ArrayList<String> privateUsernames = new ArrayList<>();


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

    @FXML
    private void showUsers() {
        Main.user.getClient().getList();
        Message message = Main.user.getClient().getLastMessage();
        if (message == null) return;
        if (!message.getCommands().equals(Commands.GET_LIST)) return;
        VBox chatsContent = (VBox) this.chats.getContent();
        Rectangle chatRect = new Rectangle();
        chatRect.setFill(Color.rgb(133, 207, 116));
        chatRect.setArcWidth(20);
        chatRect.setArcHeight(20);
        StackPane chatStack = new StackPane();
        chatStack.setPadding(new Insets(10, 0, 10, 100));

        VBox vBox = (VBox) onlineUsers.getContent();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().clear();
        message.getUsernames().forEach(s -> {
            Rectangle rectangle = new Rectangle(293, 50);
            rectangle.setFill(Color.rgb(6, 188, 212));
            rectangle.setArcHeight(15);
            rectangle.setArcWidth(15);
            StackPane stackPane = new StackPane();
            stackPane.setPadding(new Insets(10, 0, 10, 100));
            stackPane.getChildren().add(rectangle);
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
//                    Label label1 = new Label(s);
//                    chatStack.getChildren().addAll(chatRect, label1);
//                    chatsContent.getChildren().add(chatStack);
                }

            });
            vBox.getChildren().add(stackPane);
        });

    }

    private void selectUsername(Rectangle rectangle) {
        rectangle.setFill(Color.rgb(175, 107, 204));
    }

    void showMessageOnMessageBox(MessageTypes messageTypes) {
        switch (messageTypes) {
            case PUBLIC:
                break;
            case PRIVATE:
                break;
            case OTHER:
        }

    }

    @FXML
    private void actionTexts(Message message) {
        if (message == null) return;
        System.out.println("from action text: " + message.getCommands());
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
                Rectangle rec = new Rectangle(300, 20);
                rec.setArcHeight(20);
                rec.setArcWidth(20);
                rec.setFill(Color.rgb(117, 126, 255));
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

    private String getUsernamesAsString(ArrayList<String> usernames) {
        StringBuilder builder = new StringBuilder();
        usernames.forEach(s -> builder.append(s).append(","));
        return builder.toString();
    }


    private HBox generateMessage(MessageTypes person, Message message) {
        System.out.println("from generate message: " + message.getCommands());
        String accessLevel = message.getCommands() == Commands.PRIVATE_MESSAGE ? "Private" : "Public";
        String usernames = getUsernamesAsString(message.getUsernames());
        Rectangle rec = new Rectangle(300, 100);
        rec.setArcHeight(20);
        rec.setArcWidth(20);
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
            rec.setFill(Color.rgb(53, 240, 122));
        } else if (person.equals(MessageTypes.ME) && message.getCommands().equals(Commands.PUBLIC_MESSAGE)) {
            header.setText(String.format("%s Message From Me: ", accessLevel));
            bodyMessage.getChildren().add(header);
            rec.setFill(Color.rgb(53, 240, 122));
        } else if (person.equals(MessageTypes.OTHER) && message.getCommands().equals(Commands.PUBLIC_MESSAGE)) {
            header.setText(String.format("%s Message From %s: ", accessLevel, message.getUsername()));
            bodyMessage.getChildren().add(header);
            rec.setFill(Color.rgb(0, 122, 203));
        } else if (person.equals(MessageTypes.OTHER) && message.getCommands().equals(Commands.PRIVATE_MESSAGE)) {
            header.setText(String.format("%s Message From %s: ", accessLevel, message.getUsername()));
            bodyMessage.getChildren().add(header);
            rec.setFill(Color.rgb(0, 122, 203));
        }
        bodyMessage.getChildren().add(text);
        stackPane.getChildren().addAll(rec, bodyMessage);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.getChildren().add(stackPane);

        return hBox;
    }


}
