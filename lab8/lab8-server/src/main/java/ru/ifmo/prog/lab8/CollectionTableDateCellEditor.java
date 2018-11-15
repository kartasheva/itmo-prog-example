package ru.ifmo.prog.lab8;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EventObject;

public class CollectionTableDateCellEditor extends AbstractCellEditor implements TableCellEditor {
    private OffsetDateTime date = OffsetDateTime.now();
    private JTextField textField;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textField = new JTextField();
        textField.setBorder(null);
        if (value != null && !(value.toString().isEmpty())) {
            textField.setText(value.toString());
        }

        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        String str = textField.getText();
        if(!str.isEmpty())
            try {
                date = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, LabLocale.getInstance().getString("Date_format_error"));
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
