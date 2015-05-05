package ru.natasha.window;

import ru.natasha.window.tableModel.MarkTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static ru.natasha.window.TestWindow.*;


/**
 * Created by Natasha on 07.04.2015.
 */
public class ResultWindow extends JFrame {
    private JLabel allTask;
    private JLabel studentSetTask;
    private JLabel allScore;
//    private JLabel mark;

    private MarkTableModel markTableModel;
    private JTable markTable;
    private JScrollPane markTableScroll;

    private JButton detail;

    private SpringLayout spr = new SpringLayout();

    private Boolean getDetail;

    public ResultWindow(ArrayList<Integer []> listMark) {
        super("Результат тестирования");

        getDetail = true;

        Font font = new Font(null, Font.ROMAN_BASELINE, 16);

        this.setLayout(spr);

        allTask = new JLabel("Было предложено " + questionPool.getSize() + " вопросов");
        allTask.setFont(font);
        studentSetTask = new JLabel("Вы ответили на " + (questionPool.getSize() - countTaskWithOutAnswer) + " из них");
        studentSetTask.setFont(font);
        allScore = new JLabel("Вы набрали " + countScore(listMark) +" баллов из "+ maxScore(listMark));
        allScore.setFont(font);
//        mark = new JLabel("Оценка за тест: ");
//        mark.setFont(font);
        detail = new JButton("Подробнее");
        detail.addMouseListener(new DetailMouseListener());


        markTableModel = new MarkTableModel();
        markTable = new JTable(markTableModel);
        //запретить перетаскивание столбцов
        markTable.getTableHeader().setReorderingAllowed(false);
        //отключить возможность изменять ширину стобцов
        for (int i=0;i<markTable.getColumnModel().getColumnCount();i++){
            markTable.getColumnModel().getColumn(i).setResizable(false);
        }

//        markTable.setRowSelectionAllowed(false);
        markTable.setEnabled(false);
        markTable.setBackground(new Color(238, 238, 238));

        markTableScroll = new JScrollPane(markTable);
        markTableScroll.setPreferredSize(new Dimension(150, 200));
        markTableScroll.setVisible(false);

        this.add(allTask);
        this.add(studentSetTask);
        this.add(allScore);
//        this.add(mark);
        this.add(detail);
        this.add(markTableScroll);

        spr.putConstraint(SpringLayout.NORTH, allTask, 40, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, allTask, 50, SpringLayout.HORIZONTAL_CENTER, this);
        spr.putConstraint(SpringLayout.NORTH, studentSetTask, 20, SpringLayout.SOUTH, allTask);
        spr.putConstraint(SpringLayout.WEST, studentSetTask, 50, SpringLayout.HORIZONTAL_CENTER, this);
        spr.putConstraint(SpringLayout.NORTH, allScore, 20, SpringLayout.SOUTH, studentSetTask);
        spr.putConstraint(SpringLayout.WEST, allScore, 50, SpringLayout.HORIZONTAL_CENTER, this);
//        spr.putConstraint(SpringLayout.NORTH, mark, 20, SpringLayout.SOUTH, allScore);
//        spr.putConstraint(SpringLayout.WEST, mark, 30, SpringLayout.HORIZONTAL_CENTER, this);
        spr.putConstraint(SpringLayout.NORTH, detail, 40, SpringLayout.SOUTH, allScore);
        spr.putConstraint(SpringLayout.WEST, detail, 50, SpringLayout.HORIZONTAL_CENTER, this);
        spr.putConstraint(SpringLayout.NORTH, markTableScroll, 20, SpringLayout.SOUTH, detail);
        spr.putConstraint(SpringLayout.WEST, markTableScroll, 60, SpringLayout.EAST, this);

        addMarkTableModel(listMark);

        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    private void addMarkTableModel(ArrayList<Integer []> listMark) {
        String[] str;
        for (int i = 0; i < listMark.size(); i++){
            str = new String[2];

            str[0] =  ""+(i+1);
            str[1] = listMark.get(i)[0].toString();

            markTableModel.addDate(str);
//            System.out.println(str[0].toString() + " " + str[1].toString());
        }
    }

    private class DetailMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (getDetail) {
                detail.setText("Скрыть");
                markTableScroll.setVisible(true);
                getDetail = false;
            }
            else {
                detail.setText("Подробнее");
                markTableScroll.setVisible(false);
                getDetail = true;
            }
        }
    }

    //подсчитываем общее кол-во набранных баллов
    private Integer countScore(ArrayList<Integer[]> listMark) {
        Integer result = 0;

        for (int i=0; i < listMark.size(); i++) {
            result += listMark.get(i)[0];
            System.out.println("Задание " + (i+1) + " балл " + listMark.get(i)[0]);
        }
        return result;
    }

    //подсчитываем кол-во баллов, которрое можно было набрать
    private Integer maxScore(ArrayList<Integer[]> listMark) {
        Integer result = 0;

        for (int i=0; i < listMark.size(); i++)
            result += listMark.get(i)[1];

        return result;
    }
}
