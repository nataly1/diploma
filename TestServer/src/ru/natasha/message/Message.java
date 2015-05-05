package ru.natasha.message;

import java.io.Serializable;

/**
 * Created by Natasha on 16.03.2015.
 */
public class Message implements Serializable {
    private String from;
    private MessageProfessor messageFromProfessor;
    private MessageStudent messageFromStudent;

    public Message(String from, MessageProfessor message) {
        this.from = from;
        this.messageFromProfessor = message;
    }

    public Message(String from, MessageStudent message) {
        this.from = from;
        this.messageFromStudent = message;
    }

    public String getFrom() {
        return this.from;
    }

    public MessageProfessor getMessageFromProfessor() {
        return this.messageFromProfessor;
    }

    public MessageStudent getMessageFromStudent() {return this.messageFromStudent; }
}