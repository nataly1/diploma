package ru.natasha.window.tableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by Natasha on 08.04.2015.
 */
public class PC_IPTableModel extends AbstractTableModel {
    private int countColomn;
    private ArrayList<String[]> dataArrayList = null;

    public PC_IPTableModel() {
        this.countColomn = 3;
        dataArrayList = new ArrayList<String []>();

        for (int i=0; i < dataArrayList.size(); i++) {
            dataArrayList.add(new String[getColumnCount()]);
        }
    }

    //возврашает количество строк в таблице
    @Override
    public int getRowCount() {
        return dataArrayList.size();
    }

    //возврашает количество колонок в таблице
    @Override
    public int getColumnCount() {
        return this.countColomn;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ПК";
            case 1: return "IP";
            case 2: return "Статус";
        }

        return "";
    }

    //возврашает значение из определенной ячейки таблицы по номеру строки и столбца
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);

        return rows[columnIndex];
    }

    public void addDate(ArrayList<String []> row) {
        dataArrayList.clear();

        for (int i =0; i<row.size();i++) {
            //добавляем строку
            dataArrayList.add(row.get(i));
        }
    }
}
