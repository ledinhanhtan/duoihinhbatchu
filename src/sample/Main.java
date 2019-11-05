package sample;

import controller.BeginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlPath = "/gui/begin.fxml";
        URL location = getClass().getResource(fxmlPath);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Scene beginScene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("ĐUỔI HÌNH BẮT CHỮ");
        primaryStage.setScene(beginScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        ((BeginController) fxmlLoader.getController()).setStageAndScene(primaryStage, beginScene);
    }
}
