package model_views.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.User;
import model_views.exceptions.AllFieldsEmpty;
import model_views.exceptions.InvalidPort;
import model_views.exceptions.UserAlreadyExists;
import views.Main;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ControllerSignUp {
    @FXML
    TextField username, fullName, password, port;
    @FXML
    Button submitButton;
    @FXML
    Text usernameError, portText;

    /**
     * create an anonymous socket for calling to server with "localhost" address and "21000" port
     * then check fields is empty or not
     * check username is exist or not
     * check port , should be 21000
     */
    @FXML
    private void tapOnSignUpButton() {
        try {
            new Socket("localhost", 21000);
            if (checkFields(username) && checkFields(password) && checkFields(fullName) && checkFields(port))
                throw new AllFieldsEmpty();
            checkUsername();
            checkPort();
            // create user that is in Main.user
            Main.user = new User(this.username.getText(),
                    this.port.getText(),
                    this.fullName.getText(),
                    this.password.getText());
            this.rootToMainPage();
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


    /**
     * root to main page and set default demonstration
     *
     * @throws IOException when not found address
     */
    private void rootToMainPage() throws IOException {
        Parent parent = FXMLLoader.load(new File("GUI/src/views/pages/main_page.fxml").toURI().toURL());
        Scene scene = new Scene(parent, 1200, 700);
        Main.mainStage.setScene(scene);
        // set username in stage title
        Main.mainStage.setTitle(String.format("ChatRoom - %s", username.getText()));
        Main.mainStage.show();
    }

    /**
     * @param textField get this to check its empty or not
     * @return true when its empty
     */
    private boolean checkFields(TextField textField) {
        return textField.getText().isEmpty();
    }

    /**
     * if username exist in server throw below UserAlreadyExists
     *
     * @throws UserAlreadyExists
     */

    private void checkUsername() throws UserAlreadyExists {
        String usernameText = username.getText();
//        if (ServerRunner.server.isExist(usernameText)) throw new UserAlreadyExists();
    }

    /**
     * checking port for listening only on port 21000
     *
     * @throws InvalidPort if port is not valid throw this exception
     */
    private void checkPort() throws InvalidPort {
        if (!this.port.getText().equals("21000")) throw new InvalidPort();
    }

    /**
     * showing error texts for invalid port
     */
    private void showPortErrorText() {
        String text = String.format("%s port is not valid", this.port.getText());
        this.portText.setText(text);
        this.portText.setVisible(true);
    }

    /**
     * set border to text fields
     */
    private void changeStrokeBorders() {
        String style = "-fx-text-box-border: #FF1D1D; -fx-focus-color: #00D8C6;";
        this.username.setStyle(style);
        this.fullName.setStyle(style);
        this.password.setStyle(style);
        this.port.setStyle(style);
    }

    /**
     * show error message text for existing username in server
     */
    private void showUsernameErrorText() {
        String text = String.format("username %s is Already Taken!", this.username.getText());
        this.usernameError.setText(text);
        this.usernameError.setVisible(true);
    }
}
