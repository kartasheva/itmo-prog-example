package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class CollectionTableColorCellEditor extends AbstractCellEditor implements TableCellEditor {
//    private ru.ifmo.prog.lab7.Color color = new ru.ifmo.prog.lab7.Color();
    private JTextField colorField;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ru.ifmo.prog.lab7.Color color = (Color) value;

        colorField = new JTextField(color != null ? color.toString() : null);
        colorField.setBorder(null);
        java.awt.Color awtColor = JColorChooser.showDialog(colorField, "Choose a color", color != null ? color.toAWTColor() : null);
        color = awtColor != null ? new Color(awtColor) : color;
        colorField.setText(color != null ? color.toString() : "");
        return colorField;
    }

    @Override
    public Object getCellEditorValue() {
        return new ru.ifmo.prog.lab7.Color(colorField.getText());
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
