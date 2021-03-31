package model_views.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;
import model.User;
import model_views.exceptions.AllFieldsEmpty;
import model_views.exceptions.InvalidPort;
import model_views.exceptions.UserAlreadyExists;
import views.Main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;

public class ControllerSignUp {
    @FXML
    TextField username, fullName, password, port;
    @FXML
    Button submitButton;
    @FXML
    Text text, usernameError, portText;

    @FXML
    private void signInTapped() {
        try {
            Parent root = FXMLLoader.load(new File("GUI/src/views/pages/sign_in.fxml").toURI().toURL());
            Scene scene = new Scene(root, 600, 400);
            Main.mainStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tapOnTextFields() {
        submitButton.setStyle("-fx-background-color: #8ED1FE;");
        submitButton.setText("SignUp");
    }

    @FXML
    private void tapOnSignUpButton() {
        try {
            new Socket("localhost", 21000);
        } catch (IOException e) {
            this.submitButton.setStyle("-fx-background-color: #D82528;");
            this.submitButton.setText("Server is Offline");
        }
        try {
            if (checkFields(username) && checkFields(password) && checkFields(fullName) && checkFields(port))
                throw new AllFieldsEmpty();
            checkUsername();
            checkPort();
            Main.user = new User(this.username.getText(),
                    this.port.getText(),
                    this.fullName.getText(),
                    this.password.getText());
            this.rootToMainPage(Main.user);
        } catch (UserAlreadyExists userAlreadyExists) {
            showUsernameErrorText();
        } catch (AllFieldsEmpty allFieldsEmpty) {
            changeStrokeBorders();
        } catch (InvalidPort invalidPort) {
            showPortErrorText();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void rootToMainPage(User user) throws IOException {
        Parent parent = FXMLLoader.load(new File("GUI/src/views/pages/main_page.fxml").toURI().toURL());
        Scene scene = new Scene(parent, 1200, 700);
        Main.mainStage.setScene(scene);
        Main.mainStage.setTitle(String.format("ChatRoom - %s", username.getText()));
        Main.mainStage.show();
    }

    private boolean checkFields(TextField textField) {
        return textField.getText().isEmpty();
    }

    private void checkUsername() throws UserAlreadyExists {
        String usernameText = username.getText();
    }

    private void checkPort() throws InvalidPort {
        if (!this.port.getText().equals("21000")) throw new InvalidPort();
    }


    private void showPortErrorText() {
        String text = String.format("%s port is not valid", this.port.getText());
        this.portText.setText(text);
        this.portText.setVisible(true);
    }


    private void changeTextFieldStroke(TextField textField) {
        textField.setStyle("-fx-text-box-border: #FF1D1D; -fx-focus-color: #00D8C6;");
    }

    private void changeStrokeBorders() {
        String style = "-fx-text-box-border: #FF1D1D; -fx-focus-color: #00D8C6;";
        this.username.setStyle(style);
        this.fullName.setStyle(style);
        this.password.setStyle(style);
        this.port.setStyle(style);
    }


    private void showUsernameErrorText() {
        String text = String.format("username %s is Already Taken!", this.username.getText());
        this.usernameError.setText(text);
        this.usernameError.setVisible(true);
    }

}
