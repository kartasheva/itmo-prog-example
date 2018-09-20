package ru.ifmo.prog.lab7;

import javax.swing.*;
import java.io.IOException;

public class Lab7Client {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            ClientGraphics clientGraphics = new ClientGraphics();
        });
    }
}
