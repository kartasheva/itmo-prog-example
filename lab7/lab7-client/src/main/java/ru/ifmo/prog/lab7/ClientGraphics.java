package ru.ifmo.prog.lab7;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import static javax.swing.Box.createHorizontalBox;

public class ClientGraphics extends JFrame {
    Hashtable<String, Cart> store = null;

    CartFilters currentFilters = new CartFilters();

    DrawablePane drawablePane;


    public ClientGraphics() {
        super("Client");
        updateStore(currentFilters);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = getContentPane();

        drawablePane = new DrawablePane(new ArrayList<>(store.values()));
        drawablePane.setFilters(currentFilters);
        setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        Box filtersBox = createHorizontalBox(),
                actionsBox = createHorizontalBox();

        JTextField titleField;
        ButtonGroup clatterOfHoovesGroup = new ButtonGroup();
        JRadioButton clatterOfHoovesTrue,
                clatterOfHoovesFalse,
                clatterOfHoovesNull;

        JSpinner sizeSpinner, xSpinner, ySpinner;

        JButton setFiltersButton;

        titleField = (JTextField) filtersBox.add(new JTextField());
        clatterOfHoovesNull = (JRadioButton) filtersBox.add(new JRadioButton("NULL", true));
        clatterOfHoovesGroup.add(clatterOfHoovesNull);
        clatterOfHoovesTrue = (JRadioButton) filtersBox.add(new JRadioButton("TRUE", false));
        clatterOfHoovesGroup.add(clatterOfHoovesTrue);
        clatterOfHoovesFalse = (JRadioButton) filtersBox.add(new JRadioButton("FALSE", false));
        clatterOfHoovesGroup.add(clatterOfHoovesFalse);

        sizeSpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(20, 0, 999, 1)));
        xSpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999.0, 1.0)));
        ySpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999.0, 1.0)));

        setFiltersButton = (JButton) filtersBox.add(new JButton("APPLY"));
        setFiltersButton.addActionListener(l -> {
            currentFilters.setTitle(titleField.getText());
            if (clatterOfHoovesTrue.isSelected()) {
                currentFilters.setClatterOfHooves(true);
            } else if (clatterOfHoovesFalse.isSelected()) {
                currentFilters.setClatterOfHooves(false);
            } else {
                currentFilters.setClatterOfHooves(null);
            }
            currentFilters.setSize((Integer) ((SpinnerNumberModel) sizeSpinner.getModel()).getNumber());
            currentFilters.setX((Double) ((SpinnerNumberModel) xSpinner.getModel()).getNumber());
            currentFilters.setY((Double) ((SpinnerNumberModel) ySpinner.getModel()).getNumber());

            drawablePane.setFilters(currentFilters);
        });

        JButton loadButton, startButton, stopButton;

        loadButton = (JButton) actionsBox.add(new JButton("LOAD ITEMS BY FILTERS"));
        startButton = (JButton) actionsBox.add(new JButton("START"));
        stopButton = (JButton) actionsBox.add(new JButton("STOP"));

        stopButton.setEnabled(false);

        loadButton.addActionListener(l -> {
            updateStore(currentFilters);
            drawablePane.setCarts(new ArrayList<>(store.values()));
        });

        startButton.addActionListener(l -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            drawablePane.start();
        });

        stopButton.addActionListener(l -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            drawablePane.stop();
        });

        drawablePane.addTimerEndHandler(() -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        container.add(filtersBox);
        container.add(Box.createVerticalStrut(12));
        container.add(drawablePane);
        container.add(Box.createVerticalStrut(12));
        container.add(actionsBox);

        pack();
        setVisible(true);
    }

    private void updateStore(CartFilters filters) {
        try {
            Message response = Client.getMessage(new Message(MessageType.SHOW_FILTRED, filters), Cart.class);

            store = (Hashtable<String, Cart>) response.getPayload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
