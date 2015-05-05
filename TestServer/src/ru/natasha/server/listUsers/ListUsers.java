package ru.natasha.server.listUsers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Natasha on 16.03.2015.
 */
public class ListUsers {
    private HashMap<Integer, ClientSocket> onlineUsers = new HashMap <Integer, ClientSocket> ();

    public void addUser(Integer id, Socket conn, ObjectOutputStream out, ObjectInputStream in) {
        this.onlineUsers.put(id, new ClientSocket(conn, out, in));
    }

    public void deleteUser(Integer id) {
        this.onlineUsers.remove(id);
    }   //удалить соединение по ID

    public Integer[] getListUser() {
        return this.onlineUsers.keySet().toArray(new Integer[0]);
    }   //получить массив ID соединений

    public ClientSocket getInfoAboutUser(Integer id){
        return this.onlineUsers.get(id);
    }   //получить объект ClientSocket пользователя по ID
}
