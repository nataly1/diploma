package ru.natasha.window.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Natasha on 14.04.2015.
 */
public class RenderClassRoom extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if ((row % 2) == 0)
            cell.setBackground(new Color(255, 255, 255));
        else
            cell.setBackground(new Color(245, 245, 245));

        if (isSelected) {
            cell.setBackground(new Color(155, 193, 225));
        }


        return cell;
    }
}
