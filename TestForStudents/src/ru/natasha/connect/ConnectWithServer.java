package ru.natasha.connect;

import ru.natasha.message.Message;
import ru.natasha.message.MessageStudent;
import ru.natasha.window.ResultWindow;
import ru.natasha.window.StartWindow;
import ru.natasha.window.TestWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import static ru.natasha.window.TestWindow.DEMO_TEST;
import static ru.natasha.window.TestWindow.RANDOM_TEST;

/**
 * Created by Natasha on 17.03.2015.
 */
public class ConnectWithServer {
    private static ConnectWithServer connectWithServer;

    public static Socket connection;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;
    public static String address = "127.0.0.1";
    public static int port = 1290;

    public static final String FROM_STUD = "STUD";
    public static final String REG_STUD = "REGST";
    public static final String TEST_STATUS = "GETSTATUS";
    public static final String GET_DEMOTEST = "GETDEMO";
    public static final String SEND_REZULT = "REZULT";

    private Thread threadTCP;
    StartWindow win;
    TestWindow testWindow;

    //обращаясь к этому методу, будем создавать новый объект класса ConnectWithServer, если он ещё на создан
    public static ConnectWithServer getConnectWithServer() {
        if (connectWithServer == null) {
            connectWithServer= new ConnectWithServer();
        }
        return connectWithServer;
    }

    private ConnectWithServer() {
        if (connect()) {
            win = new StartWindow();

            threadTCP = new Thread(new ListenServer());
            threadTCP.setDaemon(true);
            threadTCP.start();
        }
        else
            JOptionPane.showMessageDialog(
                    null, "Подключение к серверу не удалось", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
    }

    private boolean connect() {
        System.out.println("Устанавливаем связь с сервером");
        try {
            connection = new Socket(InetAddress.getByName(address), port);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println(e + "\nПодключение не удалось\n");
            return false;
        }
    }

    private class ListenServer implements Runnable {
        @Override
        public void run() {
            sendData(new MessageStudent(REG_STUD));
            try {
                while (true) {
                    synchronized (input) {
                        MessageStudent message = (MessageStudent) input.readObject();

                        String kod = message.getKod();
                        System.out.println("Принято: " + kod);
                        switch (kod) {
                            case (GET_DEMOTEST):
                                System.out.println("Пришли задания для ДЕМО_ТЕСТА");
                                System.out.println("В listTasks");
                                System.out.println("номер задачи    задача");
                                //посмотрим, что лежит в listTasks
//                            for ( TaskStudent s : message.getListTasks())
//                            {
//                                System.out.print(s.getNumberTask() + "        ");      //номер задачи
//                                System.out.print(s.getTask() + "        ");   //задача
//                                System.out.println(s.getAnswer());   //ответ
//                                System.out.println(s.getVerbalTask());   //ответ
//                            }
                                win.dispose();
                                testWindow = new TestWindow(DEMO_TEST, message.getListTasks());
//                            new TestWindow(DEMO_TEST);
                                break;

                            case (TEST_STATUS):
                                System.out.println(message.getStatus());
                                if (message.getStatus()) {
                                    //достаем список заданий
//                                for ( TaskStudent s : message.getListTasks())
//                                {
//                                    System.out.print(s.getNumberTask() + "        ");      //номер задачи
//                                    System.out.print(s.getTask() + "        ");   //задача
//                                    System.out.println(s.getAnswer());   //ответ
//                                    System.out.println(s.getVerbalTask());   //ответ
//                                }
                                    win.dispose();
                                    testWindow = new TestWindow(RANDOM_TEST,message.getListTasks());
//                                new TestWindow(RANDOM_TEST);
                                } else {
                                    JOptionPane.showMessageDialog(win, "Не доступно", "Отказ в доступе", JOptionPane.ERROR_MESSAGE);
                                }
                                break;

                            case SEND_REZULT:
                                System.out.println("Результаты пришли");
//                            Integer result = 0;
//
//                            for (int i=1; i < (message.getListStudentsScore().size() +1); i++) {
//                                result += message.getListStudentsScore().get(i-1);
//                                System.out.println("Задание " + i + " балл " + message.getListStudentsScore().get(i-1));
//                            }

                                testWindow.dispose();

                                new ResultWindow(message.getListStudentsScore());

//                            JOptionPane.showMessageDialog(null, "Вы набрали " + result + " баллов", "Результат", JOptionPane.INFORMATION_MESSAGE);
                                break;
                        }
                    }
                }
            } catch (SocketException e) {
                JOptionPane.showMessageDialog(null, "Соединение прервано", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);

                if (win!=null)
                    win.dispose();
                if (testWindow!=null)
                    testWindow.dispose();

                System.err.println(e + "\nПодключение не удалось\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Соединение прервано", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace();
            }
        }
    }

    public static void sendData(MessageStudent obj){
        try {
            output.flush();
            output.writeObject(new Message(FROM_STUD, obj));
        } catch (SocketException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
