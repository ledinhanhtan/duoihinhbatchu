package playerdata;

import textfieldholder.PTextField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameState implements Serializable {
    private Map<Integer, String> gameStateMap;

    public GameState(ArrayList<PTextField> quizTFList) {
        gameStateMap = new LinkedHashMap<>();

        for (PTextField textField : quizTFList) {
            //Only catch text field which are uneditable which are reveal by using hint
            if(!textField.isEditable()) {
                gameStateMap.put(textField.getIndex(), textField.getText());
            }
        }
    }

    public Map<Integer, String> getGameStateMap() {return gameStateMap;}
    public ArrayList<String> getCharacterList() {return new ArrayList<>(gameStateMap.values());}
}
