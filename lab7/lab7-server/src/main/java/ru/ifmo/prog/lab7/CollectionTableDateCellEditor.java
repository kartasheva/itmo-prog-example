package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

public class CollectionTableDateCellEditor extends AbstractCellEditor implements TableCellEditor {
    private Date date = new Date();
    private JTextField textField;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textField = new JTextField();
        textField.setBorder(null);
        if (!(value.toString().isEmpty())) {
            textField.setText(value.toString());
        }

        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        String str = textField.getText();
        if(!str.isEmpty())
            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                date = format.parse(str);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка формата даты");
            }
        return date;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}
