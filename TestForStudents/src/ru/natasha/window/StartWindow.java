package ru.natasha.window;

import ru.natasha.message.MessageStudent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ru.natasha.connect.ConnectWithServer.*;

/**
 * Created by Natasha on 17.03.2015.
 */
public class StartWindow extends JFrame {
    public static Font myFont = new Font(null, Font.BOLD, 14);

    private JButton demo;
    private JButton test;
    private SpringLayout spr = new SpringLayout();
    private SpringLayout sprPanel = new SpringLayout();

    public StartWindow() {
        super("Приложение для студента");
        this.setLayout(spr);
        this.setBackground(Color.BLACK);

        demo = new JButton("ДЕМО-Тест");
        this.add(demo);
        demo.addMouseListener(new MyMouse());
        spr.putConstraint(SpringLayout.NORTH, demo, 60, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, demo, 80, SpringLayout.HORIZONTAL_CENTER, this);

        test = new JButton("Тест");
        this.add(test);
        test.addMouseListener(new MyMouse());
        spr.putConstraint(SpringLayout.NORTH, test, 60, SpringLayout.NORTH, this);
        spr.putConstraint(SpringLayout.WEST, test, 40, SpringLayout.EAST, demo);

        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
//        this.test.addMouseMotionListener(new DragAndDropExample());
    }

    private class MyMouse extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == demo) {
                sendData(new MessageStudent(GET_DEMOTEST));
            }
            if (e.getSource() == test) {
                sendData(new MessageStudent(TEST_STATUS));
            }
        }
    }

//    private class  DragAndDropExample extends MouseMotionAdapter {
//        @Override
//        public void mouseDragged(MouseEvent e) {
//            int x = e.getX();
//            int y = e.getY();
//
//            System.out.println(x + " " + y);
//            test.setLocation(x, y);
//        }
//    }
}
