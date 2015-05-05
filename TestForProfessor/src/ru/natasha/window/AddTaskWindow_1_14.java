package ru.natasha.window;

import ru.natasha.message.MessageProfessor;
import ru.natasha.task.Tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static ru.natasha.connect.ConnectWithServer.ADD_TASK;
import static ru.natasha.connect.ConnectWithServer.sendData;

/**
 * Created by Наташа on 22.03.2015.
 */
public class AddTaskWindow_1_14 extends JFrame {
    private AddTaskWindow_1_14 myWindow_1_14 = null;

    private JTextArea textTask;    //поле, в котором отображается задание
//    private JTextPane textTask;    //поле, в котором отображается задание
    private JScrollPane scrollPanetextTask;
    private JButton addNew;     //кнопка для добавления варианта ответа
//    private JButton setSup;
    private JPanel northPanel;
    private JCheckBox itemCheckBox;
    private JRadioButton itemRadioButton;
    private ButtonGroup groupRadio;
//    private JTextPane itemTextPane;
    private JTextField itemTextField;
    private JButton deleteItem;
    private JPanel itemPanel;
    private JPanel centrePanel;
    private JScrollPane scrollPane;
    private JButton addOk;
    private int numberTask;

    private ArrayList<JTextField> textFields = new ArrayList<JTextField>();
//    private ArrayList<JTextPane> textPanes = new ArrayList<JTextPane>();
    private ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
    private ArrayList<JRadioButton> radioButtons = new ArrayList<JRadioButton>();
    private ArrayList<JButton> buttons = new ArrayList<JButton>();
    private ArrayList<JPanel> panels = new ArrayList<JPanel>();

    //для отправки задания на сервер
    ArrayList<String> listTask;     //текст в JTextField
    ArrayList<Integer> listAnswer;

    SpringLayout spr = new SpringLayout();
    SpringLayout spr2 = new SpringLayout();

