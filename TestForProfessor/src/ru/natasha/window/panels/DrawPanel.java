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
    public ArrayList<Integer[]> listPoint;

    public DrawPanel(int numberTask) {
        this.numberTask = numberTask;
        listPoint = new ArrayList<>();
    }


    public void setCoordinates(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;

        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;

        if(x2 != -1 && x3 != -1) {

            if (numberTask == 15 || numberTask == 17) {

                //для этого задания возможно единовременное отображение только 1 линии
                if (numberTask == 15) {
                    if (listPoint.size() != 0)
                        listPoint.remove(listPoint.size() - 1);
                }

                //для этого задания возможно единовременное отображение только 1 линии для каждого из объектов
                else if (numberTask == 17) {
                    if (listPoint.size() != 0) {

                        Iterator it = listPoint.iterator();
                        while (it.hasNext()) {
                            Integer[] item = (Integer[]) it.next();
                            //если из этого объекта уже проведена стрелка
                            if (item[0] == x1 && item[1] == y1) {
                                it.remove();
                            }
                        }
                    }
                }


                addElement();
            }

            //для этого задания возможно единовременное отображение 2-х линий для каждого из объектов
            else if (numberTask == 16) {
                boolean elementDeleted = false;
                System.out.println("Размер массива до " + listPoint.size());

                if (listPoint.size() != 0) {

                    for(int i = 0; i < listPoint.size(); i++) {

                        //если уже отрисована линия соединяющая эти 2 точки, удаляем её
                        if (listPoint.get(i)[0] == x1 && listPoint.get(i)[1] == y1 && listPoint.get(i)[4] == x3 && listPoint.get(i)[5] == y3) {
                            System.out.println(x1+ " " + y1+ " " + x3 + " " + y3);
                            listPoint.remove(i);
                            elementDeleted = true;
                            break;
                        }
                    }

                    //если не удалили, значит она ещё не отрисована
                    if (!elementDeleted) {
                        addElement();
                    }
                }
                else {
                    addElement();
                }
            }


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
                            System.out.println("Удалить линия из " + x1 + " в " + y1);

                            myIt.remove();
                        }
                    }
                }
            }

        }
    }

    private void addElement() {
        //добавляем новые координаты в список
        Integer[] mass = new Integer[6];
        mass[0] = x1;
        mass[1] = y1;
        mass[2] = x2;
        mass[3] = y2;
        mass[4] = x3;
        mass[5] = y3;

        listPoint.add(mass);
//        for (Integer in : mass) {
//            System.out.println(in);
//        }
    }

    public void paint(Graphics g) {
        super.paint(g);

//        if(x1 != -1 && x2 != -1) {

        if (numberTask == 15 || numberTask == 17) {
            for (Integer[] massPoint : listPoint) {

                if (massPoint[5] > massPoint[1]) {

                    g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);
                    g.drawLine(massPoint[2], massPoint[3], massPoint[4], massPoint[5]);

                    //рисуем стрелочки
                    g.drawLine(massPoint[4] - 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                    g.drawLine(massPoint[4] + 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                } else if (massPoint[5] < massPoint[1]) {

                    g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);
                    g.drawLine(massPoint[2], massPoint[3], massPoint[4], massPoint[5]);

                    //рисуем стрелочки
                    g.drawLine(massPoint[4] - 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                    g.drawLine(massPoint[4] + 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                }
            }
        }

        if (numberTask == 16) {

            for (Integer[] massPoint : listPoint) {

                //если стрелка рисуется линией на одном уровне
                if (massPoint[3] == massPoint[5] && massPoint[5]== massPoint[1] ) {
                    g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);

                    //рисуем СЛЕВА НАПРАВО
                    if (massPoint[0] <= massPoint[2]) {

                        //рисуем стрелочки
                        g.drawLine(massPoint[4] - 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                        g.drawLine(massPoint[4] - 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                    }

                    //рисуем СПРАВА НАЛЕВО
                    else if (massPoint[0] >= massPoint[2]) {

                        //рисуем стрелочки
                        g.drawLine(massPoint[4] + 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                        g.drawLine(massPoint[4] + 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                    }
                }
                else {
                    //рисуем 3 линии

                    g.drawLine(massPoint[0], massPoint[1], massPoint[2], massPoint[3]);
                    g.drawLine(massPoint[2], massPoint[3], massPoint[4], massPoint[3]);
                    g.drawLine(massPoint[4], massPoint[3], massPoint[4], massPoint[5]);

                    //рисуем СВЕРХУ ВНИЗ
                    if (massPoint[3] < massPoint[1]) {

                        //рисуем стрелочки
                        g.drawLine(massPoint[4] - 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                        g.drawLine(massPoint[4] + 5, massPoint[5] - 5, massPoint[4], massPoint[5]);
                    }

                    //рисуем СНИЗУ ВВЕРХ
                    else if (massPoint[3] > massPoint[1]) {

                        //рисуем стрелочки
                        g.drawLine(massPoint[4] - 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                        g.drawLine(massPoint[4] + 5, massPoint[5] + 5, massPoint[4], massPoint[5]);
                    }
                }
            }
        }

        Component[] coms = this.getComponents();
        for (Component curCom : coms)
            curCom.repaint();

//            Polygon a = new Polygon();
//            double  beta = Math.atan2(y2-y3,x3-x2); //{ArcTan2 ищет арктангенс от x/y что бы неопределенностей не
//            //  возникало типа деления на ноль}
//            double alfa = Math.PI/15;// {угол между основной осью стрелки и рисочки в конце}
//            int r1 = 15; //{длинна риски}
//
//            int x4 =(int) Math.round(x3 - r1*Math.cos(beta + alfa));
//            int y4 =(int)Math.round(y3 + r1*Math.sin(beta + alfa));
//            a.addPoint(x4, y4);
//            x4 =(int) Math.round(x3 - r1*Math.cos(beta - alfa));
//            y4 =(int)Math.round(y3 + r1*Math.sin(beta - alfa));
//            a.addPoint(x4, y4);
//            a.addPoint(x3, y3);
//            g.drawPolygon(a);
//        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
