package textfieldholder;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import soundmaker.SoundMaker;

public class PTextField extends TextField {
    private static int indexStatic;
    private int index;
    private Button btnWhoseText;

    public PTextField() {
        super();
        index = indexStatic;
        indexStatic++;
        formatTextField();
    }

    private void formatTextField() {
        this.setText("");
        this.setPrefColumnCount(1);
        this.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        this.setAlignment(Pos.CENTER);

        //Limit the input by only 1 character
        this.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (this.getText().length() > 1) {
                    this.setText(this.getText().substring(0,1));
                }
            }
        });

        //Allow to input only letters
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            //if new value change to new character which is not in [a-zA-Z] set, replace it to oldValue
            if (!newValue.matches("a-zA-Z*")) {
                this.setText(newValue.replaceAll("[^a-zA-Z]", oldValue));
            }
        });

        //Uppercase the input using TextFormatter
        this.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        //Clear the text field whenever click on it
        this.setOnMouseReleased(e -> {
            if (!this.getText().equals("") && this.isEditable()) {
                this.clear();
                try {
                    SoundMaker.clickOff();
                    btnWhoseText.setVisible(true);
                    btnWhoseText = null;
                } catch(Exception ignored) {}
            }
        });
    }

    public int getIndex() {
        return index;
    }

    public static void resetIndexToZero() {
        indexStatic = 0;
    }

    public boolean checkYourself(String quiz) {
        return this.getText().equals(Character.toString(
                quiz.charAt(index)));
    }

    public void setTextBasedOnBtnClicked(Button btnClicked) {
        //Execute if only the Text field is empty,
        if (this.getText().equals("")) {
            this.setText(btnClicked.getText());
            //Remember the button which has been clicked
            btnClicked.setVisible(false);
            this.btnWhoseText = btnClicked;
        }
    }
}

