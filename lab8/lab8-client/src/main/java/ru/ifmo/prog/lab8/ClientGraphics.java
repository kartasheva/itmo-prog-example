package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import static javax.swing.Box.createHorizontalBox;

public class ClientGraphics extends JFrame {

    private LabLocale labLocale = LabLocale.getInstance();
    private Hashtable<String, Cart> store = null;
    private CartFilters currentFilters = new CartFilters();
    private DrawablePane drawablePane;

    public ClientGraphics() {
        super(LabLocale.getInstance().getString("Client"));
        updateStore(currentFilters);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = getContentPane();

        drawablePane = new DrawablePane(new ArrayList<>(store.values()));
        drawablePane.setFilters(currentFilters);
        setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        Box filtersBox = createHorizontalBox(),
                actionsBox = createHorizontalBox();


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(labLocale.getLocalesMenu());
        setJMenuBar(menuBar);

        JTextField titleField, createdAtField, colorField;
        ButtonGroup clatterOfHoovesGroup = new ButtonGroup();
        JRadioButton clatterOfHoovesTrue,
                clatterOfHoovesFalse,
                clatterOfHoovesNull;

        JSpinner sizeSpinner, xSpinner, ySpinner;

        JButton setFiltersButton;

        titleField = (JTextField) filtersBox.add(new JTextField());
        clatterOfHoovesNull = (JRadioButton) filtersBox.add(new JRadioButton(labLocale.getString("Null"), true));
        clatterOfHoovesGroup.add(clatterOfHoovesNull);
        clatterOfHoovesTrue = (JRadioButton) filtersBox.add(new JRadioButton(labLocale.getString("True"), false));
        clatterOfHoovesGroup.add(clatterOfHoovesTrue);
        clatterOfHoovesFalse = (JRadioButton) filtersBox.add(new JRadioButton(labLocale.getString("False"), false));
        clatterOfHoovesGroup.add(clatterOfHoovesFalse);

        sizeSpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(20, 0, 999, 1)));
        xSpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999.0, 1.0)));
        ySpinner = (JSpinner) filtersBox.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999.0, 1.0)));

        createdAtField = (JTextField) filtersBox.add(new JTextField());
        colorField = (JTextField) filtersBox.add(new JTextField());
        colorField.setEditable(false);
        colorField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LabColor labColor;

                java.awt.Color awtColor = JColorChooser.showDialog(colorField, labLocale.getString("Choose_a_labColor"), null);
                labColor = awtColor != null ? new LabColor(awtColor) : null;
                colorField.setText(labColor != null ? labColor.toString() : "");
                colorField.setBackground(labColor != null ? labColor.toAWTColor() : Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        setFiltersButton = (JButton) filtersBox.add(new JButton(labLocale.getString("Apply")));
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
            if (createdAtField.getText().length() > 0)
                currentFilters.setCreatedAt(new Date(Integer.valueOf(createdAtField.getText())));
            else
                currentFilters.setCreatedAt(null);
            if (colorField.getText().length() > 0)
                currentFilters.setLabColor(new LabColor(colorField.getText()));
            else
                currentFilters.setLabColor(null);
            drawablePane.setFilters(currentFilters);
        });

        JButton loadButton, startButton, stopButton;

        loadButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Load")));
        startButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Start")));
        stopButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Stop")));

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

        labLocale.addLocaleChangeHandler(bundle -> {
            setTitle(bundle.getString("Client"));
            clatterOfHoovesNull.setText(bundle.getString("Null"));
            clatterOfHoovesTrue.setText(bundle.getString("True"));
            clatterOfHoovesFalse.setText(bundle.getString("False"));
            setFiltersButton.setText(bundle.getString("Apply"));
            loadButton.setText(bundle.getString("Load"));
            startButton.setText(bundle.getString("Start"));
            stopButton.setText(bundle.getString("Stop"));
        });

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
