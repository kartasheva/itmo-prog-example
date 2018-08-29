package ru.ifmo.prog.lab7;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class MyTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private Cart cart;
    private Collection coll;
    private Hashtable store;
    private Object o;

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return Integer.class;
            case 5:
                return Integer.class;
            case 6:
                return Integer.class;

        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Key";
            case 1:
                return "ClatterOfHooves";
            case 2:
                return "Title";
            case 3:
                return "Size";
            case 4:
                return "X";
            case 5:
                return "Y";
            case 6:
                return "CreatedAt";
        }
        return "";
    }

    public int getRowCount() {
        return coll.getCollection().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex){
        store = coll.getCollection();
        o = store.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return "";
            case 1:
                return cart.isClatterOfHooves();
            case 2:
                return cart.getTitle();
            case 3:
                return cart.getSize();
            case 4:
                return cart.getX();
            case 5:
                return cart.getY();
            case 6:
                return cart.getCreatedAt();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }
}
