package ru.natasha.database;

import ru.natasha.task.TaskStudent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Natasha on 20.03.2015.
 */
public class ConnectWithDB {
    Connection conn = null;     //соединение с бд
    ResultSet rezSet = null;    //для результата SQL запроса
    PreparedStatement rezultPrepared = null;

    //данные для соединения с БД
    String userName = "root";
    String password = "1111";
    String url = "jdbc:mysql://localhost/probatest";

    private static ArrayList<Integer> listIdTask = new ArrayList<Integer>();;      //список id заданий, кот отправим пользователю
    private ArrayList<TaskStudent> tasks = new ArrayList<TaskStudent>();          //хранится список заданий теста, кот отправим пользователю

    public ConnectWithDB() {
        try { DriverManager.registerDriver(new com.mysql.jdbc.Driver());  }
        catch (SQLException ex) {
            System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
    }

    public ArrayList<TaskStudent> getRandTestFromDB() {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

//            Statement statement = conn.createStatement();
            ArrayList<Integer> listID = new ArrayList<Integer>();

            if (listIdTask != null) { listIdTask.clear(); }
            if (tasks != null) { tasks.clear(); }

            for (int i=1; i<11; i++) {
                int randNumder = 0;
                int massL = 0;      //количество разных заданий для вопроса
                int randID;

                //задания распределены по блокам.
                //из блока выбирается какой-то один тип задания
                if (i == 1)
                    randNumder = generateNumberTask(1, 4);
                if (i == 2)
                    randNumder = generateNumberTask(5, 14);
                if (i == 3)
                    randNumder = generateNumberTask(15, 17);
//                if (i == 4)
//                    randNumder = generateNumberTask(25, 27);
//                if (i == 5)
//                    randNumder = generateNumberTask(28, 31);
//                if (i == 6)
//                    randNumder = generateNumberTask(32, 33);
//                if (i == 7)
//                    randNumder = 34;
//                if (i == 8)
//                    randNumder = generateNumberTask(35, 37);
//                if (i == 9)
//                    randNumder = generateNumberTask(38, 40);
//                if (i == 10)
//                    randNumder = 41;

                //считаем кол-во строк с заданным номером задачи
                rezultPrepared = conn.prepareStatement("SELECT COUNT(*) FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE list_task.numbertask = ?");
                rezultPrepared.setInt(1, randNumder);
                rezSet = rezultPrepared.executeQuery();

                //подсчитываем, сколько заданий выбранного типа есть в БД
                while (rezSet.next()) {
                    massL = Integer.parseInt(rezSet.getString("COUNT(*)"));
                }
//                System.out.println("Вопрос в билете " + i);
//                System.out.println("Выбранный тип "+ randNumder +" в нем заданий " + massL);

                //выбираем все строки с заданиями выбранного типа
                //ID этих заданий записываем в массив
                if (massL>0) {
//                    rezultPrepared = conn.prepareStatement("SELECT * FROM task WHERE numbertask=?");
                    rezultPrepared = conn.prepareStatement("SELECT * FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE list_task.numbertask = ?");

                    rezultPrepared.setInt(1, randNumder);
                    rezSet = rezultPrepared.executeQuery();

                    while (rezSet.next())
                        listID.add(Integer.parseInt(rezSet.getString("list_task.id_t")));

                    //выбираем случайно элемент массива
                    //это задание и будет отправлено пользователю
                    randID = generateNumberTask(1, massL);

//                    rezultPrepared = conn.prepareStatement("SELECT * FROM task WHERE id_t=?");
                    rezultPrepared = conn.prepareStatement("SELECT * FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE list_task.id_t = ?");

                    rezultPrepared.setInt (1, listID.get(randID-1));
                    rezSet = rezultPrepared.executeQuery();

                    while (rezSet.next()) {
//                        System.out.println(rezSet.getString("list_task.verbal_task"));
//                        if (rezSet.getString("list_task.verbal_task") == null) {
//                            tasks.add(new TaskStudent(Integer.parseInt(rezSet.getString("list_task.numbertask")), rezSet.getString("verbal_task.task"), rezSet.getString("list_task.task")));
//                        }
//                        else {
                        String verbalTask = rezSet.getString("list_task.verbal_task");
//                            verbalTask = verbalTask.substring(0, verbalTask.lastIndexOf("ARRAY"))+rezSet.getString("list_task.verbal_task")+
//                                    verbalTask.substring(verbalTask.lastIndexOf("ARRAY")+5);

                        tasks.add(new TaskStudent(Integer.parseInt(rezSet.getString("list_task.numbertask")), verbalTask, rezSet.getString("list_task.task")));
//                        }
                        listIdTask.add(Integer.parseInt(rezSet.getString("list_task.id_t")));     //добавляем id задачи в список

                    }
                }

                listID.clear();
            }
            rezultPrepared.close();
            rezSet.close();
            return tasks;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    private int generateNumberTask(int min, int max) {
        // Инициализируем генератор
        Random rnd = new Random(System.nanoTime());
        // Получаем случайное число в диапазоне от min до max (включительно)
        int number = min + rnd.nextInt(max - min + 1);
        return number;
    }

    public ArrayList<TaskStudent> getDemoTestFromDB(){
        Statement statement;
        try {
            conn = DriverManager.getConnection(url, userName, password);    //устанавливаем соединение с БД
            statement = conn.createStatement();

            if (listIdTask != null) { listIdTask.clear(); }
            if (tasks != null) { tasks.clear(); }

            for (int i=1; i<18; i++) {
                //берем 1-ую строчку с заданием для каждой задачи
                rezSet = statement.executeQuery("SELECT * FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE list_task.numbertask=" + i + " LIMIT 1");

                while (rezSet.next()) {
                    //добавляем в список заданий
//                    if (rezSet.getString("list_task.verbal_task") == null) {
//                        tasks.add(new TaskStudent(Integer.parseInt(rezSet.getString("list_task.numbertask")), rezSet.getString("verbal_task.task"), rezSet.getString("list_task.task"), rezSet.getString("list_task.answer")));
//                    }
//                    else {
                    String verbalTask = rezSet.getString("list_task.verbal_task");
//                        verbalTask = verbalTask.substring(0, verbalTask.lastIndexOf("ARRAY"))+rezSet.getString("list_task.verbal_task")+
//                                verbalTask.substring(verbalTask.lastIndexOf("ARRAY")+5);

                    tasks.add(new TaskStudent(Integer.parseInt(rezSet.getString("list_task.numbertask")), verbalTask, rezSet.getString("list_task.task"), rezSet.getString("list_task.answer")));
//                    }

                    listIdTask.add(Integer.parseInt(rezSet.getString("list_task.id_t")));     //добавляем id задачи в список
                }
            }
            rezSet.close();
            return tasks;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

//    public boolean addTask_1_4(int number, ArrayList<Integer> answer, ArrayList<String> task){
//        try {
//            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД
//            //проверить, есть ли в БД уже такое задание
//            rezultPrepared = conn.prepareStatement("SELECT * FROM task WHERE numbertask=? AND task=?");
//            rezultPrepared.setInt(1, number);
//            rezultPrepared.setString(2, task.toString());
//            rezSet = rezultPrepared.executeQuery();
//            //если в БД уже есть такая запись
//            if (rezSet.next()) {
//                return false;
//            }
//            //если в БД нет такой записи
//            else {
//                rezultPrepared = conn.prepareStatement("INSERT INTO task (numbertask, task, answer) VALUES (?, ?, ?)");
//                rezultPrepared.setInt(1, number);
//                rezultPrepared.setString(2, task.toString());
//                rezultPrepared.setString(3, answer.toString());
//                rezultPrepared.executeUpdate();
//            }
//            rezultPrepared.close();
//            rezultPrepared = null;
//            return true;
//        }
//        catch (SQLException ex)
//        {    System.out.println("SQLException caught");
//            while ( ex != null ){
//                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
//                ex = ex.getNextException();
//            }
//        }
//        finally
//        {
//            if (conn != null)
//            {
//                try
//                {
//                    conn.close ();
//                }
//                catch (Exception e) { }
//            }
//        }
//        return false;
//    }

//    public boolean addTask_1_4(int number, ArrayList<String> task, ArrayList<Integer> answer){
//        try {
//            Integer idTask;
//            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД
//
//            //проверить, есть ли в БД уже такое задание
//            rezultPrepared = conn.prepareStatement("SELECT list_task.numbertask, list_task.task, list_task.answer FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE verbal_task.numbertask=? AND list_task.task=?");
//            rezultPrepared.setInt(1, number);
//            rezultPrepared.setString(2, task.toString());
//            rezSet = rezultPrepared.executeQuery();
//            //если в БД уже есть такая запись
//            if (rezSet.next()) {
//                return false;
//            }
//            //если в БД нет такой записи
//            else {
//                //берем ID заданной задачи
//                rezultPrepared = conn.prepareStatement("SELECT id AS ID FROM verbal_task WHERE numbertask=?");
//                rezultPrepared.setInt(1, number);
//                rezSet = rezultPrepared.executeQuery();
//                if (rezSet.next()) {
//                    idTask = Integer.parseInt(rezSet.getString("ID"));
//
//                    rezultPrepared = conn.prepareStatement("INSERT INTO list_task (numbertask, task, answer) VALUES (?, ?, ?)");
//                    rezultPrepared.setInt(1, idTask);
//                    rezultPrepared.setString(2, task.toString());
//                    rezultPrepared.setString(3, answer.toString());
//                    rezultPrepared.executeUpdate();
//                }
//            }
//            rezultPrepared.close();
//            rezultPrepared = null;
//            return true;
//        }
//        catch (SQLException ex)
//        {    System.out.println("SQLException caught");
//            while ( ex != null ){
//                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
//                ex = ex.getNextException();
//            }
//        }
//        finally
//        {
//            if (conn != null)
//            {
//                try
//                {
//                    conn.close ();
//                }
//                catch (Exception e) { }
//            }
//        }
//        return false;
//    }

    public boolean addTask_1_14(int number, ArrayList<String> task, ArrayList<Integer> answer, String verbal){
        try {
            Integer idTask;
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            //проверить, есть ли в БД уже такое задание
            rezultPrepared = conn.prepareStatement("SELECT list_task.numbertask, list_task.task, list_task.answer FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE verbal_task.numbertask=? AND list_task.task=?");
            rezultPrepared.setInt(1, number);
            rezultPrepared.setString(2, task.toString());
            rezSet = rezultPrepared.executeQuery();
            //если в БД уже есть такая запись
            if (rezSet.next()) {
                return false;
            }
            //если в БД нет такой записи
            else {
                //берем ID заданной задачи
                rezultPrepared = conn.prepareStatement("SELECT id AS ID FROM verbal_task WHERE numbertask=?");
                rezultPrepared.setInt(1, number);
                rezSet = rezultPrepared.executeQuery();
                if (rezSet.next()) {
                    idTask = Integer.parseInt(rezSet.getString("ID"));

                    rezultPrepared = conn.prepareStatement("INSERT INTO list_task (numbertask, task, answer, verbal_task) VALUES (?, ?, ?, ?)");
                    rezultPrepared.setInt(1, idTask);
                    rezultPrepared.setString(2, task.toString());
                    rezultPrepared.setString(3, answer.toString());
                    rezultPrepared.setString(4, verbal.trim());
                    rezultPrepared.executeUpdate();
                }
            }
            rezultPrepared.close();
            rezSet.close();
            return true;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return false;
    }

    public boolean addTask_15_18(int number, int count, ArrayList<String> answer, String verbal){
        try {
            Integer idTask;
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            //проверить, есть ли в БД уже такое задание
            rezultPrepared = conn.prepareStatement("SELECT * FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE verbal_task.numbertask=? AND list_task.answer=?");
            rezultPrepared.setInt(1, number);
            rezultPrepared.setString(2, answer.toString());
            rezSet = rezultPrepared.executeQuery();
            //если в БД уже есть такая запись
            if (rezSet.next()) {
                System.out.print(rezSet.getString("list_task.answer"));      //номер задачи
                return false;
            }
            //если в БД нет такой записи
            else {
                //берем ID заданной задачи
                rezultPrepared = conn.prepareStatement("SELECT id AS ID FROM verbal_task WHERE numbertask=?");
                rezultPrepared.setInt(1, number);
                rezSet = rezultPrepared.executeQuery();
                if (rezSet.next()) {
                    idTask = Integer.parseInt(rezSet.getString("ID"));

                    rezultPrepared = conn.prepareStatement("INSERT INTO list_task (numbertask, task, answer, verbal_task) VALUES (?, ?, ?, ?)");
                    rezultPrepared.setInt(1, idTask);
                    rezultPrepared.setInt(2, count);
                    rezultPrepared.setString(3, answer.toString());
                    rezultPrepared.setString(4, verbal.trim());
                    rezultPrepared.executeUpdate();
                }
            }
            rezultPrepared.close();
            rezSet.close();
            return true;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return false;
    }

    public String getVerbalTask(Integer number) {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД
            //есть ли в БД текстовое задание для данного номера задачи
            rezultPrepared = conn.prepareStatement("SELECT task FROM verbal_task WHERE numbertask=?");
            rezultPrepared.setInt(1, number);
            rezSet = rezultPrepared.executeQuery();
            //если есть
            if (rezSet.next()) {
                return rezSet.getString("task");
            }
            //если нет
            else {
                return null;
            }
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }


    //возвращает список баллов за каждое предложенное студенту задание
    public ArrayList<Integer> getScoreFromIdTask(ArrayList<Integer> listId) {
        ArrayList<Integer> listScore = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            for (int i=0; i<listId.size(); i++) {
                //для заданного ID смотрим количество баллов
                rezultPrepared = conn.prepareStatement("SELECT verbal_task.score FROM list_task INNER JOIN verbal_task ON verbal_task.id = list_task.numbertask WHERE list_task.id_t=?");
                rezultPrepared.setInt(1, listId.get(i));
                rezSet = rezultPrepared.executeQuery();
                //если есть
                if (rezSet.next()) {
                    listScore.add(Integer.parseInt(rezSet.getString("verbal_task.score")));
                }
            }
            rezultPrepared.close();
            rezSet.close();

            return listScore;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    //возвращает список аудиторий из БД
    public ArrayList<String[]> getNameClassRoom() {
        ArrayList<String[]> listName = new ArrayList<>();
        String[] name;

        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            rezultPrepared = conn.prepareStatement("SELECT * FROM classroom ORDER BY name");
            rezSet = rezultPrepared.executeQuery();
            //если есть
            while (rezSet.next()) {
                name = new String[1];
                name[0] = rezSet.getString("name");

                listName.add(name);

                System.out.println("Аудитория "+rezSet.getString("name"));
            }
            rezultPrepared.close();
            rezSet.close();
            return listName;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    //удалить из БД Аудиторию по её имени
    public void deleteNameClassRoom(String name) {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            rezultPrepared = conn.prepareStatement("DELETE FROM classroom WHERE name=?");
            rezultPrepared.setString(1, name.trim());
            rezultPrepared.execute();

            rezultPrepared.close();
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
    }


    //Добавить Аудиторию в БД
    public Boolean addNameClassRoom(String name) {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            rezultPrepared = conn.prepareStatement("INSERT INTO classroom (name) VALUES(?)");
            rezultPrepared.setString(1, name.trim());
            rezultPrepared.execute();

            rezultPrepared.close();

            return true;

        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return false;
    }


    //Переименовать Аудиторию
    public Boolean renameClassRoom(String in, String out) {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            rezultPrepared = conn.prepareStatement("UPDATE classroom SET name=? WHERE name=?");
            rezultPrepared.setString(1, out.trim());
            rezultPrepared.setString(2, in.trim());
            rezultPrepared.execute();

            rezultPrepared.close();

            return true;

        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return false;
    }


    //удалить из БД ПК по его IP
    //возврашает имя аудитории, к которой принадлежит IP
    public String deletePC(String ip) {
        String nameClass = "";
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            //из какой аудитории этот ПК
            rezultPrepared = conn.prepareStatement("SELECT * FROM list_pc_ip INNER JOIN classroom ON list_pc_ip.id_classroom = classroom.id WHERE ip=?");
            rezultPrepared.setString(1, ip.trim());
            rezSet = rezultPrepared.executeQuery();
            //если есть
            while (rezSet.next()) {
                nameClass = rezSet.getString("classroom.name");
                System.out.println(nameClass);
            }

            rezultPrepared = conn.prepareStatement("DELETE FROM list_pc_ip WHERE ip=?");
            rezultPrepared.setString(1, ip.trim());
            rezultPrepared.execute();

            rezultPrepared.close();

            return nameClass;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    //изменить статус ПК по его IP
    //возврашает имя аудитории, к которой принадлежит IP
    public String exchangeStatus (String ip) {
        String nameClass = "";
        int status = 0;
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            //из какой аудитории этот ПК
            rezultPrepared = conn.prepareStatement("SELECT * FROM list_pc_ip INNER JOIN classroom ON list_pc_ip.id_classroom = classroom.id WHERE ip=?");
            rezultPrepared.setString(1, ip.trim());
            rezSet = rezultPrepared.executeQuery();
            //если есть
            if (rezSet.next()) {
                nameClass = rezSet.getString("classroom.name");
                System.out.println("Изменить статус для ПК из аудитории "+ nameClass);

                status = rezSet.getInt("status");
            }

            rezultPrepared = conn.prepareStatement("UPDATE list_pc_ip SET status=? WHERE ip=?");
            //меняем статус в БД
            if (status == 0)
                rezultPrepared.setInt(1, 1);
            if (status == 1)
                rezultPrepared.setInt(1, 0);

            rezultPrepared.setString(2, ip.trim());
            rezultPrepared.execute();
            rezultPrepared.close();

            return nameClass;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    //изменить статус ПК по его IP
    //возврашает имя аудитории, к которой принадлежит IP
    public String renamePC_IP (String oldIp, String newName, String newIP) {
        String nameClass = "";
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            //из какой аудитории этот ПК
            rezultPrepared = conn.prepareStatement("SELECT * FROM list_pc_ip INNER JOIN classroom ON list_pc_ip.id_classroom = classroom.id WHERE ip=?");
            rezultPrepared.setString(1, oldIp.trim());
            rezSet = rezultPrepared.executeQuery();
            //если есть
            if (rezSet.next()) {
                nameClass = rezSet.getString("classroom.name");
                System.out.println("Переименовать ПК из аудитории "+ nameClass);
            }

            rezultPrepared = conn.prepareStatement("UPDATE list_pc_ip SET name=?, ip=? WHERE ip=?");
            rezultPrepared.setString(1, newName);
            rezultPrepared.setString(2, newIP);
            rezultPrepared.setString(3, oldIp);
            rezultPrepared.execute();
            rezultPrepared.close();

            return nameClass;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }


    //Добавить ПК в БД
    public Boolean addPC (String nameClass, String namePC, String IP) {
        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД
            int id = 0;

            //из какой аудитории этот ПК
            rezultPrepared = conn.prepareStatement("SELECT * FROM classroom WHERE name=?");
            rezultPrepared.setString(1, nameClass);
            rezSet = rezultPrepared.executeQuery();
            //если есть, смотрим какой id у аудитории
            if (rezSet.next()) {
                id = Integer.parseInt(rezSet.getString("id"));
            }


            rezultPrepared = conn.prepareStatement("INSERT INTO list_pc_ip (id_classroom, name, ip, status) VALUES(?, ?, ?, ?)");
            rezultPrepared.setInt(1, id);
            rezultPrepared.setString(2, namePC);
            rezultPrepared.setString(3, IP);
            rezultPrepared.setInt(4, 0);
            rezultPrepared.execute();

            rezultPrepared.close();

            return true;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return false;
    }

    //отправить список ПК
    public ArrayList<String[]> getPC_IPList(String name) {
        ArrayList<String[]> listPC = new ArrayList<>();
        String[] list;

        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            rezultPrepared = conn.prepareStatement("SELECT * FROM list_pc_ip INNER JOIN classroom ON list_pc_ip.id_classroom = classroom.id WHERE classroom.name=? ORDER BY list_pc_ip.name");
            rezultPrepared.setString(1, name.trim());
            rezSet = rezultPrepared.executeQuery();
            //перебираем
            while (rezSet.next()) {
                list = new String[3];
                list[0] = rezSet.getString("list_pc_ip.name");
                list[1] = rezSet.getString("ip");
                if (rezSet.getString("status").equals("0"))
                    list[2] = "запрещено";
                if (rezSet.getString("status").equals("1"))
                    list[2] = "разрешено";

                listPC.add(list);
            }

            rezultPrepared.close();
            rezSet.close();
            return listPC;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    public ArrayList<String> getAnswerFromIdTask(ArrayList<Integer> listId) {
        ArrayList<String> listAnswer = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, userName, password);        //устанавливаем соединение с БД

            for (int i=0; i<listId.size(); i++) {
                //есть ли в БД текстовое задание для данного номера задачи
                rezultPrepared = conn.prepareStatement("SELECT answer FROM list_task WHERE id_t=?");
                rezultPrepared.setInt(1, listId.get(i));
                rezSet = rezultPrepared.executeQuery();
                //если есть
                if (rezSet.next()) {
                    listAnswer.add(rezSet.getString("answer"));
                }
            }
            rezultPrepared.close();
            rezSet.close();
            return listAnswer;
        }
        catch (SQLException ex)
        {    System.out.println("SQLException caught");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
//                System.out.println("SQLState  : " + ex.getSQLState());
//                System.out.println("ErrorCode : " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }
                catch (Exception e) { }
            }
        }
        return null;
    }

    public static ArrayList<Integer> getListIdTask() {
        return listIdTask;
    }
}