    public AddTaskWindow_1_14(Integer numberTask, String text) {
        super("Добавить набор ответов для задачи " + numberTask);
        this.numberTask = numberTask;

        //NORTH
        northPanel = new JPanel();
        northPanel.setLayout(spr2);
        northPanel.setPreferredSize(new Dimension(400, 60));
//        textTask = new JTextPane();
//        HTMLEditorKit kit = new HTMLEditorKit();
//        textTask.setEditorKit(kit);
//        textTask.setContentType("text/html");
//        textTask.setText("Задание");
//        textTask.addMouseListener(new MyMouseClicked());
//        textTask.setContentType("text/html; charset=windows-1251");
//        textTask.setPreferredSize(new Dimension(310, 50));
        textTask = new JTextArea(3, 29);
        textTask.setText(text);
        textTask.setLineWrap(true);
        textTask.addMouseListener(new StandardColor());
        textTask.setEditable(true);
//        textTask.setBackground(new Color(238, 238, 238));
        scrollPanetextTask = new JScrollPane(textTask);

        northPanel.add(scrollPanetextTask);
        addNew = new JButton("+");
        addNew.setToolTipText("Добавить вариант ответа");
        addNew.addActionListener(new MyListener());
//        setSup = new JButton("Up");
//        setSup.addActionListener(new MyUpListener());
//        northPanel.add(setSup);
        northPanel.add(addNew);

        spr2.putConstraint(SpringLayout.NORTH, scrollPanetextTask, 3, SpringLayout.NORTH, this);
        spr2.putConstraint(SpringLayout.WEST, scrollPanetextTask, 3, SpringLayout.WEST, this);
//        spr2.putConstraint(SpringLayout.NORTH, setSup, 3, SpringLayout.NORTH, this);
//        spr2.putConstraint(SpringLayout.WEST, setSup, 10, SpringLayout.EAST, scrollPanetextTask);
        spr2.putConstraint(SpringLayout.NORTH, addNew, 26, SpringLayout.NORTH, this);
        spr2.putConstraint(SpringLayout.WEST, addNew, 4, SpringLayout.EAST, scrollPanetextTask);


        //CENTRE
        centrePanel = new JPanel();
        scrollPane = new JScrollPane(centrePanel);
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        if (numberTask>=5 & numberTask<=14)
            groupRadio = new ButtonGroup();

        //SOUTH
        addOk = new JButton("Готово");
        addOk.setToolTipText("Добавить вариант ответов в БД");
        addOk.addActionListener(new MyListener());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(addOk, BorderLayout.SOUTH);

        myWindow_1_14 = this;

        this.setPreferredSize(new Dimension(400, 400));
        this.pack();
        this.setLocation(600, 150);
        this.setVisible(true);
        this.setResizable(false);
//        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("dispose");

                MainWindow.myWindow.remove(myWindow_1_14);

                System.out.println("Осталось открыто " + MainWindow.myWindow.size());
                dispose();
            }
        });
    }

    private class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(addNew)) {
                System.out.println("Добавить");

                itemPanel = new JPanel();
                panels.add(itemPanel);
//                itemTextPane = new JTextPane();
//                itemTextPane.setPreferredSize(new Dimension(280, 30));
//                itemTextPane.setContentType("text/html");
//                itemTextPane.setText("Укажите вариант ответа");
//                itemTextPane.addMouseListener(new MyMouseItemPaneClicked(itemPanel));
//                textFields.add(itemTextPane);
                itemTextField = new JTextField(25);
                itemTextField.addMouseListener(new StandardColor(itemPanel));
                textFields.add(itemTextField);

                deleteItem = new JButton("-");
                deleteItem.setToolTipText("Удалить вариант ответа");
                deleteItem.addActionListener(new MyDeleteItemListener(itemPanel));
                buttons.add(deleteItem);

                if (numberTask>=1 & numberTask<=4) {
                    itemCheckBox = new JCheckBox();
                    itemCheckBox.setToolTipText("Указать верный ответ");
                    checkBoxes.add(itemCheckBox);
                    itemPanel.add(itemCheckBox);

                    spr.putConstraint(SpringLayout.NORTH, itemCheckBox, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, itemCheckBox, 5, SpringLayout.WEST, itemPanel);
                    spr.putConstraint(SpringLayout.NORTH, itemTextField, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, itemTextField, 8, SpringLayout.EAST, itemCheckBox);
                    spr.putConstraint(SpringLayout.NORTH, deleteItem, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, deleteItem, 8, SpringLayout.EAST, itemTextField);
                }

                if (numberTask>=5 & numberTask<=14) {
                    itemRadioButton = new JRadioButton();
                    itemRadioButton.setToolTipText("Указать ответ как правильный");
                    radioButtons.add(itemRadioButton);
                    groupRadio.add(itemRadioButton);
                    itemPanel.add(itemRadioButton);

                    spr.putConstraint(SpringLayout.NORTH, itemRadioButton, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, itemRadioButton, 5, SpringLayout.WEST, itemPanel);
                    spr.putConstraint(SpringLayout.NORTH, itemTextField, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, itemTextField, 8, SpringLayout.EAST, itemRadioButton);
                    spr.putConstraint(SpringLayout.NORTH, deleteItem, 5, SpringLayout.NORTH, itemPanel);
                    spr.putConstraint(SpringLayout.WEST, deleteItem, 8, SpringLayout.EAST, itemTextField);
                }

                itemPanel.add(itemTextField);
                itemPanel.add(deleteItem);
                centrePanel.add(itemPanel);

                scrollPane.revalidate();
            }

            if (e.getSource().equals(addOk)) {
                System.out.println("Отправить");
                listTask = new ArrayList<String>();     //текст в JTextField
                listAnswer = new ArrayList<Integer>();
                int countTrue = 0;      //общее кол-во выбранных JCheckBox
                if (numberTask>=1 & numberTask<=4) {
                    //проходим по списку, в котором все элементы JCheckBox, которые есть, смотрим его статус JCheckBox
                    for (JCheckBox c : checkBoxes) {
                        if (c.isSelected()) {
//                          listAnswer.add(textPanes.get(checkBoxes.indexOf(c)).getText().trim());
                            listAnswer.add(checkBoxes.indexOf(c));
                            countTrue++;
                        }
                    }
                }

                if (numberTask>=5 & numberTask<=14) {
                    for (JRadioButton c : radioButtons) {
                        if (c.isSelected()) {
//                            listAnswer.add(textPanes.get(radioButtons.indexOf(c)).getText().trim());
                            listAnswer.add(radioButtons.indexOf(c));
                            countTrue++;
                        }
                    }
                }

                //проходим по списку, в котором все элементы JTextField, которые есть, считываем текст
                for (JTextField f : textFields) {
                    listTask.add(f.getText().trim());
                }
//                for (JTextPane f : textPanes) {
//                    countElement.add(f.getText().trim());
//                }

                //если не указан вариант в каком-то из полей
                if (listTask.indexOf("") != -1) {
                    for (int i = 0; i < listTask.size(); i++) {
                        if (listTask.get(i).equals("")) {
                            itemTextField = textFields.get(i);
                            itemTextField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

                        }
                    }

                    JOptionPane.showMessageDialog(null, "Введите ответ в выделенные поля", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                }

                if (textTask.getText().trim().equals("")) {
                    textTask.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    JOptionPane.showMessageDialog(null, "Введите текст задания", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                }

                if (countTrue == 0)
                    JOptionPane.showMessageDialog(null, "Задайте правильный ответ", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                else
                    addTask();
            }
        }
    }


    private void addTask() {
        sendData(new MessageProfessor(ADD_TASK, new Tasks(numberTask, listTask, listAnswer, textTask.getText().trim())));

        Tasks addTask = new Tasks(numberTask, listTask, listAnswer, textTask.getText().trim());
        System.out.print(addTask.getNumber() + "        ");      //номер задачи
        System.out.print(addTask.getListTask() + "        ");
        System.out.println(addTask.getListAnswer() + "        ");
        System.out.println(addTask.getVerbalTask() + "        ");
    }

    private class MyDeleteItemListener implements ActionListener {
        private JPanel panel;

        MyDeleteItemListener(JPanel p) {
            this.panel = p;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //индекс элемента panel в панели-контейнере centrePanel
            //по нему будем удалять элементы из ArrayList-ов и сам элемент panel из centrePanel
            int indexP = centrePanel.getComponentZOrder(panel);
            System.out.println(indexP + "");
            //удаляем из списков
            itemPanel = panels.remove(indexP);
            if (numberTask>=1 & numberTask<=4) {
                itemCheckBox = checkBoxes.remove(indexP);
                itemTextField = textFields.remove(indexP);
            }

            if (numberTask>=5 & numberTask<=14) {
                itemRadioButton = radioButtons.remove(indexP);
                groupRadio.remove(itemRadioButton);
            }

            deleteItem = buttons.remove(indexP);
            //удаляем панель
            centrePanel.remove(itemPanel);
            centrePanel.repaint();
            scrollPane.revalidate();
        }
    }

    private class StandardColor extends MouseAdapter {
        private JPanel panel;

        StandardColor(JPanel p) {
            this.panel = p;
        }

        StandardColor() {}

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource().equals(textTask)) {
                textTask.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
            }
            else {
                //индекс элемента panel в панели-контейнере centrePanel
                //по нему будем удалять элементы из ArrayList-ов и сам элемент panel из centrePanel
                int indexP = centrePanel.getComponentZOrder(panel);
                itemTextField = textFields.get(indexP);
                itemTextField.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
            }
        }
    }

