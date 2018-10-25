package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

import static javax.swing.Box.*;

public class ServerGraphics extends JFrame {
    private Server server;
    private Collection<Cart> collection;

    private JTable collectionTable;

    public static void normalizeLocation(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = window.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        window.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public ServerGraphics(Server server, Collection<Cart> collection) {
        super("Server");

        this.server = server;
        this.collection = collection;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        prepareAuthorizationForm();

        this.server.addResponseHandler((message, client) -> {
            if (collectionTable != null) {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        setVisible(true);
    }

    private void prepareAuthorizationForm() {
        Container container = getContentPane();
        container.removeAll();
        setJMenuBar(null);

        setResizable(false);

        Box loginBox = createHorizontalBox(),
                passwordBox = createHorizontalBox(),
                actionsBox = createHorizontalBox();

        JTextField loginField;
        JPasswordField passwordField;

        JLabel loginLabel, passwordLabel;

        JButton submitButton, cancelButton;

        loginLabel = (JLabel) loginBox.add(new JLabel("login:"));
        loginBox.add(createHorizontalStrut(6));
        loginField = (JTextField) loginBox.add(new JTextField("user", 15));

        passwordLabel = (JLabel) passwordBox.add(new JLabel("password:"));
        passwordBox.add(createHorizontalStrut(6));
        passwordField = (JPasswordField) passwordBox.add(new JPasswordField("user", 10));

        loginLabel.setPreferredSize(passwordLabel.getPreferredSize());

        submitButton = (JButton) actionsBox.add(new JButton("OK"));
        cancelButton = (JButton) actionsBox.add(new JButton("CANCEL"));

        submitButton.addActionListener(event -> {
            if (login(loginField.getText(), passwordField.getPassword())) {
                preparePage();
            } else {
                JOptionPane.showMessageDialog(this,"Incorrect login or password",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(event -> System.exit(0));

        container.add(loginBox);
        container.add(Box.createVerticalStrut(12));
        container.add(passwordBox);
        container.add(Box.createVerticalStrut(17));
        container.add(actionsBox);

        pack();
        validate();
        repaint();
        normalizeLocation(this);
    }

    private void preparePage() {
        Container container = getContentPane();
        container.removeAll();
        JMenuBar menuBar = new JMenuBar();
        JMenu actions = menuBar.add(new JMenu("ACTIONS"));
        JMenuItem collectionLoadButton = actions.add(new JMenuItem("LOAD"));
        JMenuItem collectionSaveButton = actions.add(new JMenuItem("SAVE"));

        collectionLoadButton.addActionListener(event -> {
            try {
                collection.load();
            } catch (IOException e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });
        collectionSaveButton.addActionListener(event -> {
            try {
                collection.save();
            } catch (IOException e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        setJMenuBar(menuBar);

        setResizable(true);

        Box actionsBox = createHorizontalBox(), tableBox = createVerticalBox();

        JButton addButton, removeButton, removeAllButton, clearButton, exitButton;

        addButton = (JButton) actionsBox.add(new JButton("ADD ITEM"));
        removeButton = (JButton) actionsBox.add(new JButton("REMOVE SELECTED ITEM"));
        removeAllButton = (JButton) actionsBox.add(new JButton("REMOVE ALL ITEM LIKE EMPTY"));
        clearButton = (JButton) actionsBox.add(new JButton("CLEAR"));
        exitButton = (JButton) actionsBox.add(new JButton("EXIT"));

        addButton.addActionListener(event -> {
            try {
                collection.add(collection.sequence(), collection.getItemType().newInstance() );
            } catch (InstantiationException | IllegalAccessException e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        removeButton.addActionListener(removeButtonEvent -> {
            JDialog removeItemKeyDialog = new JDialog(this,"REMOVE ITEM BY KEY", Dialog.ModalityType.DOCUMENT_MODAL);
            removeItemKeyDialog.setResizable(false);
            removeItemKeyDialog.setLayout(new BoxLayout(removeItemKeyDialog.getContentPane(), BoxLayout.Y_AXIS));

            Box keyBox = createHorizontalBox(),
                    dialogActionsBox = createHorizontalBox();

            JTextField keyField;

            JLabel keyLabel;

            JButton submitButton, cancelButton;

            keyLabel = (JLabel) keyBox.add(new JLabel("KEY:"));
            keyBox.add(createHorizontalStrut(6));
            keyField = (JTextField) keyBox.add(
                    new JTextField(collectionTable.getSelectedRow() >= 0
                            ? String.valueOf(collectionTable.getModel().getValueAt(collectionTable.getSelectedRow(), 0))
                            : "",
                            15));

            submitButton = (JButton) dialogActionsBox.add(new JButton("REMOVE ITEM"));
            cancelButton = (JButton) dialogActionsBox.add(new JButton("CANCEL"));

            submitButton.addActionListener(event -> {
                try {
                    collection.remove(keyField.getText());
                } catch (Exception e) {
                    OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
                } finally {
                    removeItemKeyDialog.dispose();
                    ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
                }
            });
            cancelButton.addActionListener(event -> removeItemKeyDialog.dispose());

            removeItemKeyDialog.add(keyBox);
            removeItemKeyDialog.add(Box.createVerticalStrut(17));
            removeItemKeyDialog.add(dialogActionsBox);

            removeItemKeyDialog.pack();
            removeItemKeyDialog.setVisible(true);
            normalizeLocation(removeItemKeyDialog);
        });

        removeAllButton.addActionListener(event -> {
            try {
                collection.removeAll(new Cart());
            } catch (Exception e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        clearButton.addActionListener(event -> {
            try {
                collection.clear();
            } catch (Exception e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        exitButton.addActionListener(e -> prepareAuthorizationForm());

        collectionTable = new JTable(new CollectionTableModel(collection));
        collectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        collectionTable.setDefaultEditor(Date.class, new CollectionTableDateCellEditor());
        collectionTable.setDefaultEditor(LabColor.class, new CollectionTableColorCellEditor());
        collectionTable.setDefaultRenderer(LabColor.class, new CollectionTableColorCellRenderer());
        collectionTable.getModel().addTableModelListener(event -> collectionTable.repaint());
        JScrollPane collectionScrollPane = new JScrollPane(collectionTable);
        tableBox.add(collectionScrollPane);

        container.add(actionsBox);
        container.add(tableBox);

        pack();
        container.validate();
        container.repaint();
    }

    private static boolean login(String login, char[] password) {
        String _login = "user", _password = "user";

        return login.equals(_login) && String.valueOf(password).equals(_password);
    }
}

//вариант 311363