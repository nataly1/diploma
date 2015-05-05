package ru.natasha.message;

import ru.natasha.task.Tasks;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Natasha on 16.03.2015.
 */
public class MessageProfessor implements Serializable {
    private String kod;
    private boolean status;
    private Tasks tasks;
    private Integer numberTask;
    private String verbalTask;
    private ArrayList<String[]> listFromDB;

    public MessageProfessor(String kod) {
        this.kod = kod;
    }

    public MessageProfessor(String kod, boolean st) {
        this.kod = kod;
        this.status = st;
    }

    public MessageProfessor (String kod, Tasks t) {
        this.kod = kod;
        this.tasks = t;
    }

    //для получения вербального задания с сервера для указанного номера
    public MessageProfessor(String kod, Integer numberTask) {
        this.kod = kod;
        this.numberTask = numberTask;
    }

    //для получения вербального задания с сервера для указанного номера
    public MessageProfessor(String kod, String verbalTask) {
        this.kod = kod;
        this.verbalTask = verbalTask;
    }

    //перечень аудиторий    //перечень ПК и их IP
    public MessageProfessor(String kod, ArrayList<String[]> listFromDB){
        this.kod = kod;
        this.listFromDB = listFromDB;
    }

    public String getKod() {
        return this.kod;
    }
    public boolean getStatus() {return this.status; }
    public Tasks getTasks() {return this.tasks; }
    public Integer getNumberTask() {return this.numberTask;}
    public String getVerbalTask() {return this.verbalTask;}
    public ArrayList<String[]> getListFromDB() { return this.listFromDB;}
}
