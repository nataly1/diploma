package ru.natasha.window.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Natasha on 13.04.2015.
 */
public class RendererPC_IP extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        cell.setBackground(new Color(245,245,245));

        if (table.getValueAt(row, 2).equals("разрешено")) {
//            cell.setBackground(new Color(202, 225, 255));
            cell.setBackground(new Color(78, 238, 148));
        }
        if (isSelected) {
            cell.setBackground(new Color(155, 193, 225));
        }


        return cell;
    }
}
