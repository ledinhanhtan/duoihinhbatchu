package playerdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Player {
    private int currentLevel;
    private int ruby;
    private GameState gameState;

    public Player() {
        try {
            FileInputStream fis = new FileInputStream("resource\\ser\\player.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.currentLevel = ois.readInt();
            this.ruby = ois.readInt();
            this.gameState = (GameState) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Game open the very first time");
            currentLevel = 0;
            ruby = 0;
            gameState = null;
        }
    }


    //Getter------------------------------------
    public int getCurrentLevel() {return currentLevel;}

    public int getRuby() {return ruby;}

    public GameState getGameState() {return gameState;}
    //------------------------------------------


    public void releaseMemory() { gameState = null; }

    public void savePlayerData(int currentLevel, int ruby, GameState gameState) {
        try {
            FileOutputStream fs = new FileOutputStream("resource\\ser\\player.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeInt(currentLevel);
            os.writeInt(ruby);
            os.writeObject(gameState);
            os.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