//    private class MyUpListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (e.getSource().equals(setSup)) {
//                try {
//                    if (!(textTask.equals(null) & textTask.getSelectedText().equals(null))) {
//
//                        setUpText(textTask);
//                        System.out.println(textPanes.size());
//                    }
//
//                    else if (!textPanes.equals(null)) {
//                        System.out.println("Список не пуст");
//
//                        for (int i=0; i<textPanes.size(); i++) {
//                            if (!(textPanes.get(i).getSelectedText().equals(null))) {
//                                itemTextPane = textPanes.get(i);
//
//                                setUpText(itemTextPane);
//                                System.out.println("itemTextPane");
//                            }
//                        }
//                    }
//                    else {System.out.println("Список пуст");}
//                }
//                catch (NullPointerException e1) {
////                    JOptionPane.showMessageDialog(null, "Выдели текст", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//    }

//    private void setUpText(JTextPane textPane) {
//        System.out.println(textPane.getSelectedText() + " " + textPane.getSelectionStart() + " " + textPane.getSelectionEnd());
//        int start = textPane.getSelectionStart();
//        int end = textPane.getSelectionEnd();
//        String text = textPane.getText().replaceAll("<html>", "").replaceAll("<head>", "").replaceAll("</head>", "").replaceAll("<body>", "").replaceAll("</body>", "").replaceAll("<sup>", "").replaceAll("</sup>", "").replaceAll("</html>", "").trim().toString();
//        int lenText = countSymbol(text);
////                        System.out.println();
////                        System.out.println("Текст "+text);
////                        System.out.println("Длина "+lenText);
//        try {
//            String m = "<html>" + textPane.getText(0, start) + "<sup>" + textPane.getSelectedText() + "</sup>" + textPane.getText(end, lenText-end+1);
////                            String m2  = "<html><sup>" + textTask.getSelectedText() + "</sup>";
//
//            textPane.setText(m);
//        } catch (BadLocationException e1) {
////                            e1.printStackTrace();
//        }
//    }


//    //считаем кол-во символов в строке. На вход строка в виде: &#1081;&#1094;&#1081;&#1094;
//    private int countSymbol(String str) {
//        int count = 0;
//        char s1 = '&';
//        char s2 = '#';
//
//        for (int i = 0; i < str.length(); i++) {
//            if (str.charAt(i) == s1) {
//                if (str.charAt(i+1) == s2) {
////                    System.out.print(str.charAt(i));
//                    i += 6;
//                }
//            }
////                System.out.print(str.charAt(i));
//            count++;
//        }
//        return count;
//    }

//    private class MyMouseClicked extends MouseAdapter {
//        int c = 0;
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            if (e.getSource().equals(textTask)) {
//                if (c == 0) {
//                    textTask.setText("");
//                    c++;
//                }
//            }
//        }
//    }
//
//    private class MyMouseItemPaneClicked extends MouseAdapter {
//        private JPanel panel;
//        int c = 0;
//
//        MyMouseItemPaneClicked(JPanel p) {
//            this.panel = p;
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            if (c == 0) {
//                int indexP = centrePanel.getComponentZOrder(panel);
//                itemTextPane = textPanes.get(indexP);
//                itemTextPane.setText("");
//            }
//        }
//    }
}
