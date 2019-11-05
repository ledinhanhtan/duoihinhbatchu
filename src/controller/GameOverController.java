package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOverController {
    private Stage stage;
    private Scene beginScene;

    void setStageAndScene(Stage stage, Scene beginScene) {
        this.stage = stage;
        this.beginScene = beginScene;
    }

    public void playAgainBtnClicked() {
        stage.setScene(beginScene);
    }
}
