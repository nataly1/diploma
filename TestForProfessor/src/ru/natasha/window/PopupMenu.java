package ru.natasha.window;

import ru.natasha.message.MessageProfessor;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import static ru.natasha.connect.ConnectWithServer.*;

public class PopupMenu implements ActionListener {
    private JComponent jComponent;
    private JPopupMenu popup;
    private JMenuItem menuItem;

    private ArrayList<JMenuItem> jMenuItems;

    private MouseListener popupListener;

    public void createPopupMenuClassRoom(JComponent jComponent) {
        this.jComponent = jComponent;
        jMenuItems = new ArrayList<>();     //будем хранить список menuItem

        //Создаем меню
        popup = new JPopupMenu();
        popup.setName("ClassRoom");
        menuItem = new JMenuItem("Удалить аудиторию");
        menuItem.setName("deleteRoom");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Переименовать");
        menuItem.setName("renameRoom");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Добавить новую аудиторию");
        menuItem.setName("addRoom");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);

        //Добавляем слушателя к компоненте
        popupListener = new PopupListener(popup);
        jComponent.addMouseListener(popupListener);
    }

    public void createPopupMenuPC_IP(JComponent jComponent) {
        this.jComponent = jComponent;
        jMenuItems = new ArrayList<>();     //будем хранить список menuItem

        //Create the popup menu.
        popup = new JPopupMenu();
        popup.setName("PC");
        menuItem = new JMenuItem("Удалить ПК");
        menuItem.setName("deletePC");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Редактировать данные");
        menuItem.setName("renamePC");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Изменить статус");
        menuItem.setName("exchangeStatus");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Добавить новый ПК");
        menuItem.setName("addPC");
        jMenuItems.add(menuItem);
        menuItem.addActionListener(this);
        popup.add(menuItem);

        //Добавляем слушателя к компоненте
        popupListener = new PopupListener(popup);
        jComponent.addMouseListener(popupListener);
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        String nameClass;
//        String s = "Event source: " + source.getName()
//                + " (an instance of " + getClassName(source) + ")";
//        System.out.println(s );

        System.out.println("Функция     "+source.getName());

        //смотрим, какое действие надо выполнить
        switch (source.getName()) {
            //удалить аудиторию
            case "deleteRoom":
                //берем имя аудитории, отправляем запрос на сервер
                sendData(new MessageProfessor(DELETE_CLASSROOM, String.valueOf(MainWindow.getClassRoomTableModel().getValueAt(MainWindow.getClassRoomTable().getSelectedRow(), 0))));
                //очищаем таблицу с ПК
                MainWindow.addPC_IP(new ArrayList<String[]>());

                //если есть предыдущий, делаем его select
                if (MainWindow.getClassRoomTable().getSelectedRow() > 0) {
                    MainWindow.getClassRoomTable().setRowSelectionInterval(MainWindow.getClassRoomTable().getSelectedRow()-1, MainWindow.getClassRoomTable().getSelectedRow()-1);

                    sendData(new MessageProfessor(LIST_PC_IP, String.valueOf(MainWindow.getClassRoomTableModel().getValueAt(MainWindow.getClassRoomTable().getSelectedRow(), 0))));
                }
                //иначе удаляем выделение с удаляемого элемента
                else {
                    MainWindow.getClassRoomTable().removeRowSelectionInterval(MainWindow.getClassRoomTable().getSelectedRow(), MainWindow.getClassRoomTable().getSelectedRow());
                }
                break;

            case "renameRoom":
                nameClass = (String)JOptionPane.showInputDialog(null, "Введите новое имя для аудитории", "Добавить аудиторию",
                        JOptionPane.QUESTION_MESSAGE);

                //если пользователь что-то ввел и новое имя не совпадает с предыдущим
                if ((nameClass != null) && (nameClass.length() > 0) && !(nameClass.equals(MainWindow.getClassRoomTableModel().getValueAt(MainWindow.getClassRoomTable().getSelectedRow(), 0))) ) {
                    //отправляем на сервер
                    System.out.println(nameClass);

                    ArrayList<String[]> st = new ArrayList<>();
                    String[] el = new String[1];

                    //исходное
                    el[0] = String.valueOf(MainWindow.getClassRoomTableModel().getValueAt(MainWindow.getClassRoomTable().getSelectedRow(), 0));
                    st.add(el);
                    //на что заменить
                    el = new String[1];
                    el[0] = nameClass.trim();
                    st.add(el);

                    sendData(new MessageProfessor(RENAME_CLASSROOM, st));

                    System.out.println("Переименовать "+st.get(0)[0]+" в " +st.get(1)[0]);
                }
                break;

            case "addRoom":
                nameClass = (String)JOptionPane.showInputDialog(null, "Введите имя аудитории", "Добавить аудиторию",
                        JOptionPane.QUESTION_MESSAGE);

                //если пользователь что-то ввел
                if ((nameClass != null) && (nameClass.length() > 0)) {
                    //отправляем на сервер
                    System.out.println(nameClass);
                    sendData(new MessageProfessor(ADD_CLASSROOM, nameClass.trim()));
                }
                break;

            case "deletePC":
                //берем IP-адресс ПК, отправляем запрос на сервер
                sendData(new MessageProfessor(DELETE_IP, String.valueOf(MainWindow.getPC_IPModel().getValueAt(MainWindow.getPC_IPTable().getSelectedRow(), 1))));
                MainWindow.getPC_IPTable().clearSelection();
                break;

            case "renamePC":
                AddIPDialog.isEditDialog = true;
                new AddIPDialog(String.valueOf(MainWindow.getPC_IPModel().getValueAt(MainWindow.getPC_IPTable().getSelectedRow(), 0)), String.valueOf(MainWindow.getPC_IPModel().getValueAt(MainWindow.getPC_IPTable().getSelectedRow(), 1)));
                break;

            case "exchangeStatus":
                //берем IP-адресс ПК, отправляем запрос на сервер
                sendData(new MessageProfessor(EXCHANGE_STATUS_IP, String.valueOf(MainWindow.getPC_IPModel().getValueAt(MainWindow.getPC_IPTable().getSelectedRow(), 1))));
                break;

            case "addPC":
                AddIPDialog.isEditDialog = false;
                new AddIPDialog();
                break;
        }
    }

    //возвращаем имя класса объекта
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }

    class PopupListener extends MouseAdapter {

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());

                //если вызван слушатель для Аудиторий
                if (popup.getName().equals("ClassRoom")) {
                    //если нажали на скроле
                    if (jComponent.getComponentAt(e.getX(), e.getY()) == null) {
                        setEnableMenuItem(0, 2, false);

                        MainWindow.getClassRoomTable().clearSelection();
                        MainWindow.getPC_IPModel().addDate(new ArrayList<String[]>());
                        MainWindow.getPC_IPTable().updateUI();

                        System.out.println("На скроле ClassRoom");
                    }
                    //иначе нажали на таблице
                    else {
                        //если при этом выделена какая-то строка таблицы
                        if (MainWindow.getClassRoomTable().isRowSelected(MainWindow.getClassRoomTable().getSelectedRow())) {

                            setEnableMenuItem(0, 2, true);
                        }
                        //если нет выделенной ячейки
                        else {
                            setEnableMenuItem(0, 2, false);
                        }

                        System.out.println( getClassName(jComponent.getComponentAt(e.getX(), e.getY())) + " ClassRoom");
                    }
                }

                //если вызван слушатель для ПК
                if (popup.getName().equals("PC")) {
                    //если нажали на скроле
                    if (jComponent.getComponentAt(e.getX(), e.getY()) == null) {

                        if ((MainWindow.getClassRoomTable().getRowCount() == 0) || (MainWindow.getClassRoomTable().getSelectedRow() == -1)){
                        //снимаем выделение с таблицы с аудиториями
                            setEnableMenuItem(0, 4, false);
                        }

                        else {
                            setEnableMenuItem(0, 3, false);
                            setEnableMenuItem(3, 1, true);
                        }


                        MainWindow.getPC_IPTable().clearSelection();
                        MainWindow.getPC_IPTable().updateUI();

                        System.out.println("На скроле PC. Выделена строка " + (MainWindow.getClassRoomTable().getSelectedRow()) + " аудиторий " + MainWindow.getClassRoomTable().getRowCount());
                    }

                    //иначе нажали на таблице
                    else {
                        //если при этом выделена какая-то ячейка таблицы
                        if (MainWindow.getPC_IPTable().isRowSelected(MainWindow.getPC_IPTable().getSelectedRow())) {
                            setEnableMenuItem(0, 3, true);

                        }
                        //если нет выделенной ячейки
                        else {
                            setEnableMenuItem(0, 3, false);
                            setEnableMenuItem(3, 1, true);
                        }

                        System.out.println( getClassName(jComponent.getComponentAt(e.getX(), e.getY())) + " PC");
                    }
                }
            }
        }
    }


    //задаем свойство Enable
    private void setEnableMenuItem (int start, int count, boolean status) {
        for (int i=start; i<(start+count); i++) {
            menuItem = jMenuItems.get(i);
            menuItem.setEnabled(status);
        }
    }
}
