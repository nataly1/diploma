package ru.natasha.window;

import ru.natasha.message.MessageProfessor;
import ru.natasha.task.Tasks;
import ru.natasha.window.panels.DrawPanel;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static ru.natasha.connect.ConnectWithServer.ADD_TASK;
import static ru.natasha.connect.ConnectWithServer.sendData;

/**
 * Created by Наташа on 22.03.2015.
 */
public class AddTaskWindow_15_18 extends JFrame {
    private AddTaskWindow_15_18 myWindow_15_18 = null;

    private JTextArea textTask;    //поле, в котором отображается задание
    private JScrollPane scrollTextAreaTask;
    private JButton addStack;
    private JButton deleteStack;
    private JPanel northPanel;

    private JPanel centrePanel;
    private JScrollPane scrollPane;
    private JLabel top;
    private JPanel stackPanel;
    private JTextField itemTextField;
    private JLabel topLast;
    private JButton addOk;
    private int numberTask;

    private ArrayList<JTextField> textFields = new ArrayList<JTextField>();

    private int indexTop = -1;
    private int indexTopLast = -1;
    private ArrayList<Integer[]> listIndexArrows = new ArrayList<>();

    //для отправки задания на сервер
    int countElement;     //количество элементов
    ArrayList<String> listAnswer;  //правильный ответ

    SpringLayout spr = new SpringLayout();
    SpringLayout spr2 = new SpringLayout();
    SpringLayout sprItem = new SpringLayout();

