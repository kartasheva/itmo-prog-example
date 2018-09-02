package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CollectionTableColorCellRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ru.ifmo.prog.lab7.Color color = (ru.ifmo.prog.lab7.Color) value;
        if (color != null) {
            JLabel colorLabel = new JLabel(color.toString());
            colorLabel.setOpaque(true);
            colorLabel.setBackground(color.toAWTColor());

            return colorLabel;
        }

        return null;
    }
}
