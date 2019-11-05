package textfieldholder;

import javafx.animation.RotateTransition;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import soundmaker.SoundMaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TextFieldHolder extends FlowPane implements Serializable {

    private enum Colors {
        RED("-fx-text-fill: #FF0000"),
        GREEN("-fx-text-fill: #00FF00"),
        AQUA("-fx-text-fill: #00FFFF"),
        BLACK("-fx-text-fill: #000000");

        private final String css;

        Colors(final String css) {
            this.css = css;
        }

        @Override
        public String toString() {
            return css;
        }
    }

    private ArrayList<PTextField> quizTFList;
    private int indexFocused;

    public TextFieldHolder() {
        super(Orientation.HORIZONTAL, 10, 10);
        formatTextFieldHolder();

        quizTFList = new ArrayList<>();
    }

    private void formatTextFieldHolder() {
        this.setMaxWidth(500);
        this.setAlignment(Pos.CENTER);
    }

    public void add(PTextField tfToAdd) {
        this.getChildren().addAll(tfToAdd);
        quizTFList.add(tfToAdd);
    }

    public void setupTextField() {
        for (PTextField tfToAddListener : quizTFList) {
            //Move to next TextField after write a character
            tfToAddListener.textProperty().addListener((observable, oldValue, newValue) -> {
                //Always focus on the first text field which is empty
                for (PTextField tfToFocus : quizTFList) {
                    if (tfToFocus.getText().equals("")) {
                        tfToFocus.requestFocus();
                        break;
                    }
                }
            });

            //Get the index of the focused text field
            tfToAddListener.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    indexFocused = tfToAddListener.getIndex();
                }
            });


            tfToAddListener.setOnKeyPressed(event -> {
                //Play sound if only the key from A-Z
                if (event.getCode().getName().matches("[A-Z]")) { SoundMaker.click(); }
                //Move between text field using Arrow key
                try {
                    switch (event.getCode()) {
                        case LEFT: quizTFList.get(indexFocused-1).requestFocus();
                            break;
                        case RIGHT: quizTFList.get(indexFocused+1).requestFocus();
                            break;
                        default:
                            break;
                    }
                } catch (Exception ignored) {} //Only move inside textFieldHolder scope
            });
        }
    }

    //Rebuild text fields which are revealed by using Hint
    public void rebuild(Map<Integer, String> gameStateMap) {
        for (Map.Entry<Integer, String> entry : gameStateMap.entrySet()) {
            PTextField textField = quizTFList.get(entry.getKey());
            textField.setText(entry.getValue());
            textField.setStyle(Colors.AQUA.toString());
            textField.setEditable(false);
        }
    }

    public void focusOnFirstTextField() {
        quizTFList.get(0).requestFocus();
    }

    public ArrayList<PTextField> getList() {
        return quizTFList;
    }

    public boolean allFilled() {
        boolean allFilled = true;
        for (PTextField textField : quizTFList) {
            if (textField.getText().equals("")) {
                allFilled = false;
                break;
            }
        }
        return allFilled;
    }

    public PTextField getFirstEmptyTextField() {
        PTextField textFieldToReturn = null;
        for (PTextField temp : quizTFList) {
            if (temp.getText().equals("")) {
                textFieldToReturn = temp;
                break;
            }
        }
        return textFieldToReturn;
    }

    public String getCharacterOfFirstEmptyTextField(String quiz) {
        String character = null;
        try {
            character = Character.toString(quiz.charAt(getFirstEmptyTextField().getIndex()));
        } catch (Exception ignore) {}
        return character;
    }

    public void revealTextField(String quiz) {
        for (PTextField emptyTextField : quizTFList) {
            if (emptyTextField.getText().equals("")) {
                int index = emptyTextField.getIndex();
                emptyTextField.setText(Character.toString(quiz.charAt(index)));
                emptyTextField.setStyle(Colors.AQUA.toString());
                emptyTextField.setEditable(false);
                break;
            }
        }
    }

    public void setToGreen() {
        SoundMaker.correct();
        jiggle();
        for (PTextField temp : quizTFList) {
            temp.setStyle(Colors.GREEN.toString());
        }
    }

    public void setToRed() {
        SoundMaker.wrong();
        jiggle();
        for (PTextField temp : quizTFList) {
            temp.setStyle(Colors.RED.toString());
        }
        Timer timer = new Timer();
        timer.schedule(new setToBlackTimer(), 1000);
    }

    private class setToBlackTimer extends TimerTask {
        @Override
        public void run() {
            setToBlack();
        }
        private void setToBlack() {
            for (PTextField temp : quizTFList) {
                if (!temp.isEditable()) {temp.setStyle(Colors.AQUA.toString());}
                else {temp.setStyle(Colors.BLACK.toString());}
            }
        }
    }

    private void jiggle() {
        for (PTextField textField : quizTFList) {
            RotateTransition rotate = new RotateTransition(Duration.millis(120), textField);
            rotate.setFromAngle(0);
            rotate.setToAngle(15);
            rotate.setCycleCount(6);
            rotate.setAutoReverse(true);
            rotate.play();
        }
    }

    public void reset() {
        this.getChildren().clear();
        quizTFList.clear();
    }
}