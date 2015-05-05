package ru.natasha.window;

import ru.natasha.message.MessageProfessor;
import ru.natasha.window.renderer.RenderClassRoom;
import ru.natasha.window.renderer.RendererPC_IP;
import ru.natasha.window.tableModel.ClassRoomTableModel;
import ru.natasha.window.tableModel.PC_IPTableModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static ru.natasha.connect.ConnectWithServer.*;
/**
 * Created by Natasha on 15.03.2015.
 */


public class MainWindow extends JFrame {
    public static Font myFont = new Font(null, Font.BOLD, 16);
    public static Font myFont16 = new Font(null, Font.BOLD, 24);


    public static ArrayList<JFrame> myWindow = new ArrayList<>();

    private SpringLayout spr = new SpringLayout();

    private JMenuBar menuBar = new JMenuBar();
    JMenu newMenu = new  JMenu("Задание");
//    JMenu Menu = new  JMenu("Результаты");


    private static ClassRoomTableModel classRoomTableModel;    //модель таблицы  classRoom
    private static JTable classRoomTable;      //отображение аудиторий из БД
    private JScrollPane classRoomScroll;        //содержит элемент classRoomTable

    private static PC_IPTableModel pcIpTableModel;
    private static JTable pcIPTable;
    private JScrollPane pcIPScroll;

    //создаем меню-бар
    PopupMenu popupClassRoom;
    PopupMenu popupPC;

    private String nameItem;

