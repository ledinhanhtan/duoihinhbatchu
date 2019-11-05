package buttonholder;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import soundmaker.SoundMaker;
import textfieldholder.TextFieldHolder;

import java.util.ArrayList;

public class ButtonHolder extends GridPane {
    private ArrayList<String> charList;
    private ArrayList<Button> btnList;

    public ButtonHolder() {
        charList = new ArrayList<>();
        btnList = new ArrayList<>();
        formatButtonHolder();
        initButton();
    }

    private void formatButtonHolder() {
        this.setAlignment(Pos.CENTER);
        this.setVgap(10);
        this.setHgap(10);
    }

    private void initButton() {
        int columnIndex = 0;
        int rowIndex = 0;
        for (int i = 0; i < 14; i++) {
            Button button = new Button();
            button.setPrefSize(48, 40);
            button.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 20));

            //Add buttons to GUI
            this.add(button, columnIndex, rowIndex);
            columnIndex++;
            if (columnIndex > 6) {
                columnIndex = 0;
                rowIndex = 1;
            }

            //Add buttons to list
            btnList.add(button);
        }
    }

    public void setupButton(String quiz, TextFieldHolder textFieldHolder) {
        //Prepare char list
        setCharList(quiz);

        int counter = 0;
        for (Button button : btnList) {
            //Set text on button
            int randomCharIndex = (int) (Math.random()*(14-counter));
            button.setText(charList.get(randomCharIndex));
            charList.remove(randomCharIndex);
            counter++;
            //Add listener to button
            button.setOnAction(event -> {
                SoundMaker.click();
                Button btnClicked = (Button) event.getSource();
                //Send button into the text field, so the text field can recall that button later
                textFieldHolder.getFirstEmptyTextField().setTextBasedOnBtnClicked(btnClicked);
            });
        }
    }

    private void setCharList(String quiz) {
        for (int i = 0; i < 14; i++) {
            try {
                charList.add(Character.toString(quiz.charAt(i)));
            } catch(Exception e) {
                int temp = (int) (Math.random()*26 + 65);
                charList.add(Character.toString((char)temp));
            }
        }
    }

    public void rebuild(ArrayList<String> characterList) {
        for (String character : characterList) {
            for (Button button : btnList) {
                if (button.getText().equals(character) && button.isVisible()) {
                    button.setVisible(false);
                    break;
                }
            }
        }
    }

    public void reset() {
        for (Button btn : btnList) {
            btn.setVisible(true);
        }
    }

    public void hideBtnByUsingHint(String character) {
        for (Button btn : btnList) {
            if (btn.getText().equals(character) && btn.isVisible()) {
                btn.setVisible(false);
                break;
            }
        }
    }
}