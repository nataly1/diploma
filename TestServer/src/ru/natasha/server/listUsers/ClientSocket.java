package ru.natasha.server.listUsers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Natasha on 16.03.2015.
 */
public class ClientSocket {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientSocket(Socket s) {
        this.connection = s;
    }

    public ClientSocket(Socket s, ObjectOutputStream out, ObjectInputStream in) {
        this.connection = s;
        this.output = out;
        this.input = in;
    }

    public Socket getConnection () {return this.connection; }

    public ObjectOutputStream getOutput () {return this.output; }

    public ObjectInputStream getInput () {return this.input; }
}
