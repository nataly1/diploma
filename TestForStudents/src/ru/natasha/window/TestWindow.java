package ru.natasha.window;

import ru.natasha.message.MessageStudent;
import ru.natasha.task.TaskStudent;
import ru.natasha.task.question.Question;
import ru.natasha.window.panels.DrawPanel;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;

import static ru.natasha.connect.ConnectWithServer.SEND_REZULT;
import static ru.natasha.connect.ConnectWithServer.sendData;


/**
 * Created by Natasha on 19.03.2015.
 */
public class TestWindow extends JFrame {
    public static final String RANDOM_TEST = "Тест";
    public static final String DEMO_TEST = "Демо-тест";

    private Timer timer;

    private JPanel northPanel;      //расположена в верхней части, отображает Label и кнопки с номерами задания
    private JLabel countTask;       //позывает какое задание выполняется в данный момент
    private JPanel numberPanel;     //содержит кнопки с номерами заданий
    private JScrollPane scrollPane; //содержит в себе numberPanel, для прокрутки
    private JButton numberTask;     //кнопка с номером задания
    private JButton patternButton;
    private JPanel centrePanel;     //расположена по центру, отображает панель с самим заданием и кнопку Сбросить
    private JScrollPane centreScrollPane;   //содержит в себе centrePanel, для отображения границ панелей
    private JPanel panelWithTask;       //содержит представление задачи
    private JTextArea verbalTaskArea;
    private JPanel panelWithPattern;
    private JScrollPane panelWithPatternScroll;
    private JButton isDone;          //кнопка Готово
    private JPanel southPanel;          //расположена в нижней части, отображает кнопки для завершения теста и перехода к след/пред заданию
    private JLabel timeLabel;
    private JButton prevTask;           //перейти к След заданию
    private JButton nextTask;           //перейти к Пред заданию
    private JButton finishTest;         //Завершить тест

    private Boolean isDoneBool;         //активирована кнопка Готово или нет
    private Boolean isFinish;           //отправили на проверку на сервер или нет

    SpringLayout sprNorthPanel = new SpringLayout();
    SpringLayout sprCentrePanel = new SpringLayout();
    SpringLayout sprSouthPanel = new SpringLayout();
    SpringLayout sprPanelWithTask = new SpringLayout();

    private ArrayList<Integer> listNumberTask;      //список номеров пришедших заданий
    private ArrayList<String> listVerbalTask;
    private ArrayList<String> listTask;             //список пришедших заданий
    private ArrayList<String> listAnswer;           //список пришедших ответов (для Демо теста)
    private int currentTask;                        //номер текущего задания (1-41)
    private ArrayList<JButton> listNumberButtons;   //список кнопок в верхней части фрейма

    //для задач 1 - 4
    private JPanel panel_1_14;
    private JCheckBox checkBox_1_4;
    private JLabel label_1_14;
    private ArrayList<JCheckBox> jCheckBoxes_1_4;
    private ArrayList<JLabel> jLabels_1_4;
    private ArrayList<String> listTask_1_14;
    private ArrayList<Integer> listAnswer_1_14;

    //для задач 5 - 14
    private ButtonGroup groupRadio;
    private JRadioButton radioButton_5_14;
    private ArrayList<JRadioButton> jRadioButtons_5_14;

    //для задачи 15
    private JLabel top;
    private JPanel stackPanel;
    private JTextField itemTextField;
    private JLabel topLast;

    private ArrayList<JTextField> textFields = new ArrayList<>();

    private int indexTop = -1;
    private int indexTopLast = -1;

    //для отправки задания на сервер
    int countElement;     //количество элементов
    SpringLayout spr15 = new SpringLayout();
    SpringLayout spr15_2 = new SpringLayout();
    SpringLayout sprItem = new SpringLayout();
    SpringLayout sprItem_2 = new SpringLayout();

    private ArrayList<String> listAnswer_15_17;

    //храним координаты предыдущего ответа для отрисовки
    private Integer[] massTopCoordinates_15 = new Integer[4];





    //храним координаты предыдущего ответа для отрисовки
    private Integer[] massTopCoordinates_17 = new Integer[4];
    private Integer[] massTopLastCoordinates_17 = new Integer[4];



    public static Question questionPool = null;

    public static int countTaskWithOutAnswer;

