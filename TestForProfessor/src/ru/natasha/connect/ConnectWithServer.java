package ru.natasha.connect;

import ru.natasha.message.Message;
import ru.natasha.message.MessageProfessor;
import ru.natasha.window.AddIPDialog;
import ru.natasha.window.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Natasha on 15.03.2015.
 */
public class ConnectWithServer {
    public static Socket connection;   //посылает TCP сообщения
    public static ObjectOutputStream output;
    public static ObjectInputStream input;
    public static String address = "127.0.0.1";
    public static int port = 1290;

    MainWindow win;
    private Thread threadTCP;
    private String takeTestT = "Текущее состояние РАЗРЕШИТЬ";
    private String takeTestF = "Текущее состояние ЗАПРЕТИТЬ";
    public static final String REG_PREP = "REGPR";
    public static final String FROM_PREP = "PREP";
//    public static final String TEST_STATUS = "GETSTATUS";
    public static final String EXCHANGE_STATUS = "EXST";
    public static final String VERBAL_TASK = "VERB";
    public static final String ADD_TASK = "ADD";
    public static final String NAME_CLASSROOM = "CLASSR";      //получить список аудиторий
    public static final String LIST_PC_IP = "PCIP";         //перечень ПК и из IP адресов для заданной аудитории

    public static final String DELETE_CLASSROOM = "DELETECLR";  //удалить аудиторию
    public static final String RENAME_CLASSROOM = "RENAMECLR";     //переименовать аудиторию
    public static final String ADD_CLASSROOM = "ADDCLR";        //добавить новую аудиторию

    public static final String DELETE_IP = "DELETEIP";      //удалить IP адресс
    public static final String RENAME_IP = "RENAMEIP";      //изменить название/IP
    public static final String EXCHANGE_STATUS_IP = "EXCIP";        //измнить статус
    public static final String ADD_IP = "ADDIP";        //добавить ПК


    //если осоединение с сервером установлено, открываем окно и слушаем сервер
    public ConnectWithServer() {
        if (connect()) {
            win = new MainWindow();

            threadTCP = new Thread(new ListenServer());
            threadTCP.setDaemon(true);
            threadTCP.start();
        }
        else
            JOptionPane.showMessageDialog(null, "Подключение к серверу не удалось", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
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
            sendData(new MessageProfessor(REG_PREP));
//            sendData(new MessageProfessor(TEST_STATUS));
            try {
                while (true) {
                    synchronized (ConnectWithServer.input) {
                        MessageProfessor message = (MessageProfessor) ConnectWithServer.input.readObject();

                        String kod = message.getKod();
                        System.out.println("Принято: " + kod);
                        switch (kod) {
//                            case (TEST_STATUS):
//                                System.out.println(message.getStatus());
//                                if (message.getStatus()) {
//                                    win.setExchangeStatus(takeTestT);
//                                } else {
//                                    win.setExchangeStatus(takeTestF);
//                                }
//                                break;

                            case NAME_CLASSROOM:
                                win.addClassRoomForTable(message.getListFromDB());
                                break;

                            case LIST_PC_IP:
                                win.addPC_IP(message.getListFromDB());
                                System.out.println("Всего ПК "+message.getListFromDB().size());
                                break;

                            case ADD_CLASSROOM:
                                //если не удалось добавить аудиторию в БД
                                //напрмер, есть уже такая запись
                                JOptionPane.showMessageDialog(null, "Аудитория с таким именем уже есть", "Ошибка", JOptionPane.ERROR_MESSAGE);

                                System.out.println("Добавить Аудиторию Статус "+message.getStatus());
                                break;


                            case RENAME_IP:
                                //если добавили в БД ПК
                                if (message.getStatus()) {

                                    JOptionPane.showMessageDialog(null, "Данные успешно изменены", "Редактирование ПК", JOptionPane.INFORMATION_MESSAGE);

                                    if (!AddIPDialog.dialog.equals(null)) {
                                        AddIPDialog.dialog.disposeWindow();
                                    }
                                }
                                //если не удалось добавить ПК в БД
                                //например, есть уже ПК с таким IP
                                else
                                    JOptionPane.showMessageDialog(AddIPDialog.dialog, "ПК с таким IP уже есть", "Редактирование ПК", JOptionPane.ERROR_MESSAGE);

                                System.out.println("Добавить ПК Статус "+message.getStatus());
                                break;


                            case ADD_IP:
                                //если добавили в БД ПК
                                if (message.getStatus()) {
                                    JOptionPane.showMessageDialog(null, "ПК успешно добавлен", "Добавление ПК", JOptionPane.INFORMATION_MESSAGE);

                                    if (!AddIPDialog.dialog.equals(null)) {
                                        AddIPDialog.dialog.disposeWindow();
                                    }
                                }
                                //если не удалось добавить ПК в БД
                                //например, есть уже ПК с таким IP
                                else
                                    JOptionPane.showMessageDialog(null, "ПК с таким IP уже есть", "Добавление ПК", JOptionPane.ERROR_MESSAGE);

                                System.out.println("Добавить ПК Статус "+message.getStatus());
                                break;

                            case (VERBAL_TASK):
                            System.out.println(message.getVerbalTask());
                            win.openNewAddTask(message.getVerbalTask());
                            break;

                            case (ADD_TASK):
                                if (message.getStatus()) {
                                    JOptionPane.showMessageDialog(null, "Задание добавлено в БД", "Добавление задания", JOptionPane.INFORMATION_MESSAGE);

                                } else {
                                    JOptionPane.showMessageDialog(null, "Задание НЕ добавлено в БД", "Добавление задания", JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                        }
                    }
                }
            } catch (SocketException e) {
                JOptionPane.showMessageDialog(null, "Соединение прервано", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);

                //закрываем главное окно, если оно открыто
                if (!win.equals(null)) {

                    //закрываем окно с добавлением/переименованием ПК
                    if (AddIPDialog.dialog != null) {
                        AddIPDialog.dialog.disposeWindow();
                    }

                    for (JFrame w : MainWindow.myWindow) {
                        w.dispose();
                    }

                    win.dispose();
                }

                System.err.println(e + "\nПодключение не удалось\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendData(MessageProfessor obj){
        try {
            output.flush();
            output.writeObject(new Message(FROM_PREP,obj));
        } catch (SocketException e) {
//            JOptionPane.showMessageDialog(null, "Подключение не удалось", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
//            System.err.println(e + "\nПодключение не удалось\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Подключение не удалось", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
}
