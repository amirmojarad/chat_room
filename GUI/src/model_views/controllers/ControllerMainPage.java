package model_views.controllers;

import com.amirmjrd.Commands;
import com.amirmjrd.parser.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model_views.utils.MessageTypes;
import views.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ControllerMainPage {
    @FXML
    Button send, deleteAll, showUsers;
    @FXML
    TextArea messageBox;
    @FXML
    ScrollPane messagesView, chats, onlineUsers;

    @FXML
    private void sendMessage() {
        Main.user.getClient().setMessage(messageBox.getText());
        Main.user.getClient().sendPublicMessage();
        messageBox.clear();
        update();
    }

    public ControllerMainPage() {
        Main.user.getClient().start();
        update();
    }

    ArrayList<String> privateUsernames = new ArrayList<>();

    private void getUsersList() {
//        VBox vBox = (VBox) onlineUsers.getContent();
//        ArrayList<String> list = new ArrayList<>();
//        list.addAll(Main.user.getClient().get)
//
    }

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
        VBox vBox = (VBox) messagesView.getContent();
        switch (message.getCommands()) {
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

    private HBox generateMessage(MessageTypes messageTypes, Message message) {
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
        switch (messageTypes) {
            case ME:
                header.setText("Public Message From Me: ");
                bodyMessage.getChildren().add(header);
                rec.setFill(Color.rgb(53, 240, 122));
                break;
            case OTHER:
                header.setText(String.format("Public Message From %s: ", message.getUsername()));
                bodyMessage.getChildren().add(header);
                rec.setFill(Color.rgb(0, 122, 203));
                break;
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
