package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CollectionTableColorCellRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        LabColor labColor = (LabColor) value;
        if (labColor != null) {
            JLabel colorLabel = new JLabel(labColor.toString());
            colorLabel.setOpaque(true);
            colorLabel.setBackground(labColor.toAWTColor());

            return colorLabel;
        }

        return null;
    }
}