    public MainWindow() {
        this.setTitle("Приложение для преподавателя");
        this.setLayout(spr);

        setMenus();

        //создаем меню-бар для правой кнопки
        popupClassRoom = new PopupMenu();
        popupPC = new PopupMenu();

        //создаем модель таблицы
        classRoomTableModel = new ClassRoomTableModel();
        classRoomTable = new JTable(classRoomTableModel);
        classRoomTable.addMouseListener(new classRoomTableListener());
        //запретить перетаскивание столбцов
        classRoomTable.getTableHeader().setReorderingAllowed(false);
        classRoomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   //только по одной строчке можно выделять

        //отключить возможность изменять ширину стобцов
        for (int i=0;i<classRoomTable.getColumnModel().getColumnCount();i++){
            classRoomTable.getColumnModel().getColumn(i).setResizable(false);
        }

        classRoomScroll = new JScrollPane(classRoomTable);
        classRoomScroll.setPreferredSize(new Dimension(100, 150));
        classRoomScroll.setVisible(true);
        classRoomScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Аудитории"), BorderFactory.createEmptyBorder(5, 3, 3, 1)));

        //для всех столбцов устанавливаем один и тот же render
        for (int i=0; i<classRoomTableModel.getColumnCount(); i++) {
            classRoomTable.getColumnModel().getColumn(i).setCellRenderer(new RenderClassRoom());
        }

        this.add(classRoomScroll);
        spr.putConstraint(SpringLayout.NORTH, classRoomScroll, 30, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, classRoomScroll, 35, SpringLayout.WEST, this);
        //создаем и устанавливаем меню-бар для скрола с Аудиториями
        popupClassRoom.createPopupMenuClassRoom(classRoomScroll);
        popupClassRoom.createPopupMenuClassRoom(classRoomTable);


        pcIpTableModel = new PC_IPTableModel();
        pcIPTable = new JTable(pcIpTableModel);
        //запретить перетаскивание столбцов
        pcIPTable.getTableHeader().setReorderingAllowed(false);
//        pcIPTable.setCellSelectionEnabled(true);      //только по одной ячейке можно выделять
        pcIPTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   //только по одной строчке можно выделять

        //отключить возможность изменять ширину стобцов
        for (int i=0;i<pcIPTable.getColumnModel().getColumnCount();i++){
            pcIPTable.getColumnModel().getColumn(i).setResizable(false);
        }

        pcIPScroll = new JScrollPane(pcIPTable);
        pcIPScroll.setPreferredSize(new Dimension(300, 170));
        pcIPScroll.setVisible(true);
        pcIPScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Список ПК"), BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        //для всех столбцов устанавливаем один и тот же render
        for (int i=0; i<pcIpTableModel.getColumnCount(); i++) {
            pcIPTable.getColumnModel().getColumn(i).setCellRenderer(new RendererPC_IP());
        }

        this.add(pcIPScroll);
        spr.putConstraint(SpringLayout.NORTH, pcIPScroll, 30, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, pcIPScroll, 35, SpringLayout.EAST, classRoomScroll);
        //устанавливаем меню на скроле и таблице для ПК
        popupPC.createPopupMenuPC_IP( pcIPScroll);
        popupPC.createPopupMenuPC_IP( pcIPTable);

        this.setJMenuBar(menuBar);
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);


    }

    //добавить данные в таблицу ClassRoom
    public void addClassRoomForTable(ArrayList<String[]> listClassRoom) {
        classRoomTableModel.addDate(listClassRoom);
        classRoomTable.updateUI();
    }

    //добавить данные в таблицу ClassRoom
    public static void addPC_IP(ArrayList<String[]> listPCIP) {
        pcIpTableModel.addDate(listPCIP);

        pcIPTable.updateUI();
    }


    private class classRoomTableListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            //если нажата левая кнопка и выделена ячейка
            if ((e.getButton() == MouseEvent.BUTTON1) & (classRoomTable.isCellSelected(classRoomTable.getSelectedRow(), 0))) {
                //BUTTON3--Правая кнопка, BUTTON2--колесо мыши
                String classRoom = String.valueOf(classRoomTableModel.getValueAt(classRoomTable.getSelectedRow(), 0));

                //запрашиваем у сервера список ПК для выбранной аудитории
                sendData(new MessageProfessor(LIST_PC_IP, classRoom));
            }
        }
    }

    public static JTable getClassRoomTable() {
            return classRoomTable;
    }

    public static ClassRoomTableModel getClassRoomTableModel() {
        return classRoomTableModel;
    }

    public static JTable getPC_IPTable() {
        return pcIPTable;
    }

    public static PC_IPTableModel getPC_IPModel() {
        return pcIpTableModel;
    }

    //в меню добавлем 10 элементов JMenu - соответствуют 10-ми вопросам в билете
    //в каждый из JMenu, в добавляем определенные элементы JMenuItem
    //для этого вызываем ф-цию setItem (new_i, j)
    //передаем текущий элемент new_i типа JMenu и j- номер задания
    private void setMenus() {
        menuBar.add(newMenu);

        for (int i=1; i<11; i++) {
            int j;
            JMenu new_i = new  JMenu("Вопрос " + i);
            //для 1-ого вопроса билета
            if (i == 1)
                for (j=1; j<5; j++)
                    setItem (new_i, j);
            //для 2-ого вопроса билета
            if (i == 2)
                for (j=5; j<15; j++)
                    setItem(new_i, j);
            //для 3-его вопроса билета
            if (i == 3)
                for (j=15; j<25; j++)
                    setItem(new_i, j);
            //для 4-ого вопроса билета
            if (i == 4)
                for (j=25; j<28; j++)
                    setItem(new_i, j);
            //для 5-ого вопроса билета
            if (i == 5)
                for (j=28; j<32; j++)
                    setItem(new_i, j);
            //для 6-ого вопроса билета
            if (i == 6)
                for (j=32; j<34; j++)
                    setItem (new_i, j);
            //для 7-ого вопроса билета
            if (i == 7)
                for (j=34; j<35; j++)
                    setItem(new_i, j);
            //для 8-ого вопроса билета
            if (i == 8)
                for (j=35; j<38; j++)
                    setItem(new_i, j);
            //для 9-ого вопроса билета
            if (i == 9)
                for (j=38; j<41; j++)
                    setItem(new_i, j);
            //для 10-ого вопроса билета
            if (i == 10)
                for (j=41; j<42; j++)
                    setItem(new_i, j);

            newMenu.add(new_i);
        }

//        menuBar.add(Menu);
    }

    //для добавления нового элемента типа JMenuItem и добавления к нему слушателя
    //передаем текущий элемент new_i типа JMenu и j- номер задания
    private void setItem (JMenu new_i, int j) {
        JMenuItem Item_j = new JMenuItem("Задача " + j);
        Item_j.setName(""+j);
        new_i.add(Item_j);
        Item_j.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameItem = Item_j.getName();

                getVerbalTask(Integer.parseInt(Item_j.getName()));

                System.out.println("Выбрали " + Item_j.getText());
            }
        });
    }

    //отправить запрос на получение вербального задания для выбранного номера
    private void getVerbalTask(Integer number){ sendData(new MessageProfessor(VERBAL_TASK, number));}

    public void openNewAddTask(String verbal) {
        //смотрим имя элемента и открываем нужное окно с нужными параметрами
        Integer number = Integer.parseInt(nameItem);

        if (number >= 1 & number <=14)
            myWindow.add(new AddTaskWindow_1_14(number, verbal));

        if (number >= 15 & number <=18)
            myWindow.add(new AddTaskWindow_15_18(number, verbal));
    }

    //отправить запрос на сервер, чтобы изменить статус доступа
    private void exchangeStatus() {
        sendData(new MessageProfessor(EXCHANGE_STATUS));
    }
}