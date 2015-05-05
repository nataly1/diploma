/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natasha.window;

import ru.natasha.message.MessageProfessor;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.natasha.connect.ConnectWithServer.*;


/**
 * Created by Наташа on 22.03.2015.
 */
public class AddIPDialog extends JDialog {

    private JPanel panel;
    private JLabel label1;
    private JLabel label2;
    private JTextField pcName;
    private JTextField ip;
    private JLabel errorName;
    private JLabel errorIP;

    private JButton addPC;
    private JButton cancel;

    public static Boolean isEditDialog;
    public static AddIPDialog dialog = null;


    private SpringLayout spr = new SpringLayout();
    private SpringLayout sprPanel = new SpringLayout();

    public AddIPDialog() {
        this.setSize(305, 210);
        this.setLocation(330, 330);

        this.setLayout(spr);
        //создаем и добавляем панель
        panel = new JPanel();
        panel.setLayout(sprPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.setPreferredSize(new Dimension(280, 165));
        this.add(panel);

        spr.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, this);

        //создаем и добавляем лайблы
        label1 = new JLabel();
        label1.setText("Название ПК:");
        panel.add(label1);
        sprPanel.putConstraint(SpringLayout.NORTH, label1, 15, SpringLayout.NORTH, panel);
        sprPanel.putConstraint(SpringLayout.WEST, label1, 15, SpringLayout.WEST, panel);

        label2 = new JLabel();
        label2.setText("IP-адрес ПК:");
        panel.add(label2);
        sprPanel.putConstraint(SpringLayout.NORTH, label2, 15, SpringLayout.SOUTH, label1);
        sprPanel.putConstraint(SpringLayout.WEST, label2, 20, SpringLayout.WEST, panel);

        //создаем и добавляем поля для ввода
        pcName = new JTextField(10);
        panel.add(pcName);
        sprPanel.putConstraint(SpringLayout.NORTH, pcName, 13, SpringLayout.NORTH, panel);
        sprPanel.putConstraint(SpringLayout.WEST, pcName, 10, SpringLayout.EAST, label1);
        pcName.addMouseListener(new addIPListener());

        ip = new JTextField(10);
        ip.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
        panel.add(ip);
        sprPanel.putConstraint(SpringLayout.NORTH, ip, 13, SpringLayout.SOUTH, pcName);
        sprPanel.putConstraint(SpringLayout.WEST, ip, 10, SpringLayout.EAST, label2);
        ip.addMouseListener(new addIPListener());

        //создаем и добавляем лейблы для ошибок
        errorName = new JLabel();
        errorName.setForeground(Color.RED);
        errorName.setText("!");
        errorName.setVisible(false);
        panel.add(errorName);
        sprPanel.putConstraint(SpringLayout.NORTH, errorName, 15, SpringLayout.NORTH, panel);
        sprPanel.putConstraint(SpringLayout.WEST, errorName, 5, SpringLayout.EAST, pcName);

        errorIP = new JLabel();
        errorIP.setForeground(Color.RED);
        errorIP.setText("!");
        panel.add(errorIP);
        sprPanel.putConstraint(SpringLayout.NORTH, errorIP, 15, SpringLayout.SOUTH, errorName);
        sprPanel.putConstraint(SpringLayout.WEST, errorIP, 5, SpringLayout.EAST, ip);

        //создаем и добавляем кнопки
        addPC = new JButton("Добавить");
        panel.add(addPC);
        sprPanel.putConstraint(SpringLayout.NORTH, addPC, 30, SpringLayout.SOUTH, ip);
        sprPanel.putConstraint(SpringLayout.WEST, addPC, 15, SpringLayout.WEST, panel);
        addPC.addMouseListener(new addIPListener());

        cancel = new JButton("Отмена");
        panel.add(cancel);
        sprPanel.putConstraint(SpringLayout.NORTH, cancel, 30, SpringLayout.SOUTH, ip);
        sprPanel.putConstraint(SpringLayout.WEST, cancel, 25, SpringLayout.EAST, addPC);
        cancel.addMouseListener(new addIPListener());

        //задаем нормальное отображение
        pcNameSetNormalBorder();
        ipSetNormalBorder();

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        //если это не окно для редактирования
        if (!isEditDialog) {
            dialog = this;
            this.setModal(true);
            this.setVisible(true);
        }
    }

