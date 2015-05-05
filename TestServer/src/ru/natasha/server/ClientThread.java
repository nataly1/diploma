package ru.natasha.server;

import ru.natasha.database.ConnectWithDB;
import ru.natasha.server.listUsers.ListUsers;
import ru.natasha.task.TaskStudent;
import ru.natasha.message.Message;
import ru.natasha.message.MessageProfessor;
import ru.natasha.message.MessageStudent;
import ru.natasha.task.Tasks;
import ru.natasha.task.question.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static ru.natasha.database.ConnectWithDB.getListIdTask;

/**
 * Created by Natasha on 15.03.2015.
 */
public class ClientThread implements Runnable {
    Integer id;     //идентификатор соединения
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Message message = null;
    private MessageProfessor fromPrep = null;
    private MessageStudent fromStud = null;

    public static boolean STATUS = true;       //текущий доступ к прохождению теста
    public static final String REG_PREP = "REGPR";  //добавить нового преподавателя
    public static final String REG_STUD = "REGST";  //добавить нового студента
    public static final String FROM_PREP = "PREP";  //сообщение от преподавателя
    public static final String FROM_STUDENT = "STUD";   //сообщение от студента
    public static final String GET_STATUS = "GETSTATUS";    //получить статус доступа
//    public static final String EXCHANGE_STATUS = "EXST";    //изменить статус
    public static final String VERBAL_TASK = "VERB";
    public static final String GET_DEMOTEST = "GETDEMO";    //получить задания для ДЕМО-теста
    public static final String ADD_TASK = "ADD";        //добавить задание в БД
    public static final String SEND_REZULT = "REZULT";  //результат теста
    public static final String NAME_CLASSROOM = "CLASSR";      //получить список аудиторий
    public static final String LIST_PC_IP = "PCIP";         //перечень ПК и из IP адресов для заданной аудитории
    public static final String DELETE_CLASSROOM = "DELETECLR";  //удалить аудиторию
    public static final String RENAME_CLASSROOM = "RENAMECLR";
    public static final String ADD_CLASSROOM = "ADDCLR";
    public static final String DELETE_IP = "DELETEIP";      //удалить IP адресс
    public static final String RENAME_IP = "RENAMEIP";      //изменить название/IP
    public static final String EXCHANGE_STATUS_IP = "EXCIP";        //измнить статус
    public static final String ADD_IP = "ADDIP";        //добавить ПК



    private static ListUsers professorList = new ListUsers();
    private static ListUsers studentList = new ListUsers();

    private Question questionPool = null;

