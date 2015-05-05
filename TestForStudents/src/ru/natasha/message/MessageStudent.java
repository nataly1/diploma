package ru.natasha.message;

import ru.natasha.task.TaskStudent;
import ru.natasha.task.question.Question;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Natasha on 16.03.2015.
 */
public class MessageStudent implements Serializable {
    private String kod;
    private boolean status;
    private ArrayList<TaskStudent> listTasks;
    private Question questionPool;
    private ArrayList<Integer[]> listStudentsScore;

    public MessageStudent(String kod) {
        this.kod = kod;
    }

    public MessageStudent(String kod, boolean st) {
        this.kod = kod;
        this.status = st;
    }

    public MessageStudent(String kod, boolean st, ArrayList<TaskStudent> l) {
        this.kod = kod;
        this.status = st;
        this.listTasks = l;
    }

    //от студента массов с ответами
    public MessageStudent(String kod, Question question) {
        this.kod = kod;
        this.questionPool = question;
    }

    public MessageStudent(String kod, ArrayList<Integer[]> listStudentsScore) {
        this.kod = kod;
        this.listStudentsScore = listStudentsScore;
    }

    public String getKod() {
        return this.kod;
    }
    public boolean getStatus() {return this.status; }
    public ArrayList<TaskStudent> getListTasks() {return this.listTasks; }
    public Question getQuestionPool() {return this.questionPool; }
    public ArrayList<Integer[]> getListStudentsScore() {return this.listStudentsScore; }
}