    public AddIPDialog(String namePC, String IP) {
        this();
        //записываем текст
        pcName.setText(namePC);
        ip.setText(IP);

        //изменяяем поведение кнопки
        addPC.setText("Изменить");

        dialog = this;

        this.setModal(true);
        this.setVisible(true);
    }


    public void disposeWindow() {
        this.dispose();
    }

    private void pcNameSetErrorBorder() {
        pcName.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        errorName.setVisible(true);
    }

    private void pcNameSetNormalBorder() {
        pcName.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
        errorName.setVisible(false);
    }

    private void ipSetErrorBorder() {
        ip.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        errorIP.setVisible(true);
    }

    private void ipSetNormalBorder() {
        ip.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
        errorIP.setVisible(false);
    }

    private class addIPListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource().equals(pcName)) {
                pcNameSetNormalBorder();

                System.out.println("На поле для имени");
            }

            if (e.getSource().equals(ip)) {
                ipSetNormalBorder();

                System.out.println("На поле для IP");
            }

            if (e.getSource().equals(cancel)) {
                disposeWindow();
                System.out.println("Отмена добавления");
            }

            if (e.getSource().equals(addPC)) {
                //устанавливаем шаблон для ввода IP
                //Pattern IP = Pattern.compile("[0-9]{1,3}[.][0-9]{1,3}[.][0-9/]{1,3}[.][0-9]{1,3}");

//              String IPADDRESS_PATTERN =
//                  "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
//                      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

                //  $     - (доллар) конец проверяемой строки
                //  .     - (точка) представляет собой сокращенную форму записи для символьного класса, совпадающего с любым символом
                //  |     -  означает «или». Подвыражения, объединенные этим способом, называются альтернативами (alternatives)
                //  ?     - (знак вопроса) означает, что предшествующий ему символ является необязательным
                //  +     -  обозначает «один или несколько экземпляров непосредственно предшествующего элемента
                //  *     –  любое количество экземпляров элемента (в том числе и нулевое)
                //  \\d   –  цифровой символ

                String IP_PATTERN =
                        "^([1]\\d?\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([0]|[01]?\\d?\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([0]|[01]?\\d?\\d?|2[0-4]\\d|25[0-5])$";

                Pattern IP = Pattern.compile(IP_PATTERN);

                Matcher matcher = IP.matcher(ip.getText().trim());

                //если поле с именем ПК пустое или IP не соответствует шаблону
                if (pcName.getText().trim().equals("") || !matcher.matches()) {

                    if (pcName.getText().trim().equals("")) {
                        pcNameSetErrorBorder();

                        System.out.println("Поле не должно быть пустым");
                    }

                    if (!matcher.matches()) {
                        ipSetErrorBorder();

                        System.out.println("Неверный формат IP");
                    }
                }

                else {
                    ArrayList<String[]> addPC = new ArrayList<>();
                    String[] PC = new String[3];

                    if (!isEditDialog) {
                        //имя аудитории
                        PC[0] = String.valueOf(MainWindow.getClassRoomTableModel().getValueAt(MainWindow.getClassRoomTable().getSelectedRow(), 0));
                        //имя ПК
                        PC[1] = pcName.getText().trim();
                        //IP - адрес
                        PC[2] = ip.getText().trim();
                        addPC.add(PC);

                        sendData(new MessageProfessor(ADD_IP, addPC));

                        System.out.println("Добавить в " + addPC.get(0)[0] + " ПК " + addPC.get(0)[1] + " c IP " + addPC.get(0)[2]);
                    }

                    else {
                        //старый IP
                        PC[0] = String.valueOf(MainWindow.getPC_IPModel().getValueAt(MainWindow.getPC_IPTable().getSelectedRow(), 1));
                        //новое имя ПК
                        PC[1] = pcName.getText().trim();
                        //новый IP - адрес
                        PC[2] = ip.getText().trim();
                        addPC.add(PC);

                        sendData(new MessageProfessor(RENAME_IP, addPC));

                        System.out.println("Измнить в ПК " + addPC.get(0)[1] + " c IP " + addPC.get(0)[2]);
                    }
                }
            }
        }
    }
}
