package checker;

import javafx.concurrent.Task;
import textfieldholder.PTextField;

import java.util.ArrayList;

public class CorrectedChecker extends Task<boolean[]> {
    private ArrayList<PTextField> quizTFList;
    private String quiz;

    public CorrectedChecker(ArrayList<PTextField> quizTFList, String quiz) {
        this.quizTFList = quizTFList;
        this.quiz = quiz;
    }

    @Override
    protected boolean[] call() {
        boolean allFilled;
        boolean corrected = false;
        boolean[] result = new boolean[2];

        allFilled = true;
        for (PTextField temp : quizTFList) {
            if (temp.getText().equals("")) {
                allFilled = false;
                break;
            } 
        }

        if (allFilled) {
            corrected = true;
            for (PTextField temp : quizTFList) {
                if (!temp.checkYourself(quiz)) {
                    corrected = false;
                    break;
                }
            }
        }

        result[0] = allFilled;
        result[1] = corrected;
        return result;
    }
}