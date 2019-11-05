package quiz;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Quiz {
    private static File[] files;
    private static ArrayList<String> quizList;

    public static void loadQuiz() {
        File dir = new File("resource\\picture");
        files = dir.listFiles();

        //Version 1: load from picture's name
//        quizList = new ArrayList<>();
//        assert files != null;
//        for (File file : files) {
//            String name = file.getName();
//            //Use DOUBLE escape operators, since it's regex
//            String[] tokens = name.split("\\.");
//            quizList.add(tokens[1]);
//        }
        //-------------------------------------

        //Version 2: load from quizlist.ser
        try {
            FileInputStream fis = new FileInputStream("resource\\ser\\quizlist.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //noinspection unchecked
            quizList = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception ignore) {}
        //---------------------------------
    }

    public static Image getImage(int index) {
        return new Image(files[index].toURI().toString());
    }

    public static String getQuiz(int index) {
        return StringUtils.removeAccent(quizList.get(index).replaceAll(" ",""));
    }

    public static String getAnswer(int index) {
        return quizList.get(index);
    }

    public static int getLimit() { return quizList.size(); }
}