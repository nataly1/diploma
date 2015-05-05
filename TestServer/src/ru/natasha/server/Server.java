package ru.natasha.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Natasha on 15.03.2015.
 */
public class Server implements Runnable {
    Integer id = 0;     //идентификатор  соединения
    @Override
    public void run() {
        System.out.println("Запустили сервер");
        try {
            ServerSocket server = new ServerSocket(1290); //передаем порт  Создаем слушатель
            while (true) {
                Socket connection = null;   //посылает TCP сообщения
                while (connection == null) {
                    connection = server.accept();   //возвращает сокет
                }
                //Создаем новый поток, которому передаем сокет
                new Thread (new ClientThread(id++, connection)).start();
            }
        } catch (UnknownHostException e) {
            System.out.println("Ошибка UnknownHostException     " + e);
        } catch (IOException e) {
            System.out.println("Ошибка IOException     " + e);
        }
        System.out.println("Сервер отключен");
    }
}
