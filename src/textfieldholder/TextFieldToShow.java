package textfieldholder;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextFieldToShow extends TextField {
    public TextFieldToShow(String text) {
        super(text);
        this.setPrefColumnCount(1);
        this.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-text-fill: #FF4500");
        this.setEditable(false);
    }
}