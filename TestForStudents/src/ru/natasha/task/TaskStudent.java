package ru.natasha.task;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Natasha on 20.03.2015.
 */
public class TaskStudent implements Serializable {
    private Integer numberTask;
    private String verbalTask;
    private String task;
    private String answer;
    private ArrayList<String> taskList;

    //для случайно сгенерированного теста
    public TaskStudent(Integer num, String v, String t){
        this.numberTask = num;
        this.verbalTask = v;
        this.task = t;
    }

    public TaskStudent(Integer num, String v, ArrayList<String> t){
        this.numberTask = num;
        this.verbalTask = v;
        this.taskList = t;
    }
    //для демо-теста
    public TaskStudent(Integer num, String v, String t, String an){
        this.numberTask = num;
        this.verbalTask = v;
        this.task = t;
        this.answer = an;
    }


    public Integer getNumberTask() {
        return this.numberTask;
    }
    public String getVerbalTask() {return this.verbalTask; }
    public String getTask() {
        return this.task;
    }
    public String getAnswer() {
        return this.answer;
    }
    public ArrayList<String> getTaskList() {return this.taskList;}
}
