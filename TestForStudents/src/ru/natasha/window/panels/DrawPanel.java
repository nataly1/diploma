package ru.natasha.window.panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Natasha on 22.04.2015.
 */
public class DrawPanel extends JPanel {

    private int x1 = -1;
    private int x2 = -1;
    private int x3 = -1;

    private int y1 = -1;
    private int y2 = -1;
    private int y3 = -1;

    private int numberTask;
    private ArrayList<Integer[]> listPoint;

    public DrawPanel() {

    }

    public void setNumberTask(int numberTask) {
        this.numberTask = numberTask;
        listPoint = new ArrayList<>();
    }

    public int getX1() {
        if (numberTask == 15) {
            if (listPoint.size() != 0)
                return listPoint.get(0)[0];
        }
        return 0;
    }

    public int getY1() {
        if (numberTask == 15) {
            if (listPoint.size() != 0)
                return listPoint.get(0)[1];
        }
        return 0;
    }

    public void setCoordinates(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;

        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;

        if(x2 != -1 && x3 != -1) {
            //для этого задания возможно единовременное отображение только 1 линии
            if (numberTask == 15) {
                if (listPoint.size() != 0)
                    listPoint.remove(listPoint.size()-1);
            }

            //для этого задания возможно единовременное отображение только 1 линии для каждого из объектов
            if (numberTask == 17) {
                if (listPoint.size() != 0) {
                    System.out.println("Размер массива " + listPoint.size());

                    Iterator it = listPoint.iterator();
                    while (it.hasNext())
                    {
                        Integer[] item = (Integer[])it.next();
                        //если из этого объекта уже проведена стрелка
                        if (item[0] == x1 && item[1] == y1) {
                            it.remove();
                        }
                    }
                }
            }

            //добавляем новые координаты в список
            Integer[] mass = new Integer[6];
            mass[0] = x1;
            mass[1] = y1;
            mass[2] = x2;
            mass[3] = y2;
            mass[4] = x3;
            mass[5] = y3;

            listPoint.add(mass);

            System.out.println(x1 +" "+ y1 + " "+ x2 +" "+ y2);

        }
        else {
            if (numberTask == 15 || numberTask == 17) {
                System.out.println("Размер массива " + listPoint.size());

                if (listPoint.size() != 0) {
                    Iterator myIt = listPoint.iterator();
                    while (myIt.hasNext())
                    {
                        Integer[] item = (Integer[])myIt.next();
                        //если из этого объекта уже проведена стрелка
                        if (item[0] == x1 && item[1] == y1) {
                            System.out.println("Уже проведена линия из " + x1 + " в " + y1);

                            myIt.remove();
                        }
                    }
                }
            }

        }
    }


    public void paint(Graphics g) {
        super.paint(g);

        if(listPoint != null) {

        for (Integer[] massPoint : listPoint) {

            if (massPoint[5] > massPoint[1]) {

                g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);
                g.drawLine(massPoint[2], massPoint[3], massPoint[4], massPoint[5]);

                //рисуем стрелочки
                g.drawLine(massPoint[4] - 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                g.drawLine(massPoint[4] + 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
            }
            else if (massPoint[5] < massPoint[1])  {

                g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);
                g.drawLine(massPoint[2], massPoint[3], massPoint[4], massPoint[5]);

                //рисуем стрелочки
                g.drawLine(massPoint[4] - 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                g.drawLine(massPoint[4] + 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
            }

            Component[] coms = this.getComponents();
            for (Component curCom : coms)
                curCom.repaint();
        }

        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
