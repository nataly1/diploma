import ru.natasha.server.Server;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.run();

//        ArrayList<TaskStudent> taskStudents = new ConnectWithDB().getDemoTestFromDB();
//
//        for ( TaskStudent s : taskStudents)
//        {
//            System.out.print(s.getNumberTask() + "        ");      //номер задачи
//            System.out.print(s.getTask() + "        ");   //задача
//            System.out.println(s.getAnswer());   //ответ
//        }
//
//        System.out.println("В listTasks");
//        System.out.println("номер задачи    задача");
//        //посмотрим, что лежит в listTasks
//
//
//        for ( Integer s : connectWithDB.getNumber())
//        {
//            System.out.print(s + "        ");      //номер задачи
//            System.out.println(connectWithDB.getTask(s));   //задача
//        }
//
//        System.out.println();
//        System.out.println("ID");
//            for (Integer s : ConnectWithDB.getListIdTask()) { System.out.println(s); }
    }
}
