package ru.natasha.window.tableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by Natasha on 07.04.2015.
 */
public class MarkTableModel extends AbstractTableModel {

    private int countColomn;
    private ArrayList<String []> dataArrayList = null;

    public MarkTableModel() {
        this.countColomn = 2;
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
            case 0: return "№ вопроса";
            case 1: return "Ваш балл";
        }

        return "";
    }

    //возврашает значение из определенной ячейки таблицы по номеру строки и столбца
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);

        return rows[columnIndex];
    }

    public void addDate(String [] row) {
        String[] rowTable = new String[getColumnCount()];
        rowTable = row;

        //добавляем строку
        dataArrayList.add(rowTable);
    }
}
