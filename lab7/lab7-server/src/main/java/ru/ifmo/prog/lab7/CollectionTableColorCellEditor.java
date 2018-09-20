package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class CollectionTableColorCellEditor extends AbstractCellEditor implements TableCellEditor {
//    private ru.ifmo.prog.lab7.LabColor color = new ru.ifmo.prog.lab7.LabColor();
    private JTextField colorField;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        LabColor labColor = (LabColor) value;

        colorField = new JTextField(labColor != null ? labColor.toString() : null);
        colorField.setBorder(null);
        java.awt.Color awtColor = JColorChooser.showDialog(colorField, "Choose a labColor", labColor != null ? labColor.toAWTColor() : null);
        labColor = awtColor != null ? new LabColor(awtColor) : labColor;
        colorField.setText(labColor != null ? labColor.toString() : "");
        return colorField;
    }

    @Override
    public Object getCellEditorValue() {
        return new LabColor(colorField.getText());
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
