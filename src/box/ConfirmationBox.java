package box;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox {
    private static Stage stage;
    private static boolean btnYesClicked;

    public static boolean show(String message, String title, String textYes, String textNo) {
        btnYesClicked = false;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label lbl = new Label();
        lbl.setText(message);
        lbl.setFont(new Font(20));

        Button btnYes = new Button();
        btnYes.setText(textYes);
        btnYes.setFont(new Font(15));
        btnYes.setOnAction(e -> btnYes_Clicked());

        Button btnNo = new Button();
        btnNo.setText(textNo);
        btnNo.setFont(new Font(15));
        btnNo.setOnAction(e -> btnNo_Clicked());

        HBox paneBtn = new HBox(20);
        paneBtn.getChildren().addAll(btnYes, btnNo);
        paneBtn.setAlignment(Pos.CENTER);

        VBox pane = new VBox(20);     
        pane.getChildren().addAll(lbl, paneBtn);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();

        return btnYesClicked;
    }

    private static void btnYes_Clicked() {
        stage.close();
        btnYesClicked = true;
    }

    private static void btnNo_Clicked() {
        stage.close();
        btnYesClicked = false;
    }
}