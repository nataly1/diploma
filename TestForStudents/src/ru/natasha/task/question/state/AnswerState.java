package ru.natasha.task.question.state;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Natasha on 30.03.2015.
 */
public class AnswerState implements Serializable{

    private ArrayList selectedAnswer = null;

    public AnswerState() {
        selectedAnswer = new ArrayList<>();
    }

    public void setAnswer(ArrayList answer) {
        selectedAnswer = answer;
    }

    public ArrayList getSelectedAnswer() {
        return this.selectedAnswer;
    }
}