    public ClientThread(Integer id, Socket connection) {
        System.out.println("Новый поток для клиента запускаем");
        this.id = id;
        this.socket = connection;
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                message = (Message) input.readObject(); //читаем пришедший объект
                String from = message.getFrom();    //смотрим от кого

                switch (from) {
                    //если от преподавателя
                    case FROM_PREP:
                        fromPrep = message.getMessageFromProfessor();   //получаем сообщение
                        String kodP = fromPrep.getKod();    //смотрим, какой код прислали
                        switch (kodP) {
                            //регистрируем преподавателя
                            case (REG_PREP):
                                System.out.println("Запущено приложение преподавателя");
                                getProfessorList().addUser(id, socket, output, input);
                                sendData(new MessageProfessor(NAME_CLASSROOM, new ConnectWithDB().getNameClassRoom()));
                                break;

                            //отправляем текущий статус доступа к тесту
                            case (GET_STATUS):
                                System.out.println("Отправили статус");
                                sendData(new MessageProfessor(GET_STATUS, STATUS));
                                break;

                            //изменяем статут доступа для студентов, отправляем информацию об этом изменении всем подключенным приложениям преподавателя
//                            case (EXCHANGE_STATUS):
//                                STATUS = !STATUS;
//                                System.out.println("Статус  " +STATUS);
//                                for ( Integer s : getProfessorList().getListUser())
//                                {
//                                    getProfessorList().getInfoAboutUser(s).getOutput().flush();
//                                    getProfessorList().getInfoAboutUser(s).getOutput().writeObject(
//                                            new MessageProfessor(GET_STATUS, STATUS));
//                                }
//                                break;

                            case LIST_PC_IP:
                                sendData(new MessageProfessor(LIST_PC_IP, new ConnectWithDB().getPC_IPList(fromPrep.getVerbalTask())));

                                System.out.println("Отправили список ПК с IP. Кол-во "+new ConnectWithDB().getPC_IPList(fromPrep.getVerbalTask()).size());
                                break;

                            //удалить аудиторию по имени
                            case DELETE_CLASSROOM:
                                //удаляем из БД
                                new ConnectWithDB().deleteNameClassRoom(fromPrep.getVerbalTask());
                                //отправляем новый список адиторий
                                sendData(new MessageProfessor(NAME_CLASSROOM, new ConnectWithDB().getNameClassRoom()));
                                System.out.println("Удалить " +fromPrep.getVerbalTask());
                                break;

                            case ADD_CLASSROOM:
                                //дабавляем в БД
                                //если true - отсылаем новый список

                                if (new ConnectWithDB().addNameClassRoom(fromPrep.getVerbalTask()))
                                    sendData(new MessageProfessor(NAME_CLASSROOM, new ConnectWithDB().getNameClassRoom()));
                                //иначе - возврашаем false
                                else
                                    sendData(new MessageProfessor(ADD_CLASSROOM, false));
//                                sendData(new MessageProfessor(NAME_CLASSROOM, new ConnectWithDB().addNameClassRoom(fromPrep.getVerbalTask())));

                                System.out.println("Добавить аудиторию " +fromPrep.getVerbalTask());
                                break;


                            //переименовать аудиторию
                            case RENAME_CLASSROOM:
                                //переименовываем в БД
                                //если удалось переименовать - отсылаем список аудиторий
                                if (new ConnectWithDB().renameClassRoom(fromPrep.getListFromDB().get(0)[0], fromPrep.getListFromDB().get(1)[0]))
                                    sendData(new MessageProfessor(NAME_CLASSROOM, new ConnectWithDB().getNameClassRoom()));
                                //иначе - возвращаем false
                                else
                                    sendData(new MessageProfessor(ADD_CLASSROOM, false));

                                System.out.println("Переимновать "+fromPrep.getListFromDB().get(0)[0]+" в " +fromPrep.getListFromDB().get(1)[0]);
                                break;

                            //удалить ПК с указанным IP
                            case DELETE_IP:
                                //удаляем из БД
                                //отправляем новый список адиторий
                                sendData(new MessageProfessor(LIST_PC_IP, new ConnectWithDB().getPC_IPList( new ConnectWithDB().deletePC(fromPrep.getVerbalTask()) )));

                                System.out.println("Удалить ПК с IP " +fromPrep.getVerbalTask());
                                break;

                            //изменить статус для ПК
                            case EXCHANGE_STATUS_IP:
                                sendData(new MessageProfessor(LIST_PC_IP, new ConnectWithDB().getPC_IPList( new ConnectWithDB().exchangeStatus(fromPrep.getVerbalTask()) )));

                                System.out.println("Изменить статус ПК с IP " +fromPrep.getVerbalTask());
                                break;

                            //переименовать ПК или изменить IP
                            case RENAME_IP:
                                String nameClassRoom = new ConnectWithDB().renamePC_IP(fromPrep.getListFromDB().get(0)[0], fromPrep.getListFromDB().get(0)[1], fromPrep.getListFromDB().get(0)[2]);
                                //если не произошло ошибок при изменении
                                if (nameClassRoom != null) {
                                    sendData(new MessageProfessor(RENAME_IP, true));

                                    sendData(new MessageProfessor(LIST_PC_IP, new ConnectWithDB().getPC_IPList( nameClassRoom )));

                                    System.out.println("Изменить статус ПК с IP " +fromPrep.getListFromDB().get(0)[0]);
                                }
                                //иначе
                                else {
                                    sendData(new MessageProfessor(RENAME_IP, false));

                                    System.out.println("НЕ УДАЛОСЬ Изменить статус ПК с IP " +fromPrep.getListFromDB().get(0)[2]);
                                }

                                break;

                            //добавить новый ПК
                            case ADD_IP:
                                //дабавляем в БД
                                //если true - отсылаем новый список
                                if (new ConnectWithDB().addPC(fromPrep.getListFromDB().get(0)[0], fromPrep.getListFromDB().get(0)[1], fromPrep.getListFromDB().get(0)[2])) {
                                    sendData(new MessageProfessor(LIST_PC_IP, new ConnectWithDB().getPC_IPList(fromPrep.getListFromDB().get(0)[0])));
                                    //говорим, что успешно добавлен IP
                                    sendData(new MessageProfessor(ADD_IP, true));
                                }
                                    //иначе - возврашаем false
                                else
                                    sendData(new MessageProfessor(ADD_IP, false));


                                System.out.println("Добавить ПК с IP " +fromPrep.getListFromDB().get(0)[2]);
                                break;


                            //найти в БД вербальное задание для нужной задачи и отправить его клиенту
                            case (VERBAL_TASK):
                                System.out.println("Отпрваить " +VERBAL_TASK);
                                verbalTask(fromPrep.getNumberTask());
                                break;

                            //добавляем в БД задание
                            case (ADD_TASK):
                                addTask(fromPrep.getTasks());
                                break;
                        }
                        break;


                    //если от студента
                    case FROM_STUDENT:
                        fromStud = message.getMessageFromStudent();  //получаем сообщение
                        String kodS = fromStud.getKod();        //смотрим, какой код прислали
                        switch (kodS) {
                            //регистрируем студента
                            case (REG_STUD):
                                System.out.println("Запущено приложение студента");
                                getStudentList().addUser(id, socket, output, input);
                                break;
                            //если статус = faulse, отправляем сообщение о том, что недоступно
                            //иначе, отправляем сгенерированные задания для теста
                            case (GET_STATUS):
                                if (STATUS){
                                    System.out.println("Выдать сгенерированные задания");
                                    ArrayList<TaskStudent> taskStudents = new ConnectWithDB().getRandTestFromDB();
                                    sendData(new MessageStudent(GET_STATUS, STATUS, taskStudents));

                                    for ( TaskStudent s : taskStudents)
                                    {
                                        System.out.print(s.getNumberTask() + "        ");      //номер задачи
                                        System.out.print(s.getTask() + "        ");   //задача
                                        System.out.println(s.getAnswer());   //ответ
                                    }
                                }
                                else {
                                    System.out.println("Отправляем сообщение о том, что недоступно");
                                    sendData(new MessageStudent(GET_STATUS, STATUS));
                                }
                                break;
                            //отправляем задания для ДЕМО-теста
                            case (GET_DEMOTEST):
                                System.out.println("Выдать стандартные 41 задания");
                                ArrayList<TaskStudent> taskStudents = new ConnectWithDB().getDemoTestFromDB();
                                sendData(new MessageStudent(GET_DEMOTEST, STATUS, taskStudents));

                                for ( TaskStudent s : taskStudents)
                                {
                                    System.out.print(s.getNumberTask() + "        ");      //номер задачи
                                    System.out.print(s.getTask() + "        ");   //задача
                                    System.out.println(s.getAnswer());   //ответ
                                }
                                break;

                            case (SEND_REZULT):
                                //получаем список ответов из БД
                                ArrayList<String> answer = new ConnectWithDB().getAnswerFromIdTask(getListIdTask());
                                //список баллов за каждое задание
                                ArrayList<Integer> score = new ConnectWithDB().getScoreFromIdTask(getListIdTask());
                                //список ответов от пользователя
                                questionPool = message.getMessageFromStudent().getQuestionPool();
                                //для подсчета набранных баллов
                                ArrayList<Integer[]> listStudentsScore = new ArrayList<>();
                                Integer[] pairScore;

                                System.out.println("Проверить " +getListIdTask().toString());

                                for (int i=0; i<answer.size(); i++) {
                                    pairScore = new Integer[2];
                                    if (answer.get(i).equals(questionPool.getStringAnswerOnQuestion(i).toString())) {
                                        System.out.println("Верно " + getListIdTask().get(i) + " + " + score.get(i) +" баллов");

                                        //балл за верный ответ
                                        pairScore[0] = pairScore[1] = score.get(i);
                                        listStudentsScore.add(pairScore);
                                    }

                                    else {
                                        System.out.println("Не верно " + getListIdTask().get(i) + " потерял " + score.get(i) +" баллов");

                                        //балл за неверный ответ = 0
                                        pairScore[0] = 0;
                                        pairScore[1] = score.get(i);
                                        listStudentsScore.add(pairScore);
                                    }
                                    System.out.println(answer.get(i) + " " + questionPool.getStringAnswerOnQuestion(i));
                                }

                                sendData(new MessageStudent(SEND_REZULT, listStudentsScore));
                                break;
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Клиент " + id + " закрыл приложение");
            getProfessorList().deleteUser(id);      //удаляем соединение с ID
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendData(Object obj){
        try {
            output.flush();
            output.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTask (Tasks addTask) {


        if (addTask.getNumber() >= 1 & addTask.getNumber() <=14) {
            System.out.print(addTask.getNumber() + "        ");      //номер задачи
            System.out.print(addTask.getListTask() + "        ");
            System.out.println(addTask.getListAnswer() + "        ");
            System.out.println(addTask.getVerbalTask() + "        ");

            if (new ConnectWithDB().addTask_1_14(addTask.getNumber(), addTask.getListTask(), addTask.getListAnswer(), addTask.getVerbalTask()))
                sendData(new MessageProfessor(ADD_TASK, true));
//                System.out.println("Добавить задание в БД");
            else  sendData(new MessageProfessor(ADD_TASK, false));
        }

        if (addTask.getNumber() >= 15 & addTask.getNumber() <=18) {
            System.out.print(addTask.getNumber() + "        ");      //номер задачи
            System.out.print(addTask.getCountElements() + "        ");
            System.out.println(addTask.getListTask() + "        ");
            System.out.println(addTask.getVerbalTask() + "        ");

            if (new ConnectWithDB().addTask_15_18(addTask.getNumber(), addTask.getCountElements(), addTask.getListTask(), addTask.getVerbalTask()))
                sendData(new MessageProfessor(ADD_TASK, true));
//                System.out.println("Добавить задание в БД");
            else  sendData(new MessageProfessor(ADD_TASK, false));
        }

    }

    private void verbalTask(Integer number) {
        String task = new ConnectWithDB().getVerbalTask(number);

//        if (task != null) {
            sendData(new MessageProfessor(VERBAL_TASK, task));
//        }
    }

    public synchronized static ListUsers getProfessorList() {
        return professorList;
    }

    public synchronized static ListUsers getStudentList() {
        return studentList;
    }
}