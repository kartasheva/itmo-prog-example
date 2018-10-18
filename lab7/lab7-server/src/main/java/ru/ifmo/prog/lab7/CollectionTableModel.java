package ru.ifmo.prog.lab7;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionTableModel extends AbstractTableModel implements TableModel {
    private Collection<Cart> collection;
    private Hashtable<String, Cart> hashtable;
    private List<Field> fields;

    public CollectionTableModel(Collection<Cart> collection) {
        this.collection = collection;
        this.hashtable = collection.getStore();

        fields = Arrays.stream(collection.getItemType().getDeclaredFields())
                .collect(Collectors.toList());
    }

    @Override
    public int getRowCount() {
        return hashtable.size();
    }

    @Override
    public int getColumnCount() {
        return fields.size() + 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "KEY";
        } else {
            return fields.get(columnIndex - 1).getName();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return fields.get(columnIndex - 1).getType();
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String key = (String) hashtable.keySet().toArray()[rowIndex];

        if (columnIndex == 0) {
            return key;
        } else {
            Field field = fields.get(columnIndex - 1);

            try {
                field.setAccessible(true);
                return field.get(hashtable.get(key));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < hashtable.size()) {
            String key = (String) hashtable.keySet().toArray()[rowIndex];

            if (columnIndex == 0) {
                Cart store = hashtable.get(key);
                hashtable.remove(key);
                hashtable.put((String) aValue, store);
            } else {
                Field field = fields.get(columnIndex - 1);

                try {
                    field.setAccessible(true);
                    field.set(hashtable.get(key), field.getType().cast(aValue));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
