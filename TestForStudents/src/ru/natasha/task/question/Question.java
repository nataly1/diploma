package ru.natasha.task.question;

import ru.natasha.task.question.state.AnswerState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Natasha on 30.03.2015.
 */
public class Question implements Serializable {

    private static Question question = null;

    private AnswerState[] answerStates = null;

    private Question(int size) {
        answerStates = new AnswerState[size];
        for(int i = 0; i < size; i++) {
            answerStates[i] = new AnswerState();
        }
    }

    public static Question getQuestionPool() {
        return question;
    }

    public void setAnswerOnQuestion(int numberOfQue, ArrayList<Integer> answers) {
        answerStates[numberOfQue].setAnswer(answers);
    }

//    public void setAnswerOnQuestion(int numberOfQue, ArrayList<Integer> answers) {
//        answerStates[numberOfQue].setAnswer(answers);
//    }

    public ArrayList<Integer> getIntegerAnswerOnQuestion(int numberOfQue) {
        ArrayList<Integer> listInt = new ArrayList<>();
        for (Object el : this.answerStates[numberOfQue].getSelectedAnswer()) {
            listInt.add(Integer.parseInt(el.toString()));
        }

        return listInt;
    }

    public ArrayList<String> getStringAnswerOnQuestion(int numberOfQue) {
        ArrayList<String> listInt = new ArrayList<>();
        for (Object el : this.answerStates[numberOfQue].getSelectedAnswer()) {
            listInt.add(el.toString());
        }

        return listInt;
    }

    public Integer getSize() {
        return answerStates.length;
    }

    public static Question initQuestion(int size){
        question = new Question(size);
        return question;
    }
}
