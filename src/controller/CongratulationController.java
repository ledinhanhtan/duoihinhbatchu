package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import quiz.Quiz;
import textfieldholder.TextFieldToShow;

public class CongratulationController {
    private Stage stage;
    private Scene scene;

    @FXML
    private FlowPane answerHolder;
    @FXML
    private Label rubyIncrementLabel;
    @FXML
    private Button nextButton;


    void setStageAndScene(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    void setAnswer(int currentLevel) {
        String answer = Quiz.getAnswer(currentLevel);
        String[] token = answer.split("\\s");

        try {//Wrap in try catch block so regardless it go out of token[], ignore it
            for (int i = 0; i < 4; i++) {
                HBox tempHBox = new HBox(4);
                for (int x = 0; x < token[i].length(); x++) {
                    String tempString = Character.toString(token[i].charAt(x));
                    TextFieldToShow textFieldToShow = new TextFieldToShow(tempString);
                    tempHBox.getChildren().add(textFieldToShow);
                }
                answerHolder.getChildren().add(tempHBox);
            }
        } catch (Exception ignore) {}

        //Optional: request focus on button
        nextButton.requestFocus();
    }

    void setRubyIncrement(int rubyIncrement) {
        rubyIncrementLabel.setText("+" + rubyIncrement);
    }

    public void nextBtnClicked() {
        stage.setScene(scene);
        answerHolder.getChildren().clear();
    }
}
