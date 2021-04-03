package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;

import java.io.File;

public class Main extends Application {
    // stage used in program
    public static Stage mainStage;
    // user used in program
    public static User user;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(new File("GUI/src/views/pages/sign_up.fxml").toURI().toURL());
        Scene scene = new Scene(root, 600, 400);
        initStageUI();
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(windowEvent -> {
            user.getClient().exit();
        });
    }

    /**
     *  set min width and height for init program
     *  set "ChatRoom" as stage's title
     *  load chat icon from resources
     */
    private void initStageUI() {
        mainStage.setMinWidth(600);
        mainStage.setMinHeight(400);
        mainStage.setTitle("ChatRoom");
        mainStage.getIcons().add(new Image("views/resources/pictures/icon.jpg"));
    }
}