    public AddTaskWindow_15_18(Integer numberTask, String text) {
        super("Добавить набор ответов для задачи " + numberTask);
        this.numberTask = numberTask;

        //NORTH
        northPanel = new JPanel();
        northPanel.setLayout(spr);
        northPanel.setPreferredSize(new Dimension(400, 120));
        textTask = new JTextArea(5, 35);
        textTask.setText(text);
        textTask.setLineWrap(true);
        textTask.setEditable(true);
        textTask.addMouseListener(new ItemTextFieldEnableMouseAdapter());
        scrollTextAreaTask = new JScrollPane(textTask);
        northPanel.add(scrollTextAreaTask);
//        northPanel.setBackground(new Color(120,10,10));

        addStack = new JButton("Добавить элемент");
        addStack.addActionListener(new MyListener());
        northPanel.add(addStack);

        deleteStack = new JButton("Удалить элемент");
        deleteStack.addActionListener(new MyListener());
        deleteStack.setEnabled(false);
        northPanel.add(deleteStack);

        spr.putConstraint(SpringLayout.NORTH, scrollTextAreaTask, 3, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, scrollTextAreaTask, 3, SpringLayout.WEST, this);
        spr.putConstraint(SpringLayout.NORTH, addStack, 3, SpringLayout.SOUTH, scrollTextAreaTask);
        spr.putConstraint(SpringLayout.WEST, addStack, 4, SpringLayout.WEST, this);
        spr.putConstraint(SpringLayout.NORTH, deleteStack, 3, SpringLayout.SOUTH, scrollTextAreaTask);
        spr.putConstraint(SpringLayout.EAST, deleteStack, -10, SpringLayout.EAST, this);

        //CENTRE
        centrePanel = new DrawPanel(numberTask);
        centrePanel.setLayout(spr2);
        scrollPane = new JScrollPane(centrePanel);

        top = new JLabel("Top");
        top.setFont(MainWindow.myFont);
        top.setVisible(false);
        centrePanel.add(top);

        spr2.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.NORTH, scrollPane);
        spr2.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, scrollPane);


        if (numberTask == 15 || numberTask == 17) {
            top.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    System.out.println(centrePanel.getComponentAt(e.getX()+20, e.getY()+20).getWidth() + " X "+ (e.getX()-30)+ " "+
                            centrePanel.getComponentAt(e.getX()+20, e.getY()+20).getHeight() + " Y " + (e.getY()-25));

                    System.out.println(centrePanel.getComponentZOrder(centrePanel.getComponentAt(e.getX()+20, e.getY()+20)));

                    //если отпустили кнопку над панелькой с текстовыми полями
                    if (centrePanel.getComponentZOrder(centrePanel.getComponentAt(e.getX()+20, e.getY()+20)) == 1) {

/*                      int indexField = stackPanel.getComponentZOrder(centrePanel.getComponentAt(e.getX() + 20, e.getY() + 20).getComponentAt(e.getX()-30, e.getY()-25));
                        itemTextField = textFields.get(indexField);

                        for (JTextField t : textFields) {
                            if (t.getText().equals(top.getText()))
                                t.setText("//////");
                        }
                        itemTextField.setText(top.getText());
                        */
                        int indexField = stackPanel.getComponentZOrder(centrePanel.getComponentAt(e.getX() + 20, e.getY() + 20).getComponentAt(e.getX()-30, e.getY()-25));

                        //если отпустили кнопку над текстовым полем
                        if (indexField != -1) {
                            int x1 = top.getX() + top.getWidth();
                            int y1 = top.getY() + top.getHeight() / 2;

                            int x2 = (x1 + 10) + textFields.get(indexField).getWidth()*(indexField+1) -  (textFields.get(indexField).getWidth()/2)-2;

                            int y3 = top.getY() + top.getHeight() + 10;

                            centrePanel.removeAll();
                            ((DrawPanel) centrePanel).setCoordinates(x1, y1, x2, y1, x2, y3);

                            setElement_15();

                            indexTop = indexField;
                        }
                    }
                    else {
                        centrePanel.removeAll();
                        ((DrawPanel)centrePanel).setCoordinates(top.getX() + top.getWidth(), top.getY() + top.getHeight() / 2, -1, -1, -1, -1);

                        indexTop = -1;

                        setElement_15();
                    }
//                System.out.println(centrePanel.getComponentZOrder(centrePanel.getComponentAt(e.getX()+20, e.getY()+20)));
                }
            });

            stackPanel = new JPanel();
            stackPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
            stackPanel.setLayout(sprItem);
            stackPanel.setVisible(false);
            centrePanel.revalidate();
            centrePanel.add(stackPanel);

            spr2.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
            spr2.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);
        }

        if (numberTask == 17) {
            top.setText("first");

            topLast = new JLabel("last");
            topLast.setFont(MainWindow.myFont);
            topLast.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {

                    System.out.println(centrePanel.getComponentAt(e.getX()+20, e.getY()+20+top.getHeight() + 10 + stackPanel.getHeight() + 10).getWidth() + " X "+ (e.getX()-30)+ " "+
                            centrePanel.getComponentAt(e.getX()+20,e.getY()+20+top.getHeight() + 10 + stackPanel.getHeight() + 10).getHeight() + " Y " + (e.getY()+30+3));

                    //если отпустили кнопку над панелькой с текстовыми полями
                    if (centrePanel.getComponentZOrder(centrePanel.getComponentAt(e.getX()+20, e.getY()+20+top.getHeight() + 10 + stackPanel.getHeight() + 10)) == 1) {

                        int indexField = stackPanel.getComponentZOrder(centrePanel.getComponentAt(e.getX() + 20, e.getY()+20+top.getHeight() + 10 + stackPanel.getHeight() + 10).getComponentAt(e.getX()-30, e.getY()+30+3));
                        System.out.println(indexField);

                        //если отпустили кнопку над текстовым полем
                        if (indexField != -1) {
                            int x1 = topLast.getX() + topLast.getWidth();
                            int y1 = topLast.getY() + topLast.getHeight() / 2;

                            int x2 = (x1 + 10) + textFields.get(indexField).getWidth()*(indexField+1) -  (textFields.get(indexField).getWidth()/2)-2;

                            int y3 = topLast.getY() - topLast.getHeight()/2;

                            centrePanel.removeAll();
                            ((DrawPanel) centrePanel).setCoordinates(x1, y1, x2, y1, x2, y3);

                            setElement_15();

                            indexTopLast = indexField;
                        }
                    }
                    else {
                        centrePanel.removeAll();
                        ((DrawPanel)centrePanel).setCoordinates(topLast.getX() + topLast.getWidth(), topLast.getY() + topLast.getHeight() / 2, -1, -1, -1, -1);

                        indexTopLast = -1;

                        setElement_15();
                    }
                }
            });

            topLast.setVisible(false);
            centrePanel.add(topLast);
            spr2.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
            spr2.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, scrollPane);
        }

        if (numberTask == 18) {
            top.setText("first");


            topLast = new JLabel("last");
            topLast.setFont(MainWindow.myFont);
//        top.addMouseListener(new MyMouseAdapter());
            topLast.setVisible(false);
            centrePanel.add(topLast);
            spr2.putConstraint(SpringLayout.NORTH, topLast, 90, SpringLayout.SOUTH, top);
            spr2.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, scrollPane);
        }


        //SOUTH
        addOk = new JButton("Готово");
        addOk.setToolTipText("Добавить вариант ответов в БД");
        addOk.addActionListener(new MyListener());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(addOk, BorderLayout.SOUTH);

        myWindow_15_18 = this;

        this.setPreferredSize(new Dimension(400, 400));
        this.pack();
        this.setLocation(600, 150);
        this.setVisible(true);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.myWindow.remove(myWindow_15_18);
                System.out.println("Осталось открыто " + MainWindow.myWindow.size());

                dispose();
            }
        });
    }

    private class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(addOk)) {
                System.out.println("Отправить");

                if (textTask.getText().trim().equals("")) {
                    textTask.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    JOptionPane.showMessageDialog(null, "Введите текст задания", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                }
                else if (textFields.size() == 0) {
                    JOptionPane.showMessageDialog(null, "Введите ответ", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    addTask();
                }
            }

            //добавить элемент
            if (e.getSource().equals(addStack)) {

//                System.out.println("Добавить элемент в стек");
                if (numberTask == 15 || numberTask == 17) {

                    if (textFields.size() == 0 ) {
                        stackPanel.setPreferredSize(new Dimension(45, 28));
                    }

                    if (textFields.size() == 8) {
                        if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) {
                            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                        }
                    }
                    if (textFields.size() >= 8) {
                        centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() + textFields.get(0).getWidth(), centrePanel.getHeight()-15));
                    }
                    if (textFields.size() >= 1) {
                        stackPanel.setPreferredSize(new Dimension(stackPanel.getWidth() + textFields.get(0).getWidth(), stackPanel.getHeight()));
                    }

                    //создаем  поля для ввода
                    itemTextField = createStandardItemTextField();

                    itemTextField.addMouseListener(new ItemTextFieldEnableMouseAdapter(itemTextField));
                    itemTextField.setText("//////");

                    sprItem.putConstraint(SpringLayout.NORTH, itemTextField, 2, SpringLayout.NORTH, stackPanel);

                    if (textFields.size() == 0) {
                        sprItem.putConstraint(SpringLayout.WEST, itemTextField, 2, SpringLayout.WEST, stackPanel);
                    } else {
                        sprItem.putConstraint(SpringLayout.WEST, itemTextField, 0, SpringLayout.EAST, textFields.get(textFields.size() - 1));
                    }

                    stackPanel.add(itemTextField);


                    textFields.add(itemTextField);


                    //если не было ни одного элемента
                    if (!top.isVisible()) {
                        top.setVisible(true);
                        stackPanel.setVisible(true);
                        deleteStack.setEnabled(true);

                        if (numberTask == 17) {
                            topLast.setVisible(true);
                        }
                    }

                    centrePanel.revalidate();
                }

                if (numberTask == 16 || numberTask == 18) {
                    if (numberTask == 16) {
                        if (textFields.size() == 2 * 4 + 1) {
                            if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) {
                                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                            }
                        }
                        if (textFields.size() >= 2 * 4 + 1) {
                            centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() + textFields.get(0).getWidth() + 35, centrePanel.getHeight() - 15));
                        }
                    }

                    if (numberTask == 18) {
                        if (textFields.size() == 2 * 5) {
                            if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) {
                                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                            }
                        }
                        if (textFields.size() >= 2 * 5) {
                            centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() + textFields.get(0).getWidth() + 35, centrePanel.getHeight() - 15));
                        }
                    }
                    //если не было ни одного элемента
                    if (!top.isVisible()) {
                        top.setVisible(true);
                        deleteStack.setEnabled(true);

                        //создаем  поля для ввода
                        itemTextField = createStandardItemTextField();

                        //добавляем нужные атрибуты неактивного поля
                        itemTextField = createDisableItemTextField(itemTextField);


                        spr2.putConstraint(SpringLayout.NORTH, itemTextField, 2, SpringLayout.SOUTH, top);
                        spr2.putConstraint(SpringLayout.WEST, itemTextField, -5, SpringLayout.WEST, top);

                        textFields.add(itemTextField);
                        centrePanel.add(itemTextField);

                        if (numberTask == 18) {
                            topLast.setVisible(true);

                            //создаем  поля для ввода
                            itemTextField  = createStandardItemTextField();

                            //добавляем нужные атрибуты неактивного поля
                            itemTextField = createDisableItemTextField(itemTextField);

                            spr2.putConstraint(SpringLayout.SOUTH, itemTextField, 2, SpringLayout.NORTH, topLast);
                            spr2.putConstraint(SpringLayout.WEST, itemTextField, -5, SpringLayout.WEST, topLast);

                            textFields.add(itemTextField);
                            centrePanel.add(itemTextField);
                        }
                    }


                    //создаем  поля для ввода
                    itemTextField  = createStandardItemTextField();


                    itemTextField.addMouseListener(new ItemTextFieldEnableMouseAdapter(itemTextField));
                    itemTextField.setText("//////");

                    if (numberTask == 16) {
                        spr2.putConstraint(SpringLayout.NORTH, itemTextField, -2, SpringLayout.NORTH, top);

                        if (textFields.size() == 1) {
                            spr2.putConstraint(SpringLayout.WEST, itemTextField, 35, SpringLayout.EAST, textFields.get(0));
                        } else {
                            spr2.putConstraint(SpringLayout.WEST, itemTextField, 35, SpringLayout.EAST, textFields.get(textFields.size()-1));
                        }
                    }
                    if (numberTask == 18) {
                        spr2.putConstraint(SpringLayout.NORTH, itemTextField, -2, SpringLayout.SOUTH, textFields.get(0));
                        if (textFields.size() == 2) {
                            spr2.putConstraint(SpringLayout.WEST, itemTextField, 35, SpringLayout.EAST, textFields.get(0));
                        } else {
                            spr2.putConstraint(SpringLayout.WEST, itemTextField, 35, SpringLayout.EAST, textFields.get(textFields.size()-1));
                        }
                    }


                    textFields.add(itemTextField);
                    centrePanel.add(itemTextField);


                    //создаем  поля для ввода указателей
                    itemTextField  = createStandardItemTextField();


                    //добавляем нужные атрибуты неактивного поля
                    itemTextField = createDisableItemTextField(itemTextField);

                    spr2.putConstraint(SpringLayout.NORTH, itemTextField, 0, SpringLayout.SOUTH, textFields.get(textFields.size()-1));
                    spr2.putConstraint(SpringLayout.WEST, itemTextField, 35, SpringLayout.EAST, textFields.get(textFields.size()-2));

                    textFields.add(itemTextField);
                    centrePanel.add(itemTextField);

                    centrePanel.updateUI();
                    scrollPane.revalidate();

                    //если уже 4 столбика
                    if (textFields.size() == 7)
                        addStack.setEnabled(false);
                }
            }

            if (e.getSource().equals(deleteStack)) {
//                System.out.println("Удалить элемент из стека");

                if (numberTask == 15 || numberTask == 17) {

                    //устанавливаем полосу прокрутки
                    if (textFields.size() == 9) {
                        if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
                            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight() - 15));
                        }
                    }
                    //изменяем размер центральной панели
                    if (textFields.size() >= 9) {
                        centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() - textFields.get(0).getWidth(), centrePanel.getHeight()));
                    }
                    //изменяем размер панели со стеком
                    if (textFields.size() >= 1) {
                        stackPanel.setPreferredSize(new Dimension(stackPanel.getWidth() - textFields.get(0).getWidth(), stackPanel.getHeight()));
                    }

                    //если над удаляемым элементом нарисована стрелочка
                    if ((indexTop == textFields.size()-1) || (indexTopLast == textFields.size()-1)){
                        if (indexTop == textFields.size()-1){
                            indexTop = -1;
                            centrePanel.removeAll();
                            ((DrawPanel)centrePanel).setCoordinates(top.getX() + top.getWidth(), top.getY() + top.getHeight() / 2, -1, -1, -1, -1);
                        }

                        if (indexTopLast == textFields.size()-1){
                            indexTopLast = -1;
                            centrePanel.removeAll();
                            ((DrawPanel)centrePanel).setCoordinates(topLast.getX() + topLast.getWidth(), topLast.getY() + topLast.getHeight() / 2, -1, -1, -1, -1);
                        }

                        setElement_15();
                    }
                    //удаляем последний элемент
                    textFields.remove(textFields.size() - 1);
                    stackPanel.remove(textFields.size());

                    //если не было ни одного элемента
                    if (textFields.size() == 0) {
                        top.setVisible(false);
                        stackPanel.setVisible(false);
                        deleteStack.setEnabled(false);

                        if (numberTask == 17) {
                            topLast.setVisible(false);
                        }
                    }

                    stackPanel.revalidate();
                }

                if (numberTask == 16|| numberTask == 18) {

                    if (numberTask == 16) {
                        //устанавливаем полосу прокрутки
                        if (textFields.size() == 2 * 5 + 1) {
                            if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
                                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                                scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight() - 15));
                            }
                        }
                        //изменяем размер центральной панели
                        if (textFields.size() >= 2 * 5 + 1) {
                            centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() - textFields.get(0).getWidth() - 35, centrePanel.getHeight()));
                        }
                    }
                    if (numberTask == 18) {
                        //устанавливаем полосу прокрутки
                        if (textFields.size() == 2 * 6) {
                            if (scrollPane.getHorizontalScrollBarPolicy() != ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
                                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                                scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight() - 15));
                            }
                        }
                        //изменяем размер центральной панели
                        if (textFields.size() >= 2 * 6) {
                            centrePanel.setPreferredSize(new Dimension(centrePanel.getWidth() - textFields.get(0).getWidth() - 35, centrePanel.getHeight()));
                        }
                    }

                    //удаляем два последних элемента
                    int x1 = centrePanel.getComponent(textFields.size()).getX();
                    int x2 = centrePanel.getComponent(textFields.size()).getX() + textFields.get(0).getWidth();
                    int y1 = centrePanel.getComponent(textFields.size()).getY() + textFields.get(0).getHeight() / 2 - 5;
                    int y2 = centrePanel.getComponent(textFields.size()).getY() + textFields.get(0).getHeight() / 2 + 5;

                    int X1_7 = -2, X2_7 = -2, Y1_7 = -2, Y2_7 = -2;
                    //для 7-ого
                    if (textFields.size() == 7) {
                        X1_7 = centrePanel.getComponent(7).getX() + centrePanel.getComponent(7).getWidth() / 2 - 5;
                        X2_7 = centrePanel.getComponent(7).getX() + centrePanel.getComponent(7).getWidth() / 2 + 5;
                        Y1_7 = centrePanel.getComponent(7 - 1).getY();
                        Y2_7 = centrePanel.getComponent(7).getY() + centrePanel.getComponent(7).getHeight();
                    }

                    int X1_5 = -2, X2_5 = -2, Y1_5 = -2;
                    //для 5-ого
                    if (textFields.size() == 5) {
                        X1_5 = centrePanel.getComponent(5).getX() + centrePanel.getComponent(5).getWidth() / 2 - 5;
                        X2_5 = centrePanel.getComponent(5).getX() + centrePanel.getComponent(5).getWidth() / 2 + 5;
                        Y1_5 = centrePanel.getComponent(5).getY() + centrePanel.getComponent(5).getHeight();
                    }

                    //количество элементов, которые удалили
                    int count = 0;

                    for(int i = 0; i < ((DrawPanel) centrePanel).listPoint.size() + count; i++) {

                        //в эту точку СЛЕВА прямая
                        if (((DrawPanel) centrePanel).listPoint.get(i - count)[4] == x1 && ((DrawPanel) centrePanel).listPoint.get(i- count)[5] == y1) {
                            System.out.println("в эту точку СЛЕВА  " + x1 + " " + y1);
                            ((DrawPanel) centrePanel).listPoint.remove(i - count);
                            count++;
                        }

                        //из этой точки ВЛЕВО прямая
                        else if (((DrawPanel) centrePanel).listPoint.get(i- count)[0] == x1 && ((DrawPanel) centrePanel).listPoint.get(i- count)[1] == y2){
                            System.out.println("из этой точки ВЛЕВО " +x1 + " " + y2);
                            ((DrawPanel) centrePanel).listPoint.remove(i - count);
                            count++;
                        }

                        else if (textFields.size() == 7) {
                            //в эту точку
                            if (((DrawPanel) centrePanel).listPoint.get(i - count)[4] == X1_7 && ((DrawPanel) centrePanel).listPoint.get(i - count)[5] == Y1_7) {
                                System.out.println("в эту точку СВЕРХУ  " + x1 + " " + y1);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            } else if (((DrawPanel) centrePanel).listPoint.get(i - count)[4] == X1_7 && ((DrawPanel) centrePanel).listPoint.get(i - count)[5] == Y2_7) {
                                System.out.println("в эту точку СНИЗУ " + X1_7 + " " + Y2_7);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            }
                            //из этой точки
                            else if (((DrawPanel) centrePanel).listPoint.get(i - count)[0] == X2_7 && ((DrawPanel) centrePanel).listPoint.get(i - count)[1] == Y1_7) {
                                System.out.println("из этой точки СВЕРХУ " + X2_7 + " " + Y1_7);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            } else if (((DrawPanel) centrePanel).listPoint.get(i - count)[0] == X2_7 && ((DrawPanel) centrePanel).listPoint.get(i - count)[1] == Y2_7) {
                                System.out.println("из этой точки СНИЗУ " + X2_7 + " " + Y2_7);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            }
                        } else if (textFields.size() == 5) {
                            //в эту точку
                            if (((DrawPanel) centrePanel).listPoint.get(i - count)[4] == X2_5 && ((DrawPanel) centrePanel).listPoint.get(i - count)[5] == Y1_5) {
                                System.out.println("в эту точку СНИЗУ " + X2_5 + " " + Y1_5);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            }
                            //из этой точки
                            else if (((DrawPanel) centrePanel).listPoint.get(i - count)[0] == X1_5 && ((DrawPanel) centrePanel).listPoint.get(i - count)[1] == Y1_5) {
                                System.out.println("из этой точки СНИЗУ " + X1_7 + " " + Y1_5);
                                ((DrawPanel) centrePanel).listPoint.remove(i - count);
                                count++;
                            }
                        }
                    }


                    if (count != 0) {

                        int count2 = 0;


                        int ind = centrePanel.getComponentZOrder(centrePanel.getComponentAt(x1, y1));;

                        //если удалили элемент
                        for (int j = 0; j < listIndexArrows.size() + count2; j++) {

                            if (listIndexArrows.get(j-count2)[0] == (ind-1) || listIndexArrows.get(j-count2)[1] == (ind-1)) {
                                listIndexArrows.remove(j - count2);
                                count2++;
                            }
                        }

                        System.out.println("Удалили элемент  " + ind + " стрелок " + count2);
                    }

                    textFields.remove(textFields.size() - 1);
                    centrePanel.remove(textFields.size() + 1);
                    centrePanel.revalidate();

                    textFields.remove(textFields.size() - 1);
                    centrePanel.remove(textFields.size() + 1);
                    centrePanel.revalidate();
                    System.out.println("Клочиество ПОЛЕЙ " + textFields.size());
                    System.out.println("Клочиество СТРЕЛОК " + listIndexArrows.size());


                    if (numberTask == 16) {
                        //если остался 1-ый столбик
                        if (textFields.size() == 1) {
                            top.setVisible(false);
                            deleteStack.setEnabled(false);
                            centrePanel.remove(1);
                            textFields.remove(0);

                            listIndexArrows.clear();
                        }
                    }

                    if (numberTask == 18) {
                        //если остался 1-ый столбик
                        if (textFields.size() == 2) {
                            top.setVisible(false);
                            topLast.setVisible(false);
                            deleteStack.setEnabled(false);
                            centrePanel.remove(1);
                            centrePanel.remove(1);
                            textFields.clear();
                        }
                    }
                    centrePanel.repaint();
                    scrollPane.revalidate();

                    //если < чем 4 столбика
                    if (textFields.size() < 7)
                        addStack.setEnabled(true);
                }
            }
        }
    }

    private JTextField createStandardItemTextField() {
        //создаем  поля для ввода
        itemTextField = new JTextField(3);

        //можно вводить только 4 символа
        itemTextField.setDocument(new PlainDocument() {
            //                    String chars = "0123456789";
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
//                        if (chars.indexOf(str) != -1) {
                if (getLength() < 4) {
                    super.insertString(offs, str, a);
                }
//                        }
            }
        });
        itemTextField.setFont(MainWindow.myFont);
        itemTextField.setHorizontalAlignment(JTextField.CENTER);    //текст по центру

        return itemTextField;
    }

    private JTextField createDisableItemTextField(JTextField field) {
        field.setEditable(false);
        field.addMouseListener(new ItemTextFieldDisableMouseAdapter(field));
        field.setBackground(new Color(140,140,140));
        field.setForeground(Color.black);
        field.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

        return field;
    }

    private void setElement_15() {
        scrollPane.repaint();
        centrePanel.repaint();
        repaint();

        centrePanel.add(top);
        centrePanel.add(stackPanel);
        if (numberTask == 17) {
            centrePanel.add(topLast);
            spr2.putConstraint(SpringLayout.NORTH, topLast, 10, SpringLayout.SOUTH, stackPanel);
            spr2.putConstraint(SpringLayout.WEST, topLast, 20, SpringLayout.WEST, scrollPane);
        }

        spr2.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.NORTH, scrollPane);
        spr2.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, scrollPane);
        spr2.putConstraint(SpringLayout.NORTH, stackPanel, 10, SpringLayout.SOUTH, top);
        spr2.putConstraint(SpringLayout.WEST, stackPanel, 5, SpringLayout.EAST, top);
    }


    private class ItemTextFieldEnableMouseAdapter extends MouseAdapter {
        private JTextField field;

        ItemTextFieldEnableMouseAdapter(JTextField p) {
            this.field = p;
        }

        ItemTextFieldEnableMouseAdapter() {}

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getModifiers() == e.BUTTON1_MASK) {

                //если правой кнопкой мыши щелкнули по полю с заданием, устанавливаем стандартную рамку
                if (e.getSource().equals(textTask)) {
                    textTask.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 1));
                }

                //иначе, по полю в стеке
                else {
                    if (numberTask == 15 || numberTask == 17) {
                        //индекс элемента field в панели stackPanel
                        int indexField = stackPanel.getComponentZOrder(field);
                        itemTextField = textFields.get(indexField);

                        if (itemTextField.getText().equals("//////")) {
                            itemTextField.setText("");
                        }
                    }
                    if (numberTask == 16) {
                        //индекс элемента field в панели stackPanel
                        int indexField = centrePanel.getComponentZOrder(field) - 1;
                        itemTextField = textFields.get(indexField);

                        if (itemTextField.getText().equals("//////")) {
                            itemTextField.setText("");
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getModifiers()  == e.BUTTON1_MASK) {

                if (field != null) {
                    if (numberTask == 15 || numberTask == 17) {
                        int indexField = stackPanel.getComponentZOrder(field);
                        itemTextField = textFields.get(indexField);
                    }

                    if (numberTask == 16) {
                        int indexField = centrePanel.getComponentZOrder(field) - 1;
                        itemTextField = textFields.get(indexField);
                    }
                }
            }
            System.out.println("Нажатие ЛЕВОЙ клавиши мыши");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (field != null) {
                if (numberTask == 15 || numberTask == 17) {
                    int indexField = stackPanel.getComponentZOrder(field);
                    itemTextField = textFields.get(indexField);
                }

                if (numberTask == 16) {
                    int indexField = centrePanel.getComponentZOrder(field)-1;
                    itemTextField = textFields.get(indexField);
                }

                if (itemTextField.getText().equals("")) {
                    itemTextField.setText("//////");
                }
            }
        }
    }

    private class ItemTextFieldDisableMouseAdapter extends MouseAdapter {
        private JTextField field;
        private int indexPressed;
        private int indexReleased;

        ItemTextFieldDisableMouseAdapter(JTextField p) {
            this.field = p;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getModifiers() == e.BUTTON1_MASK) {
                if (field != null) {

                    if (numberTask == 16) {

                        indexReleased = centrePanel.getComponentZOrder(centrePanel.getComponentAt((e.getX() + 15 + (35 + textFields.get(0).getWidth()) * (indexPressed / 2)), (e.getY() + 20 + top.getHeight() + 2)));

                        //если отпустили над полем для указания связи
                        if (indexReleased % 2 == 1) {
//                            System.out.println("Координаты Released " + (e.getX() + 15 + (35 + textFields.get(0).getWidth()) * (indexPressed / 2)) + " " + (e.getY() + 20 + top.getHeight() + 2));

                            //размер списка стрелок ДО
                            int to = ((DrawPanel) centrePanel).listPoint.size();


                            itemTextField = textFields.get(indexReleased - 1);

                            System.out.println("Отпустили ЛЕВУЮ клавишу мыши " + indexReleased);

                            //отпустили на той же клетке
                            if (indexPressed == indexReleased) {
                                if (itemTextField.getText().equals("")) {
//                                    itemTextField.setFont(MainWindow.myFont16);
                                    itemTextField.setText("*");
                                } else if (itemTextField.getText().equals("*")) {
                                    itemTextField.setText("nil");
//                                    itemTextField.setFont(MainWindow.myFont);
                                } else if (itemTextField.getText().equals("nil")) {
                                    itemTextField.setText("");
                                }
                            }

                            //отпустили на соседней клетке
                            else if (Math.abs(indexPressed - indexReleased) == 2) {
                                int x1, y1, x2, y3;

                                //рисуем СЛЕВА НАПРАВО
                                if ((indexPressed - indexReleased) == -2) {

                                    //рисуем стрелочку
                                    x1 = centrePanel.getComponent(indexPressed).getX() + textFields.get(0).getWidth();
                                    y1 = centrePanel.getComponent(indexPressed).getY() + textFields.get(0).getHeight() / 2 - 5;
                                    x2 = centrePanel.getComponent(indexReleased).getX();
                                    y3 = centrePanel.getComponent(indexReleased).getY() + textFields.get(0).getHeight() / 2 - 5;

                                }

                                //рисуем СПРАВА НАЛЕВО
                                else {
                                    //рисуем стрелочку
                                    x1 = centrePanel.getComponent(indexPressed).getX();
                                    y1 = centrePanel.getComponent(indexPressed).getY() + textFields.get(0).getHeight() / 2 + 5;
                                    x2 = centrePanel.getComponent(indexReleased).getX() + textFields.get(0).getWidth();
                                    y3 = centrePanel.getComponent(indexReleased).getY() + textFields.get(0).getHeight() / 2 + 5;

                                }

                                centrePanel.removeAll();
                                ((DrawPanel) centrePanel).setCoordinates(x1, y1, x2, y1, x2, y3);

                                setElement_16();
                            }
                            else {

                                int x1 = -1, y1 = -1, x2 = -1, y3 = -1;


                                //стрелка с 1-ого столбика
                                if (indexPressed == 1) {

                                    y1 = centrePanel.getComponent(indexPressed).getY() + textFields.get(0).getHeight();

                                    if (indexReleased == 5) {
                                        x1 = centrePanel.getComponent(indexPressed).getX() + 3*centrePanel.getComponent(indexPressed).getWidth()/5;
                                        x2 = centrePanel.getComponent(indexReleased).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 + 5;
                                        y3 = y1+ 12;

                                    } else if (indexReleased == 7) {
                                        x1 = centrePanel.getComponent(indexPressed).getX() + 2*centrePanel.getComponent(indexPressed).getWidth()/5;
//                                        y1 = centrePanel.getComponent(indexPressed).getY() + textFields.get(0).getHeight();
                                        x2 = centrePanel.getComponent(indexReleased).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 - 5;
                                        y3 = y1+ 16;
                                    }

                                }
                                //стрелка со 2-ого столбика
                                else if (indexPressed == 3) {
//                                    if (indexReleased == 7) {
                                    x1 = centrePanel.getComponent(indexPressed).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 + 5;
                                    y1 = centrePanel.getComponent(indexPressed-1).getY();
                                    x2 = centrePanel.getComponent(indexReleased).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 - 5;
                                    y3 = y1 - 8;
//                                    }
                                }
                                else if (indexPressed == 5) {
//                                    if (indexReleased == 1) {
                                    x1 = centrePanel.getComponent(indexPressed).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 - 5;
                                    y1 = centrePanel.getComponent(indexPressed).getY() + centrePanel.getComponent(indexPressed).getHeight();
                                    x2 = centrePanel.getComponent(indexReleased).getX() + 4*centrePanel.getComponent(indexReleased).getWidth()/5;
                                    y3 = y1 + 8;
//                                    }
                                }
                                else if (indexPressed == 7) {

                                    x1 = centrePanel.getComponent(indexPressed).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 + 5;

                                    if (indexReleased == 1) {
                                        y1 = centrePanel.getComponent(indexPressed).getY() + centrePanel.getComponent(indexPressed).getHeight();
                                        x2 = centrePanel.getComponent(indexReleased).getX() + 1*centrePanel.getComponent(indexReleased).getWidth()/5;
                                        y3 = y1 + 20;

                                    } else if (indexReleased == 3) {
//                                        x1 = centrePanel.getComponent(indexPressed).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 + 5;
                                        y1 = centrePanel.getComponent(indexPressed-1).getY();
                                        x2 = centrePanel.getComponent(indexReleased).getX() + centrePanel.getComponent(indexPressed).getWidth()/2 - 5;
                                        y3 = y1 - 12;
                                    }
                                }

                                centrePanel.removeAll();
                                ((DrawPanel) centrePanel).setCoordinates(x1, y1, x1, y3, x2, y1);

                                setElement_16();
                            }

                            //размер списка стрелок ПОСЛЕ
                            int after = ((DrawPanel) centrePanel).listPoint.size();

                            //если добавили элемент
                            if (after > to) {
                                Integer[] m = new Integer[2];
                                m[0] = indexPressed - 1;
                                m[1] = indexReleased - 1;

                                listIndexArrows.add(m);

                                System.out.println("+");
                            } else {
                                for (int i = 0; i < listIndexArrows.size(); i++) {
                                    if (listIndexArrows.get(i)[0] == (indexPressed - 1) && listIndexArrows.get(i)[1] == (indexReleased-1)) {
                                        System.out.println((indexPressed-1) + " " + (indexReleased-1));
                                        listIndexArrows.remove(i);
                                        System.out.println("-");
                                        break;
                                    }
                                }
                            }

                            System.out.println("В списке "+listIndexArrows.size());
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed (MouseEvent e){
            if (e.getModifiers() == e.BUTTON1_MASK) {
                if (field != null) {

                    if (numberTask == 16) {
                        indexPressed = centrePanel.getComponentZOrder(field);
                    }
                }
            }
            System.out.println("Нажатие ЛЕВОЙ клавиши мыши " + indexPressed);
        }
    }



    private void setElement_16() {
        centrePanel.repaint();

        centrePanel.add(top);
        spr2.putConstraint(SpringLayout.NORTH, top, 20, SpringLayout.NORTH, scrollPane);
        spr2.putConstraint(SpringLayout.WEST, top, 20, SpringLayout.WEST, scrollPane);

        for (int i=0; i<textFields.size(); i++) {
            centrePanel.add(textFields.get(i));

            if (i == 0) {
                //1
                spr2.putConstraint(SpringLayout.NORTH, textFields.get(i), 2, SpringLayout.SOUTH, top);
                spr2.putConstraint(SpringLayout.WEST, textFields.get(i), -5, SpringLayout.WEST, top);
            }

            //рисуем верхние элементы
            else if (i%2 == 1) {
                spr2.putConstraint(SpringLayout.NORTH, textFields.get(i), -2, SpringLayout.NORTH, top);
                spr2.putConstraint(SpringLayout.WEST, textFields.get(i), 35, SpringLayout.EAST, textFields.get(i-1));
            }
            //рисуем нижние
            else {
                spr2.putConstraint(SpringLayout.NORTH, textFields.get(i), 0, SpringLayout.SOUTH, textFields.get(i-1));
                spr2.putConstraint(SpringLayout.WEST, textFields.get(i), 35, SpringLayout.EAST, textFields.get(i-2));
            }
        }


        if (numberTask == 18) {


        }

    }

    private void addTask() {
        //фиксируем ответ и отправляем на сервер
        listAnswer = new ArrayList<>();

        if (numberTask == 15) {
            countElement = textFields.size();

            //список ответов - перечень содержимого ячеек + индекс указателя
            for (int i = 0; i < textFields.size(); i++) {
                listAnswer.add(textFields.get(i).getText().trim());
            }
            listAnswer.add(""+indexTop);
        }

        if (numberTask == 16) {
            countElement = (textFields.size()+ 1) / 2;

            //список ответов - перечень содержимого ячеек + пары индексов указателей
            for (int i = 0; i < textFields.size(); i++) {
                listAnswer.add(textFields.get(i).getText().trim());
            }

            for (int i = 0; i < listIndexArrows.size(); i++) {
                listAnswer.add(listIndexArrows.get(i)[0].toString());
                listAnswer.add(listIndexArrows.get(i)[1].toString());
            }


        }

        if (numberTask == 17) {
            countElement = textFields.size();

            //список ответов - перечень содержимого ячеек + два индекса указателей
            for (int i = 0; i < textFields.size(); i++) {
                listAnswer.add(textFields.get(i).getText().trim());
            }
            listAnswer.add(""+indexTop);
            listAnswer.add(""+indexTopLast);
        }

        sendData(new MessageProfessor(ADD_TASK, new Tasks(numberTask, countElement, listAnswer, textTask.getText().trim())));

        Tasks addTask = new Tasks(numberTask, countElement, listAnswer, textTask.getText().trim());
        System.out.print(addTask.getNumber() + "        ");      //номер задачи
        System.out.print(addTask.getCountElements() + "        ");
        System.out.println(addTask.getListTask() + "        ");
        System.out.println(addTask.getVerbalTask() + "        ");
    }
}
