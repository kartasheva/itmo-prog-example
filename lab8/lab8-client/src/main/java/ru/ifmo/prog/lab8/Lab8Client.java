package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.io.IOException;

public class Lab8Client {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            ClientGraphics clientGraphics = new ClientGraphics();
        });
    }
}