    public TestWindow(String win, ArrayList<TaskStudent> listTasksFromServer) {
        super(win);

        listNumberTask = new ArrayList<Integer>();      //список пришедших с сервера номеров задач
        listVerbalTask = new ArrayList<String>();
        listTask = new ArrayList<String>();
        listNumberButtons = new ArrayList<JButton>();

        isDoneBool = false;
        isFinish = false;

        for (TaskStudent s : listTasksFromServer) {
            listNumberTask.add(s.getNumberTask());
            listVerbalTask.add(s.getVerbalTask());
            listTask.add(s.getTask());
        }

        //создаем переменную, в которую будем записывать ответы пользователя
        questionPool = Question.initQuestion(listNumberTask.size());

        countTaskWithOutAnswer = listNumberTask.size();

        //текущим номером устанавливаем 1-ое задание, которое пришло
        currentTask = listNumberTask.get(0);

        //NORTH
        northPanel = new JPanel();
        countTask = new JLabel();
        numberPanel = new JPanel();
        numberTask = new JButton();
        scrollPane = new JScrollPane(numberPanel);
        northPanel.setLayout(sprNorthPanel);

        northPanel.add(countTask);
        northPanel.add(scrollPane);

        sprNorthPanel.putConstraint(SpringLayout.NORTH, countTask, 5, SpringLayout.NORTH, this);
        sprNorthPanel.putConstraint(SpringLayout.WEST, countTask, 5, SpringLayout.WEST, this);
        sprNorthPanel.putConstraint(SpringLayout.NORTH, scrollPane, 17, SpringLayout.NORTH, countTask);
        sprNorthPanel.putConstraint(SpringLayout.WEST, scrollPane, 3, SpringLayout.WEST, this);

        //отрисовываем кнопки для отображения текущей задачи
        for (int i=1; i<listNumberTask.size()+1; i++) {
            numberTask = new JButton(i+"/"+listNumberTask.get(i-1));
            numberTask.setName(i+"");
            numberTask.setPreferredSize(new Dimension(60, 26)); /*new Dimension(51, 26)*/
            numberTask.setBackground(new Color(198, 198, 198));
            numberTask.setBorder(BorderFactory.createLineBorder(new Color(130, 130, 130), 1));
            numberPanel.add(numberTask);
            numberTask.addMouseListener(new MyNumberButtonMouse());
            listNumberButtons.add(numberTask);
        }

        //CENTRE
        centrePanel = new JPanel();
        centreScrollPane = new JScrollPane(centrePanel);
        panelWithTask = new DrawPanel();
//        panelWithTask.setLayout(new BoxLayout(panelWithTask, BoxLayout.Y_AXIS));

        verbalTaskArea = new JTextArea();
        verbalTaskArea.setLineWrap(true);
        verbalTaskArea.setEditable(false);
        verbalTaskArea.setBackground(new Color(238, 238, 238));
        panelWithTask.add(verbalTaskArea);

        isDone = new JButton("Готово");
        isDone.setEnabled(false);
        isDone.addMouseListener(new MyStaticButtonMouse());
        centrePanel.setLayout(sprCentrePanel);

        centrePanel.add(panelWithTask);
        centrePanel.add(isDone);

        sprCentrePanel.putConstraint(SpringLayout.NORTH, panelWithTask, 0, SpringLayout.NORTH, this);
        sprCentrePanel.putConstraint(SpringLayout.WEST, panelWithTask, 5, SpringLayout.WEST, this);
        sprCentrePanel.putConstraint(SpringLayout.NORTH, isDone, 3, SpringLayout.SOUTH, panelWithTask);
        sprCentrePanel.putConstraint(SpringLayout.WEST, isDone, 4, SpringLayout.WEST, this);

        //SOUTH
        southPanel = new JPanel();
        prevTask = new JButton("<");
        prevTask.addMouseListener(new MyStaticButtonMouse());
        nextTask = new JButton(">");
        nextTask.addMouseListener(new MyStaticButtonMouse());
        finishTest = new JButton("Завершить");
        finishTest.addMouseListener(new MyStaticButtonMouse());
        southPanel.setLayout(sprSouthPanel);


        southPanel.add(prevTask);
        southPanel.add(nextTask);
        southPanel.add(finishTest);

        if (win.equals(RANDOM_TEST)) {
            this.setSize(600, 500);
            //NORTH
            northPanel.setPreferredSize(new Dimension(480, 70));
            scrollPane.setPreferredSize(new Dimension(585, 40));

            //CENTRE
            panelWithTask.setPreferredSize(new Dimension(585, 325));
//            panelWithTask.setBackground(new Color(140, 140, 140));
            //SOUTH
            southPanel.setPreferredSize(new Dimension(480, 33));
            timeLabel = new JLabel();

            southPanel.add(timeLabel);

            sprSouthPanel.putConstraint(SpringLayout.NORTH, timeLabel, 7, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, timeLabel, 15, SpringLayout.WEST, this);
            sprSouthPanel.putConstraint(SpringLayout.NORTH, prevTask, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, prevTask, 0, SpringLayout.HORIZONTAL_CENTER, this);
            sprSouthPanel.putConstraint(SpringLayout.NORTH, nextTask, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, nextTask, 30, SpringLayout.HORIZONTAL_CENTER, prevTask);
            sprSouthPanel.putConstraint(SpringLayout.NORTH, finishTest, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, finishTest, -2, SpringLayout.EAST, this);

            //        long start = System.currentTimeMillis();
            //каждую секунду
            timer=new Timer(1000, new ActionListener() {
                long start,now  = 1000*60*60;    //60 минут
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Novosibirsk");   //задаем время по Новосибирску
                SimpleDateFormat formatTime;

                @Override
                public void actionPerformed(ActionEvent e) {
//                long now = System.currentTimeMillis();
                    now -= 1000;
                    long time = (start - now) / 1000;

                    formatTime = new SimpleDateFormat("'Осталось:' HH:mm:ss");
                    timeLabel.setText(formatTime.format(now - timeZone.getOffset(start)));

                    if (time == 0) {
                        //отправить результаты на проверку на сервер
                        JOptionPane.showMessageDialog(null, "Время вышло", "Тестирование завершено", JOptionPane.ERROR_MESSAGE);
                        sendAnswerToServer();
                    }
                }
            });

            timer.start();
        }

        if (win.equals(DEMO_TEST)) {
            this.setSize(750, 500);
            //NORTH
            northPanel.setPreferredSize(new Dimension(650, 75));
//            northPanel.setBackground(new Color(140, 140, 140));
            scrollPane.setPreferredSize(new Dimension(710, 55));

            patternButton = new JButton("?");
            patternButton.setPreferredSize(new Dimension(26, 26)); /*new Dimension(51, 26)*/
            patternButton.setToolTipText("Показать подсказку");
            patternButton.setBorder(BorderFactory.createLineBorder(new Color(255, 130, 130), 2));
            northPanel.add(patternButton);
            patternButton.addMouseListener(new MyStaticButtonMouse());

            sprNorthPanel.putConstraint(SpringLayout.NORTH, patternButton, 10, SpringLayout.NORTH, scrollPane);
            sprNorthPanel.putConstraint(SpringLayout.WEST, patternButton, 3, SpringLayout.EAST, scrollPane);

            //CENTRE
            panelWithTask.setPreferredSize(new Dimension(429, 325));
            panelWithPattern = new DrawPanel();
            panelWithPattern.setPreferredSize(new Dimension(310, 310));
            panelWithPatternScroll = new JScrollPane(panelWithPattern);
            centrePanel.add(panelWithPatternScroll);
            panelWithPatternScroll.setVisible(false);

            sprCentrePanel.putConstraint(SpringLayout.NORTH, panelWithPatternScroll, 30, SpringLayout.NORTH, this);
            sprCentrePanel.putConstraint(SpringLayout.WEST, panelWithPatternScroll, 0, SpringLayout.EAST, panelWithTask);
            //SOUTH
            southPanel.setPreferredSize(new Dimension(550, 33));

            sprSouthPanel.putConstraint(SpringLayout.NORTH, prevTask, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, prevTask, 0, SpringLayout.HORIZONTAL_CENTER, this);
            sprSouthPanel.putConstraint(SpringLayout.NORTH, nextTask, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, nextTask, 30, SpringLayout.HORIZONTAL_CENTER, prevTask);
            sprSouthPanel.putConstraint(SpringLayout.NORTH, finishTest, 3, SpringLayout.NORTH, this);
            sprSouthPanel.putConstraint(SpringLayout.WEST, finishTest, -20, SpringLayout.EAST, this);

            listAnswer = new ArrayList<String>();

            for (TaskStudent s : listTasksFromServer) {
                listAnswer.add(s.getAnswer());
            }
        }

        paintNewTask(currentTask);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(centreScrollPane, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.repaint();

        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clickToFinish();
            }
        });
    }

    //для отрисовки нужного задания, номер которого (1-10) нажали
    private class MyNumberButtonMouse extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (!isDoneBool) {
                String nameButton = ((JButton) e.getSource()).getName();
                int task = listNumberTask.get(Integer.parseInt(nameButton) - 1);

                //если нужно нарисовать новое задание, а не текущее
                if (currentTask != task)
                    paintNewTask(task);
            }
        }
    }

    //поведение кнопок Очистить, Пред, След, Завершить
    private class MyStaticButtonMouse extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource().equals(isDone)) {
                if (isDoneBool ) {
                    System.out.println("Вы нажали Готово  для задачи " + currentTask);
                    //записать ответ
                    rememberStateOldTask();

                    setEnableButtonIsDone(false);

                    if (listNumberTask.indexOf(currentTask) < (listNumberTask.size()-1))
                        paintNewTask(listNumberTask.get((listNumberTask.indexOf(currentTask)+1)));

                    if (countTaskWithOutAnswer==0) {
                        System.out.println("Можно завершать");

                        finishTest.setFont(new Font(null, Font.BOLD, 13));
                    }
                    else {
                        finishTest.setFont(new Font(null, Font.BOLD, 12));
                    }

                }
            }

            if (e.getSource().equals(prevTask)) {
//                System.out.println("Вы нажали prevTask");
                if (!isDoneBool) {
                    int indexNewTask = listNumberTask.indexOf(currentTask) - 1;
                    if (indexNewTask >= 0) {
                        //если нужно нарисовать новое задание, а не текущее
                        if (currentTask != listNumberTask.get(indexNewTask))
                            paintNewTask(listNumberTask.get(indexNewTask));
                    }
                }
            }

            if (e.getSource().equals(nextTask)) {
//                System.out.println("Вы нажали nextTask");
                if (!isDoneBool) {
                    int indexNewTask = listNumberTask.indexOf(currentTask) + 1;
                    if (indexNewTask < listNumberTask.size()) {
                        //если нужно нарисовать новое задание, а не текущее
                        if (currentTask != listNumberTask.get(indexNewTask))
                            paintNewTask(listNumberTask.get(indexNewTask));
                    }
                }
            }

            if (e.getSource().equals(finishTest)) {
//                System.out.println("Вы нажали finishTest");
                if (!isFinish & !isDoneBool) {
                    clickToFinish();
                }
            }

            //показать подсказку
            if (e.getSource().equals(patternButton)) {
                if (panelWithPatternScroll.isShowing()) {
                    System.out.println("Отображена");
                    patternButton.setToolTipText("Показать подсказку");
                    panelWithPatternScroll.setVisible(false);

                }
                else {
                    System.out.println("Скрыта");
                    patternButton.setToolTipText("Скрыть подсказку");
                    panelWithPatternScroll.setVisible(true);
                }
            }
        }
    }

    private void clickToFinish() {
        Object[] options = {"Да", "Нет"};
        String worlds;
        if (countTaskWithOutAnswer != 0)
            worlds = "Вы не ответили на " + countTaskWithOutAnswer + " вопроса.\nЗавершить?";
        else
            worlds = "Завершить?";
        int rez = JOptionPane.showOptionDialog(null, worlds, "Подтверждение",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        switch (rez) {
            case JOptionPane.YES_OPTION:
                System.out.println("Отправить ответы");
                sendAnswerToServer();
                break;

            case JOptionPane.NO_OPTION:
                System.out.println("Продолжить");
                this.setVisible(true);
                break;

            case JOptionPane.CLOSED_OPTION:
                System.out.println("Закрыть");
                break;
        }
    }

    private void paintNewTask ( Integer newTask) {
        isDone.setEnabled(false);

        System.out.println("    считать все данные для задачи  " + newTask);


        numberTask = listNumberButtons.get(listNumberTask.indexOf(currentTask));
        numberTask.setBackground(new Color(198,198,198));
//        numberTask.setFocusPainted(false);

        numberTask = listNumberButtons.get(listNumberTask.indexOf(newTask));
        numberTask.setBackground(new Color(240,240,240));
//        numberTask.setFocusPainted(true);

        panelWithTask.removeAll();

        if (currentTask == 15 || currentTask == 17) {
            ((DrawPanel) panelWithTask).setCoordinates(top.getX() + top.getWidth(), top.getY() + top.getHeight() / 2, -1, -1, -1, -1);
            panelWithTask.repaint();

            if (currentTask == 17) {
                ((DrawPanel) panelWithTask).setCoordinates(topLast.getX() + topLast.getWidth(), topLast.getY() + topLast.getHeight() / 2, -1, -1, -1, -1);
                panelWithTask.repaint();
            }

            if (panelWithPattern != null) {
                panelWithPattern.removeAll();

                ((DrawPanel) panelWithPattern).setCoordinates(20 + 28, 20 + 10,-1, -1, -1, -1);
                panelWithPattern.repaint();

                if (currentTask == 17) {
                    ((DrawPanel) panelWithPattern).setCoordinates(20 + 28, 20 + 10+40,-1, -1, -1, -1);
                    panelWithPattern.repaint();
                }
            }
        }



        //текущим ставим новое задание
        currentTask = newTask;
        showNewTask(newTask);
        countTask.setText("Вопрос №" + (listNumberTask.indexOf(currentTask)+1) + " из " +listNumberTask.size());

        setEnableNextPrev();
    }

    private void setElement_15() {
        panelWithTask.repaint();


        panelWithTask.add(verbalTaskArea);
        verbalTaskArea.setText(listVerbalTask.get(listNumberTask.indexOf(currentTask)));
        System.out.println("из БД " + listTask.get(listNumberTask.indexOf(currentTask)));

        panelWithTask.add(top);
        panelWithTask.add(stackPanel);


        spr15.putConstraint(SpringLayout.NORTH, verbalTaskArea, 0, SpringLayout.NORTH, panelWithTask);
        spr15.putConstraint(SpringLayout.WEST, verbalTaskArea, 0, SpringLayout.WEST, panelWithTask);
        spr15.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.SOUTH, verbalTaskArea);
        spr15.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, panelWithTask);
        spr15.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
        spr15.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);

        if (currentTask == 17) {
            panelWithTask.add(topLast);
            spr15.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
            spr15.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, panelWithTask);
        }
    }

    //                String s = "[1212, we, 12efg, 12fgegdfxh, 454645]";
    //на вход - строка из БД и список, в который надо поместить элементы
    private void stringToArray (String s, ArrayList<String> list) {
        int i =0;
        while (i < s.length()){
            Character ch = s.charAt(i);
            if(ch != '[' &  ch != ','  & ch != ' '  &  ch != ']' ) {
                try {
                    list.add(s.substring(i, s.indexOf(",", i)));
                    i=s.indexOf(",", i)-1;
                }
                catch(Exception ex) {
                    list.add(s.substring(i, s.indexOf("]")));
                    i+=s.indexOf("]")-1;
                }
            }
            i++;
        }
    }

    //                String s = "[1212, we, 12efg, 12fgegdfxh, 454645]";
    //на вход - строка из БД и список, в который надо поместить элементы
    private void integerToArray (String s, ArrayList<Integer> list) {
        int i =0;
        while (i < s.length()){
            Character ch = s.charAt(i);
            if(ch != '[' &  ch != ','  & ch != ' '  &  ch != ']' ) {
                try
                {
                    list.add(Integer.parseInt(s.substring(i, s.indexOf(",", i))));
                    i=s.indexOf(",", i)-1;
                }
                catch(Exception ex)
                {
                    list.add(Integer.parseInt(s.substring(i, s.indexOf("]"))));
                    i+=s.indexOf("]")-1;
                }
            }
            i++;
        }
    }

    private void rememberStateOldTask() {
        ArrayList listA = new ArrayList<>();

        //если номер текущей задачи от 1 до 4
        if (currentTask >= 1 & currentTask <= 4) {
            if (jCheckBoxes_1_4 != null) {
                for (JCheckBox checkBox : jCheckBoxes_1_4) {
                    if (checkBox.isSelected()) {
                        listA.add(listTask_1_14.indexOf(listTask_1_14.get(jCheckBoxes_1_4.indexOf(checkBox))));
                    }
                }
                System.out.println(listA.toString()+"   " + (listNumberTask.indexOf(currentTask)+1) + "  " + listNumberTask.size());
            }
        }

        if (currentTask >= 5 & currentTask <= 14) {
            if (jRadioButtons_5_14 != null) {
                for (JRadioButton radioButton : jRadioButtons_5_14) {
                    if (radioButton.isSelected()) {
                        listA.add(listTask_1_14.indexOf(listTask_1_14.get(jRadioButtons_5_14.indexOf(radioButton))));
                    }
                }
                System.out.println(listA.toString()+"   " + (listNumberTask.indexOf(currentTask)+1) + "  " + listNumberTask.size());
            }
        }

        if (currentTask == 15 || currentTask == 17) {
            if (textFields != null) {
                for (JTextField jTextField : textFields) {
                    listA.add(jTextField.getText().trim());
                }
            }
            listA.add(indexTop);

            if (currentTask == 17) {
                listA.add(indexTopLast);
            }

            System.out.println(listA.toString()+"   " + (listNumberTask.indexOf(currentTask)+1) + "  " + listNumberTask.size());
        }

        //если ответ не пустой, то помечаем как задание, на кот получен ответ. заносим ответ в questionPool
        if (listA.size() != 0) {
            if (currentTask >= 1 & currentTask <= 14) {
                if (questionPool.getIntegerAnswerOnQuestion(listNumberTask.indexOf(currentTask)).size() == 0) {
                    numberTask = listNumberButtons.get(listNumberTask.indexOf(currentTask));
                    numberTask.setBorder(BorderFactory.createLineBorder(new Color(69, 255, 53), 2));

                    countTaskWithOutAnswer--;

                    System.out.println("Что-то вместо пустоты");
                } else
                    System.out.println("Что-то вместо чего-то");
            }
            else if (currentTask == 15 || currentTask == 17) {
                if (questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).size() == 0) {
                    numberTask = listNumberButtons.get(listNumberTask.indexOf(currentTask));
                    numberTask.setBorder(BorderFactory.createLineBorder(new Color(69, 255, 53), 2));

                    countTaskWithOutAnswer--;

                    System.out.println("Что-то вместо пустоты");
                } else
                    System.out.println("Что-то вместо чего-то");
            }
        }

        else {
            if (currentTask >= 1 & currentTask <= 14) {
                if (questionPool.getIntegerAnswerOnQuestion(listNumberTask.indexOf(currentTask)).size() != 0) {
                    numberTask = listNumberButtons.get(listNumberTask.indexOf(currentTask));
                    numberTask.setBorder(BorderFactory.createLineBorder(new Color(130, 130, 130), 1));

                    countTaskWithOutAnswer++;

                    System.out.println("Пустоту вместо чего-то" + questionPool.getIntegerAnswerOnQuestion(listNumberTask.indexOf(currentTask)).toString());
                }
            }

            if (currentTask == 15 || currentTask == 17) {
                if (questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).size() != 0) {
                    numberTask = listNumberButtons.get(listNumberTask.indexOf(currentTask));
                    numberTask.setBorder(BorderFactory.createLineBorder(new Color(130, 130, 130), 1));

                    countTaskWithOutAnswer++;

                    System.out.println("Пустоту вместо чего-то" + questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).toString());

                }
            }
        }
        questionPool.setAnswerOnQuestion(listNumberTask.indexOf(currentTask), listA);

    }

    private void showNewTask(Integer newTask) {

        //нужно отобразить задачу с выбором ответа
        if (newTask >= 1 & newTask <=14) {
            //если уже дали ответ на этот вопрос
            ArrayList<Integer> listLastAnswer = questionPool.getIntegerAnswerOnQuestion(listNumberTask.indexOf(newTask));


            if (panelWithPattern != null) {
                panelWithPattern.removeAll();
                listAnswer_1_14 = new ArrayList<Integer>();
                integerToArray(listAnswer.get(listNumberTask.indexOf(newTask)), listAnswer_1_14);
            }

            listTask_1_14 = new ArrayList<String>();  //создаем список для ответов
            jLabels_1_4 = new ArrayList<JLabel>();

            //        разбираю строку из БД в список String
            stringToArray(listTask.get(listNumberTask.indexOf(newTask)), listTask_1_14);

            panelWithTask.setLayout(new GridLayout(listTask_1_14.size() + 1, 1));
            panelWithTask.add(verbalTaskArea);
            verbalTaskArea.setText(listVerbalTask.get(listNumberTask.indexOf(newTask)));

            if (panelWithPattern != null) {
//                verbalTaskArea = new JTextArea();
//                verbalTaskArea.setLineWrap(true);
//                verbalTaskArea.setEditable(false);
//                verbalTaskArea.setBackground(new Color(238, 238, 238));
                panelWithPattern.setLayout(new GridLayout(listTask_1_14.size() + 1, 1));
//            panelWithPattern.add(verbalTaskArea);
//            verbalTaskArea.setText(listVerbalTask.get(listNumberTask.indexOf(newTask)));
            }

            if (newTask >= 1 & newTask <= 4) {

                jCheckBoxes_1_4 = new ArrayList<JCheckBox>(); //список для JCheckBox

                setElement_1_14_ForPanel(listTask_1_14, listLastAnswer, panelWithTask);

                if (panelWithPattern != null) {
                    setElement_1_14_ForPanel(listTask_1_14, listLastAnswer, panelWithPattern);
                }
            }

            if (newTask >= 5 & newTask <= 14) {
                groupRadio = new ButtonGroup();
                jRadioButtons_5_14 = new ArrayList<JRadioButton>();

                setElement_1_14_ForPanel(listTask_1_14,  listLastAnswer, panelWithTask);

                if (panelWithPattern != null) {
                    setElement_1_14_ForPanel(listTask_1_14, listLastAnswer, panelWithPattern);
                }
            }

            panelWithTask.updateUI();

            if (panelWithPattern != null)
                panelWithPattern.updateUI();
        }

        //нужно отобразить задачу со схемой стека на азе массива
        else if (newTask >= 15 || newTask <= 17) {
            //если уже дали ответ на этот вопрос
            ArrayList<String> listLastAnswer = questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(newTask));
            textFields.clear();

            if (panelWithPattern != null) {
                panelWithPattern.removeAll();

                listAnswer_15_17 = new ArrayList<String>();
                //        разбираю строку из БД в список String
                stringToArray(listAnswer.get(listNumberTask.indexOf(newTask)), listAnswer_15_17);
                System.out.println(listAnswer_15_17.toString());
            }

            panelWithTask.setLayout(spr15);
            panelWithTask.add(verbalTaskArea);
            verbalTaskArea.setText(listVerbalTask.get(listNumberTask.indexOf(newTask)));
            System.out.println("из БД "+listTask.get(listNumberTask.indexOf(newTask)));
//            verbalTaskArea.setPreferredSize(new Dimension(panelWithTask.getWidth()-20, 50));

            spr15.putConstraint(SpringLayout.NORTH, verbalTaskArea, 0, SpringLayout.NORTH, this);
            spr15.putConstraint(SpringLayout.WEST, verbalTaskArea, 0, SpringLayout.WEST, this);


            if (panelWithPattern != null) {
                panelWithPattern.setLayout(spr15_2);

            }

            if (newTask == 15 || newTask == 17) {
                ((DrawPanel) panelWithTask).setNumberTask(newTask);


                if (panelWithPattern != null) {
                    ((DrawPanel) panelWithPattern).setNumberTask(newTask);

                    setElement_15_18_ForPanel(Integer.parseInt(listTask.get(listNumberTask.indexOf(newTask))), listLastAnswer, panelWithPattern);
                }


                setElement_15_18_ForPanel(Integer.parseInt(listTask.get(listNumberTask.indexOf(newTask))), listLastAnswer, panelWithTask);
            }

            panelWithTask.repaint();
            if (stackPanel != null)
                stackPanel.repaint();

            if (panelWithPattern != null) {
                panelWithPattern.repaint();
                panelWithPatternScroll.revalidate();
            }

        }
    }

    private void setElement_15_18_ForPanel (int sizeArray, ArrayList<String> listLastAnswer, JPanel panel) {
        System.out.println("Зашли в setElement_15_18_ForPanel " + sizeArray);

        top = new JLabel("Top");
        top.setFont(StartWindow.myFont);
        panel.add(top);


        stackPanel = new JPanel();
        stackPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
        stackPanel.setPreferredSize(new Dimension(45 + 40 * (sizeArray - 1), 28));
        panel.add(stackPanel);

        //добавляем на панель нужное количество ячеек для ввода
        for (int i = 0; i < sizeArray; i++) {
            //создаем  поля для ввода
            itemTextField = new JTextField(3);

            //можно вводить только 4 символа
            itemTextField.setDocument(new PlainDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (getLength() < 4) {
                        super.insertString(offs, str, a);
                    }
                }
            });

            itemTextField.setHorizontalAlignment(JTextField.CENTER);    //текст по центру
            itemTextField.setFont(StartWindow.myFont);

            if (panel.equals(panelWithTask)) {
                stackPanel.setLayout(sprItem);

                itemTextField.addMouseListener(new MyMouseAdapter(itemTextField));
//                itemTextField.addActionListener(new IsDoneSetEnable());

                itemTextField.setText("//////");

                //если уже отображали, то фиксируем прошлый ответ
                if (listLastAnswer.size() != 0) {
                    itemTextField.setText(listLastAnswer.get(i));
                }


                sprItem.putConstraint(SpringLayout.NORTH, itemTextField, 2, SpringLayout.NORTH, stackPanel);

                if (textFields.size() == 0) {
                    sprItem.putConstraint(SpringLayout.WEST, itemTextField, 2, SpringLayout.WEST, stackPanel);
                } else {
                    sprItem.putConstraint(SpringLayout.WEST, itemTextField, 0, SpringLayout.EAST, textFields.get(textFields.size() - 1));
                }


                textFields.add(itemTextField);
            }

            else if (panel.equals(panelWithPattern)) {
                stackPanel.setLayout(sprItem_2);

//                itemTextField.setEnabled(false);
                itemTextField.setEditable(false);
                itemTextField.setText(listAnswer_15_17.get(i));

                sprItem_2.putConstraint(SpringLayout.NORTH, itemTextField, 2, SpringLayout.NORTH, stackPanel);

                if (i == 0) {
                    sprItem_2.putConstraint(SpringLayout.WEST, itemTextField, 2, SpringLayout.WEST, stackPanel);
                } else {
                    sprItem_2.putConstraint(SpringLayout.WEST, itemTextField, 2+40*i, SpringLayout.WEST, stackPanel);
                }
            }

            stackPanel.add(itemTextField);
        }


        if (panel.equals(panelWithTask)) {

            spr15.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.SOUTH, verbalTaskArea);
            spr15.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, panel);

            spr15.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
            spr15.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);


            if (currentTask == 15 || currentTask == 17) {

                top.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        setEnableButtonIsDone(true);


                        System.out.println(panelWithTask.getComponentZOrder(panelWithTask.getComponentAt(e.getX() + 20, e.getY() + 20 + verbalTaskArea.getHeight())) + " " + (e.getX() + 20) + " " + (e.getY() + 20 + verbalTaskArea.getHeight()));

                        //если отпустили кнопку над панелькой с текстовыми полями
                        if (panelWithTask.getComponentZOrder(panelWithTask.getComponentAt(e.getX() + 20, e.getY() + 20 + verbalTaskArea.getHeight())) == 2) {


                            int indexField = stackPanel.getComponentZOrder(panelWithTask.getComponentAt(e.getX() + 20, e.getY() + 20 + verbalTaskArea.getHeight()).getComponentAt(e.getX() - 30, e.getY() - 25));

                            //если отпустили кнопку над текстовым полем
                            if (indexField != -1) {
                                int x1 = top.getX() + top.getWidth();
                                int y1 = top.getY() + top.getHeight() / 2;
                                int x2 = (x1 + 10) + textFields.get(indexField).getWidth() * (indexField + 1) - (textFields.get(indexField).getWidth() / 2) - 2;
                                int y3 = top.getY() + top.getHeight() + 9;


                                if (currentTask==15) {
                                    massTopCoordinates_15[0] = x1;
                                    massTopCoordinates_15[1] = y1;
                                    massTopCoordinates_15[2] = x2;
                                    massTopCoordinates_15[3] = y3;
                                }

                                if (currentTask==17) {
                                    massTopCoordinates_17[0] = x1;
                                    massTopCoordinates_17[1] = y1;
                                    massTopCoordinates_17[2] = x2;
                                    massTopCoordinates_17[3] = y3;
                                }


                                panelWithTask.removeAll();
                                ((DrawPanel) panelWithTask).setCoordinates(x1, y1, x2, y1, x2, y3);

                                setElement_15();

                                indexTop = indexField;
                            }
                        } else {
                            panelWithTask.removeAll();
                            ((DrawPanel) panelWithTask).setCoordinates(top.getX() + top.getWidth(), top.getY() + top.getHeight() / 2, -1, -1, -1, -1);

                            if (currentTask==15) {
                                massTopCoordinates_15 = new Integer[4];
                            }

                            if (currentTask==17) {
                                massTopCoordinates_17 = new Integer[4];

                            }

                            indexTop = -1;

                            setElement_15();
                        }
                    }
                });

                panelWithTask.revalidate();
            }

            if (currentTask == 15) {
                //рисуем линию, которую проводил пользователь
                if (listLastAnswer.size() != 0) {
                    if (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray)) != -1) {

                        if (massTopCoordinates_17[0] != null) {

                            int x1 = massTopCoordinates_15[0] /*45*/;
                            int y1 = massTopCoordinates_15[1];
//                        int x2 = x1 + 37 * (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray)) + 1);
//                        int y3 = y1 + 18;

                            int x2 = massTopCoordinates_15[2];
                            int y3 = massTopCoordinates_15[3];

                            ((DrawPanel) panelWithTask).setCoordinates(x1, y1, x2, y1, x2, y3);
                            panelWithTask.repaint();

                            System.out.println(x1 + " " + y1);
                        }
                    }
                }

            }

            if (currentTask == 17) {
                if (listLastAnswer.size() != 0) {
                    if (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray)) != -1) {

                        if (massTopCoordinates_17[0] != null) {

                            int x1 = massTopCoordinates_17[0] /*= 48*/;
                            int y1 = massTopCoordinates_17[1]/* = 61*/;
                            int x2 = massTopCoordinates_17[2] /*= x1 + 37 * (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray)) + 1)*/;
                            int y3 = massTopCoordinates_17[3] /*= y1 + 18*/;

                            ((DrawPanel) panelWithTask).setCoordinates(x1, y1, x2, y1, x2, y3);
                            System.out.println(x1 + " " + y1);
                        }
                    }

                    // отрисовываем вторую линию
                    if (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray+1)) != -1) {

                        if (massTopLastCoordinates_17[0] != null) {

                            int x1 = massTopLastCoordinates_17[0] /*= 45*/;
                            int y1 = massTopLastCoordinates_17[1]/* = 128*/;
                            int x2 = massTopLastCoordinates_17[2] /*= x1 + 37 * (Integer.parseInt(questionPool.getStringAnswerOnQuestion(listNumberTask.indexOf(currentTask)).get(sizeArray+1)) + 1)*/;
                            int y3 = massTopLastCoordinates_17[3] /*= y1 - 20*/;


                            ((DrawPanel) panelWithTask).setCoordinates(x1, y1, x2, y1, x2, y3);

                            System.out.println(x1 + " " + y1);
                        }
                    }
                    panelWithTask.repaint();
                }

                top.setText("first");

                topLast = new JLabel("last");
                topLast.setFont(StartWindow.myFont);
                panel.add(topLast);

                topLast.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        setEnableButtonIsDone(true);


                        System.out.println(panelWithTask.getComponentZOrder(panelWithTask.getComponentAt(e.getX() + 20, e.getY() + 20 + verbalTaskArea.getHeight() +top.getHeight() + 10 + stackPanel.getHeight() + 10)) + " " + (e.getX() + 20) + " " + (e.getY() + 20 + verbalTaskArea.getHeight() +top.getHeight() + 10 + stackPanel.getHeight() + 10));

                        //если отпустили кнопку над панелькой с текстовыми полями
                        if (panelWithTask.getComponentZOrder(panelWithTask.getComponentAt(e.getX()+20, e.getY() + 20 + verbalTaskArea.getHeight() +top.getHeight() + 10 + stackPanel.getHeight() + 10)) == 2) {

                            int indexField = stackPanel.getComponentZOrder(panelWithTask.getComponentAt(e.getX() + 20, e.getY() + 20 + verbalTaskArea.getHeight() +top.getHeight() + 10 + stackPanel.getHeight() + 10).getComponentAt(e.getX()-30, e.getY()+30+3));
                            System.out.println(indexField);

                            //если отпустили кнопку над текстовым полем
                            if (indexField != -1) {
                                int x1 = topLast.getX() + topLast.getWidth();
                                int y1 = topLast.getY() + topLast.getHeight() / 2;
                                int x2 = (x1 + 10) + textFields.get(indexField).getWidth()*(indexField+1) -  (textFields.get(indexField).getWidth()/2)-2;
                                int y3 = topLast.getY() - topLast.getHeight()/2;


                                if (currentTask == 17) {
                                    massTopLastCoordinates_17[0] = x1;
                                    massTopLastCoordinates_17[1] = y1;
                                    massTopLastCoordinates_17[2] = x2;
                                    massTopLastCoordinates_17[3] = y3;
                                }


                                panelWithTask.removeAll();
                                ((DrawPanel) panelWithTask).setCoordinates(x1, y1, x2, y1, x2, y3);
                                panelWithTask.repaint();

                                setElement_15();

//                                panelWithTask.add(verbalTaskArea);
//                                verbalTaskArea.setText(listVerbalTask.get(listNumberTask.indexOf(currentTask)));
//                                System.out.println("из БД " + listTask.get(listNumberTask.indexOf(currentTask)));
//
//                                panelWithTask.add(top);
//                                panelWithTask.add(stackPanel);
//
//
//                                spr15.putConstraint(SpringLayout.NORTH, verbalTaskArea, 0, SpringLayout.NORTH, panelWithTask);
//                                spr15.putConstraint(SpringLayout.WEST, verbalTaskArea, 0, SpringLayout.WEST, panelWithTask);
//                                spr15.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.SOUTH, verbalTaskArea);
//                                spr15.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, panelWithTask);
//                                spr15.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
//                                spr15.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);
//
//                                if (currentTask == 17) {
//                                    panelWithTask.add(topLast);
//                                    spr15.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
//                                    spr15.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, panelWithTask);
//                                }

                                indexTopLast = indexField;
                            }
                        }
                        else {
                            panelWithTask.removeAll();
                            ((DrawPanel)panelWithTask).setCoordinates(topLast.getX() + topLast.getWidth(), topLast.getY() + topLast.getHeight() / 2, -1, -1, -1, -1);


                            if (currentTask==17) {
                                massTopLastCoordinates_17 = new Integer[4];

                            }

                            indexTopLast = -1;

                            setElement_15();
                        }
                    }
                });

                spr15.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
                spr15.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, panelWithTask);
            }

        }


        else if (panel.equals(panelWithPattern)) {

            spr15_2.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.NORTH, panel);
            spr15_2.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, panel);

            spr15_2.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
            spr15_2.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);


            //отобразить правильно линию
            if (currentTask == 15 || currentTask == 17) {
                ((DrawPanel) panel).setCoordinates(20 + 28, 20 + 10,
                        32 + 40*(Integer.parseInt(listAnswer_15_17.get(Integer.parseInt(listTask.get(listNumberTask.indexOf(currentTask)))))+1), 20 + 10,
                        32 + 40*(Integer.parseInt(listAnswer_15_17.get(Integer.parseInt(listTask.get(listNumberTask.indexOf(currentTask)))))+1), 20 + 10 + 18);


                if (currentTask == 17) {
                    top.setText("first");

                    topLast = new JLabel("last");
                    topLast.setFont(StartWindow.myFont);
                    panel.add(topLast);

                    spr15_2.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
                    spr15_2.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, panel);

                    ((DrawPanel) panel).setCoordinates(20 + 28, 20 + 10 +40+25,
                            32 + 40*(Integer.parseInt(listAnswer_15_17.get(Integer.parseInt(listTask.get(listNumberTask.indexOf(currentTask)))+1))+1), 20 + 10+40+25,
                            32 + 40*(Integer.parseInt(listAnswer_15_17.get(Integer.parseInt(listTask.get(listNumberTask.indexOf(currentTask)))+1))+1), 20 + 10 + 18 +30);
                }

                panel.repaint();
            }
        }
    }


    private void setElement_1_14_ForPanel(ArrayList<String> list, ArrayList<Integer> listLastAnswer, JPanel panel) {

        for (int i=0; i<list.size(); i++) {
            panel_1_14 = new JPanel();
            panel_1_14.setLayout(sprPanelWithTask);
//                panel_1_14.setBackground(new Color(generateNumberTask(10, 255), generateNumberTask(10, 255), generateNumberTask(10, 255)));
            label_1_14 = new JLabel(listTask_1_14.get(i));
            jLabels_1_4.add(label_1_14);

            if (currentTask >= 1 & currentTask <= 4) {
                checkBox_1_4 = new JCheckBox();
                checkBox_1_4.addActionListener(new IsDoneSetEnable());
                if (isFinish)
                    checkBox_1_4.setEnabled(false);

                if (panel.equals(panelWithTask)) {
                    jCheckBoxes_1_4.add(checkBox_1_4);

                    //если этот ответ был выбран, восстанавливаем
                    if (listLastAnswer.indexOf(i) != -1) {
                        checkBox_1_4.setSelected(true);
                    }
                }

                panel_1_14.add(checkBox_1_4);
                sprPanelWithTask.putConstraint(SpringLayout.NORTH, checkBox_1_4, 5, SpringLayout.NORTH, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.WEST, checkBox_1_4, 5, SpringLayout.WEST, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.NORTH, label_1_14, 8, SpringLayout.NORTH, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.WEST, label_1_14, 5, SpringLayout.EAST, checkBox_1_4);

                //отмечаем правильные ответы
                if (panel.equals(panelWithPattern)) {
                    checkBox_1_4.setEnabled(false);
                    if (listAnswer_1_14.indexOf(i) != -1) {
                        checkBox_1_4.setSelected(true);
                    }
                }
            }

            if (currentTask >= 5 & currentTask <= 14) {
                radioButton_5_14 = new JRadioButton();
                radioButton_5_14.addActionListener(new IsDoneSetEnable());

                if (isFinish)
                    radioButton_5_14.setEnabled(false);

                if (panel.equals(panelWithTask)) {
                    jRadioButtons_5_14.add(radioButton_5_14);

                    if (listLastAnswer.indexOf(i) != -1) {
                        radioButton_5_14.setSelected(true);
                    }
                }
                groupRadio.add(radioButton_5_14);
                panel_1_14.add(radioButton_5_14);
                sprPanelWithTask.putConstraint(SpringLayout.NORTH, radioButton_5_14, 5, SpringLayout.NORTH, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.WEST, radioButton_5_14, 5, SpringLayout.WEST, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.NORTH, label_1_14, 8, SpringLayout.NORTH, panel_1_14);
                sprPanelWithTask.putConstraint(SpringLayout.WEST, label_1_14, 5, SpringLayout.EAST, radioButton_5_14);

                if (panel.equals(panelWithPattern)) {
                    groupRadio = new ButtonGroup();
                    groupRadio.add(radioButton_5_14);
                    radioButton_5_14.setEnabled(false);
                    if (listAnswer_1_14.indexOf(i) != -1) {
                        radioButton_5_14.setSelected(true);
                    }
                }
            }

            panel_1_14.add(label_1_14);
            panel.add(panel_1_14);
        }
//        if (panel.equals(panelWithTask))
//            System.out.println("panelWithTask");
//
//        if (panel.equals(panelWithPattern))
//            System.out.println("panelWithPattern");
    }

    private class MyMouseAdapter extends MouseAdapter {
        private JTextField field;
        private int modif;

        MyMouseAdapter(JTextField p) {
            this.field = p;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            modif = e.getModifiers();
            if (modif==e.BUTTON1_MASK) {

                //если правой кнопкой мыши щелкнули по полю в стеке
                if (currentTask == 15 || currentTask == 17) {
//                    System.out.println("mouseReleased 15 || 17");

                    setEnableButtonIsDone(true);

//                    //индекс элемента field в панели stackPanel
                    int indexField = textFields.indexOf(field);
                    itemTextField = textFields.get(indexField);

                    if (itemTextField.getText().equals("//////")) {
                        itemTextField.setText("");
                    }
                }
                if (currentTask == 16) {
//                    System.out.println("mouseReleased 16");

                    //индекс элемента field в панели stackPanel
//                    int indexField = panelWithTask.getComponentZOrder(field) - 2;
//                    itemTextField = textFields.get(indexField);
//
//                    if (itemTextField.getText().equals("//////")) {
//                        itemTextField.setText("");
//                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            modif = e.getModifiers();
            String mykey = "";
            if (modif == e.BUTTON1_MASK) {

                if (field != null) {
                    if (currentTask == 15 || currentTask == 17) {
//                        System.out.println("mousePressed 15 || 17");

                        int indexField = textFields.indexOf(field);
                        itemTextField = textFields.get(indexField);
                    }

                    if (currentTask == 16) {
                        System.out.println("mousePressed 16");

//                        int indexField = panelWithTask.getComponentZOrder(field) - 2;
//                        itemTextField = textFields.get(indexField);
                    }
                }
                mykey = " ЛЕВОЙ ";
            }
            System.out.println("Нажатие " + mykey + "клавиши мыши");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (field != null) {
//                System.out.println("Exited item");

                if (currentTask == 15 || currentTask == 17) {
//                    System.out.println("mouseExited 15 || 17");

                    int indexField = textFields.indexOf(field);
                    System.out.println(indexField + " indexField");
                    itemTextField = textFields.get(indexField);
                }

                if (currentTask == 16) {
                    System.out.println("mouseExited 16");

//                    int indexField = panelWithTask.getComponentZOrder(field)-2;
//                    itemTextField = textFields.get(indexField);
                }

                if (itemTextField != null) {
                    if (itemTextField.getText().equals("")) {
                        itemTextField.setText("//////");
                    }
                }
            }
        }
    }

    private class IsDoneSetEnable implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setEnableButtonIsDone(true);
        }
    }

    private void setEnableButtonIsDone (Boolean status) {
        isDoneBool = status;
        isDone.setEnabled(status);
        finishTest.setEnabled(!status);

        if (status) {
            nextTask.setEnabled(false);
            prevTask.setEnabled(false);
        }
        else {
            setEnableNextPrev();
        }

        for (int i = 0; i < listNumberButtons.size(); i++) {
            numberTask = listNumberButtons.get(i);
            numberTask.setEnabled(!status);
        }
    }

    private void setEnableNextPrev () {
        if (listNumberTask.indexOf(currentTask) == 0) {
            prevTask.setEnabled(false);
            nextTask.setEnabled(true);
        }

        else if (listNumberTask.indexOf(currentTask) == (listNumberTask.size()-1)) {
            prevTask.setEnabled(true);
            nextTask.setEnabled(false);
        }

        else {
            prevTask.setEnabled(true);
            nextTask.setEnabled(true);
        }
    }

    private void sendAnswerToServer() {
        finishTest.setEnabled(false);
        isFinish = true;

        sendData(new MessageStudent(SEND_REZULT, questionPool));

        for (int i=0; i<questionPool.getSize(); i++) {
            try {
                System.out.println(questionPool.getIntegerAnswerOnQuestion(i).toString());
            } catch (NumberFormatException ex) {
                System.out.println(questionPool.getStringAnswerOnQuestion(i).toString());

            }
        }



    }
    private int generateNumberTask(int min, int max) {
        // Инициализируем генератор
        Random rnd = new Random(System.nanoTime());
        // Получаем случайное число в диапазоне от min до max (включительно)
        int number = min + rnd.nextInt(max - min + 1);
        return number;
    }
}