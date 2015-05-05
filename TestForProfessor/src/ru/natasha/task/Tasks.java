package ru.natasha.task;

        import java.io.Serializable;
        import java.util.ArrayList;

/**
 * Created by Natasha on 23.03.2015.
 */
public class Tasks implements Serializable {
    private int number;
    private ArrayList<String> listTask;
    private ArrayList<Integer> listAnswer;     //текст в JTextField
    private String verbalTask;

    private int countElements;


    //для задач 5 - 14
    public Tasks(int n,  ArrayList<String> i, ArrayList<Integer> a, String v){
        this.number = n;
        this.listTask = i;
        this.listAnswer = a;
        this.verbalTask = v;
    }

    //для задачи 15
    public Tasks(int n, int count, ArrayList<String> a, String v){
        this.number = n;
        this.countElements = count;
        this.listTask = a;
        this.verbalTask = v;
    }

    public int getNumber() { return this.number;}
    public ArrayList<String> getListTask() {return this.listTask; }
    public ArrayList<Integer> getListAnswer() {return this.listAnswer;}
    public String getVerbalTask() {return this.verbalTask;}
    public int getCountElements() {return this.countElements;}
}
