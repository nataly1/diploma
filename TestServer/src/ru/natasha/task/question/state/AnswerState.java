//package ru.natasha.task.question.state;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
///**
// * Created by Natasha on 30.03.2015.
// */
//public class AnswerState implements Serializable{
//
//    private ArrayList<Integer> selectedIntegerAnswer = null;
//    private ArrayList<String> selectedStringAnswer = null;
//
//
//    public AnswerState() {
//        selectedIntegerAnswer = new ArrayList<>();
//        selectedStringAnswer = new ArrayList<>();
//    }
//
//    public void setIntegerAnswer(ArrayList<Integer> answer) {
//        selectedIntegerAnswer = answer;
//    }
//
//    public void setStringAnswer(ArrayList<String> answer) {
//        selectedStringAnswer = answer;
//    }
//
//    public ArrayList<Integer> getSelectedIntegerAnswer() {
//        return this.selectedIntegerAnswer;
//    }
//
//    public ArrayList<String> getSelectedStringAnswer() {
//        return this.selectedStringAnswer;
//    }
//
//}

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
