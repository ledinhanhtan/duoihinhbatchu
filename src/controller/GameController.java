package controller;

import box.ConfirmationBox;
import box.MessageBox;
import box.RedeemBox;
import buttonholder.ButtonHolder;
import checker.CorrectedChecker;
import playerdata.GameState;
import playerdata.Player;
import quiz.Quiz;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundmaker.SoundMaker;
import textfieldholder.PTextField;
import textfieldholder.TextFieldHolder;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private Stage stage;
    private Scene beginScene;
    private Scene gameScene;
    private Scene congratulationScene;

    private Player playerData;
    private int currentLevel;
    private int ruby;
    private int rubyIncrement;
    private GameState gameState;
    private String quiz;
    private StringProperty levelProperty;
    private IntegerProperty rubyProperty;

    @FXML
    Label levelLabel;
    @FXML
    Label rubyLabel;

    @FXML
    private VBox quizVBox;
    @FXML
    private ImageView imageView;
    private TextFieldHolder textFieldHolder;
    @FXML
    private Pane buttonPane;
    private ButtonHolder buttonHolder;

    @FXML
    public void initialize() {
        setupGame();
        setupGUI();
        setupLevel();
        //Rebuild from the first time open
        rebuild();
    }

    void setStageAndScene(Stage stage, Scene beginScene, Scene gameScene) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            event.consume();
            cleanUp();
        });
        this.beginScene = beginScene;
        this.gameScene = gameScene;
    }

    private void setupGame() {
        //Load quiz data
        Quiz.loadQuiz();
        //Load data
        playerData = new Player();
        //Get previous currentLevel
        currentLevel = playerData.getCurrentLevel();
        //Get previous ruby
        ruby = playerData.getRuby();
        //Get game state
        gameState = playerData.getGameState();
    }

    private void setupGUI() {
        //Level label
        levelProperty = new SimpleStringProperty();
        levelLabel.textProperty().bind(levelProperty);
        //Ruby label
        rubyProperty = new SimpleIntegerProperty();
        rubyLabel.textProperty().bind(rubyProperty.asString());
        //textFieldHolder
        textFieldHolder = new TextFieldHolder();
        quizVBox.getChildren().add(textFieldHolder);
        //buttonHolder
        buttonHolder = new ButtonHolder();
        buttonPane.getChildren().add(buttonHolder);
    }

    private void setupLevel() {
        setupQuiz();
        setupTextField();
        setupButton();

        levelProperty.setValue("Level: " + (currentLevel+1));
        rubyProperty.setValue(ruby);
    }

    private void setupQuiz() {
        quiz = Quiz.getQuiz(currentLevel);
        Image image = Quiz.getImage(currentLevel);
        imageView.setImage(image);
    }

    private void setupTextField() {
        for (int i = 0; i < quiz.length(); i ++) {
            PTextField textField = new PTextField();
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.matches("[a-zA-Z]") || oldValue.matches("[a-zA-Z]")) {
                    check();
                }
            });
            textFieldHolder.add(textField);
        }
        textFieldHolder.setupTextField();
        textFieldHolder.focusOnFirstTextField();
    }

    private void check() {
        CorrectedChecker task = new CorrectedChecker(textFieldHolder.getList(), quiz);
        Thread checker = new Thread(task);
        checker.setDaemon(true);
        checker.start();

        task.setOnSucceeded(event -> {
            boolean[] result = task.getValue();
            if (result[0] && !result[1]) {
                textFieldHolder.setToRed();
            } else if (result[1]) {
                increment();
                textFieldHolder.setToGreen();
                //Schedule after 1.2s, the stage will change the scene
                Timer timer = new Timer();
                timer.schedule(new MoveToCongratulationSceneTimer(), 1200);
            }
        });
    }

    private void increment() {
        currentLevel++;
        rubyIncrement = (int) (Math.random()*3 + 4);//Random from 4-6
        ruby += rubyIncrement;
    }

    private boolean alreadySetupCongratulationScene = false;
    private CongratulationController congratulationController;
    public class MoveToCongratulationSceneTimer extends TimerTask {
        @Override
        public void run() {
            SoundMaker.applause();
            Platform.runLater(() -> {
                if (currentLevel < Quiz.getLimit()) {
                    if (!alreadySetupCongratulationScene) {
                        try { setupCongratulationScene(); } catch (IOException e) { e.printStackTrace(); }
                    }
                    congratulationController.setAnswer(currentLevel-1);
                    congratulationController.setRubyIncrement(rubyIncrement);
                    stage.setScene(congratulationScene);
                    //Next level
                    resetLevel();
                    setupLevel();
                } else {
                    setupGameOverScene();
                    currentLevel = 0;
                    resetLevel();
                    setupLevel();
                }
            });
        }
    }

    private void setupCongratulationScene() throws IOException {
        String fxmlPath = "/gui/congratulation.fxml";
        URL location = getClass().getResource(fxmlPath);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        congratulationScene = new Scene(fxmlLoader.load());
        congratulationController = fxmlLoader.getController();
        congratulationController.setStageAndScene(stage, gameScene);
        //Load congratulation.fxml just ONE time only
        alreadySetupCongratulationScene = true;
    }

    private void setupButton() {
        buttonHolder.setupButton(quiz, textFieldHolder);
    }

    private void rebuild() {
        if(!(gameState == null)) {
            textFieldHolder.rebuild(gameState.getGameStateMap());
            buttonHolder.rebuild(gameState.getCharacterList());
            //Release memory
            gameState = null;
            playerData.releaseMemory();
        }
    }

    private void resetLevel() {
        textFieldHolder.reset();
        buttonHolder.reset();
        PTextField.resetIndexToZero();
    }

    public void backBtnClicked() {
        stage.setScene(beginScene);
    }

    public void hintBtnClicked() {
        if (ruby-5 >= 0 && !textFieldHolder.allFilled()) {
            boolean reallyToUseHint;
            reallyToUseHint = ConfirmationBox.show("Dùng gợi ý tốn 5 Ruby", "Xác nhận", "Dùng", "Không");
            if (reallyToUseHint) {
                SoundMaker.dingDing();
                //Decrease ruby by 5
                rubyProperty.setValue(ruby -= 5);
                //Hide the first matched button
                buttonHolder.hideBtnByUsingHint(textFieldHolder.getCharacterOfFirstEmptyTextField(quiz));
                //Then set the Text
                textFieldHolder.revealTextField(quiz);
            }
        } else if (textFieldHolder.allFilled()) {
            MessageBox.show("Các ô chữ đã full", "Thông báo");
        }
        else {
            MessageBox.show("Không đủ Ruby", "Thông báo");
        }
    }

    public void redeemBtnClicked() {
        boolean correctCode = RedeemBox.show();
        if (correctCode) {
            rubyProperty.setValue(ruby += 50);
        }
    }

    private void setupGameOverScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameOver.fxml"));
        Parent gameOverParent = null;
        try {
            gameOverParent = fxmlLoader.load();
        } catch (IOException e) { e.printStackTrace(); }
        assert gameOverParent != null;
        GameOverController gameOverController = fxmlLoader.getController();
        gameOverController.setStageAndScene(stage, beginScene);
        //Change scene
        Scene gameOverScene = new Scene(gameOverParent);
        stage.setScene(gameOverScene);
    }

    private void cleanUp() {
        boolean reallyQuit;
        reallyQuit = ConfirmationBox.show("Bạn có thật sự muốn thoát?", "Xác nhận", "Yes", "No");
        if(reallyQuit) {
            playerData.savePlayerData(currentLevel, ruby, new GameState(textFieldHolder.getList()));
            System.exit(0);
        }
    }
}
