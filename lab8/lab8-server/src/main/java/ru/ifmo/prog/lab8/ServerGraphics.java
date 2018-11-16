package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.OffsetDateTime;

import static javax.swing.Box.*;

public class ServerGraphics extends JFrame {
    private Server server;
    private Collection<Cart> collection;

    private JTable collectionTable;

    private LabLocale labLocale = LabLocale.getInstance();

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
        super(LabLocale.getInstance().getString("Server"));

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

        labLocale.addLocaleChangeHandler(bundle -> setTitle(bundle.getString("Server")));

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

        loginLabel = (JLabel) loginBox.add(new JLabel(labLocale.getString("Login")));
        loginBox.add(createHorizontalStrut(6));
        loginField = (JTextField) loginBox.add(new JTextField("user", 15));

        passwordLabel = (JLabel) passwordBox.add(new JLabel(labLocale.getString("Password")));
        passwordBox.add(createHorizontalStrut(6));
        passwordField = (JPasswordField) passwordBox.add(new JPasswordField("user", 10));

        loginLabel.setPreferredSize(passwordLabel.getPreferredSize());

        submitButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Ok")));
        cancelButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Cancel")));

        labLocale.addLocaleChangeHandler(bundle -> {
            if (loginLabel != null)
                loginLabel.setText(bundle.getString("Login"));
            if (passwordLabel != null)
                passwordLabel.setText(bundle.getString("Password"));
            if (submitButton != null)
                submitButton.setText(bundle.getString("Ok"));
            if (cancelButton != null)
                cancelButton.setText(bundle.getString("Cancel"));
        });

        submitButton.addActionListener(event -> {
            if (login(loginField.getText(), passwordField.getPassword())) {
                preparePage();
            } else {
                JOptionPane.showMessageDialog(this,labLocale.getString("Incorrect_login_or_password"),
                        labLocale.getString("Error"), JOptionPane.ERROR_MESSAGE);
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
        JMenu actions = menuBar.add(new JMenu(labLocale.getString("Actions")));
        JMenuItem collectionLoadButton = actions.add(new JMenuItem(labLocale.getString("Load")));
        JMenuItem collectionSaveButton = actions.add(new JMenuItem(labLocale.getString("Save")));

        menuBar.add(labLocale.getLocalesMenu());

        setJMenuBar(menuBar);

        collectionLoadButton.addActionListener(event -> {
            try {
                collection.load();
            } catch (SQLException e) {
                OutputHelper.print(Thread.currentThread().getName(), e.getMessage());
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });
        collectionSaveButton.addActionListener(event -> {
            try {
                collection.save();
            } finally {
                ((CollectionTableModel) collectionTable.getModel()).fireTableDataChanged();
            }
        });

        setJMenuBar(menuBar);

        setResizable(true);

        Box actionsBox = createHorizontalBox(), tableBox = createVerticalBox();

        JButton addButton, removeButton, removeAllButton, clearButton, exitButton;

        addButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Add_item")));
        removeButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Remove_selected_item")));
        removeAllButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Remove_all_items_like_empty")));
        clearButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Clear")));
        exitButton = (JButton) actionsBox.add(new JButton(labLocale.getString("Exit")));

        labLocale.addLocaleChangeHandler(bundle -> {
            if (actions != null)
                actions.setText(bundle.getString("Actions"));
            if (collectionLoadButton != null)
                collectionLoadButton.setText(bundle.getString("Load"));
            if (collectionSaveButton != null)
                collectionSaveButton.setText(bundle.getString("Save"));
            if (addButton != null)
                addButton.setText(bundle.getString("Add_item"));
            if (removeButton != null)
                removeButton.setText(bundle.getString("Remove_selected_item"));
            if (removeAllButton != null)
                removeAllButton.setText(bundle.getString("Remove_all_items_like_empty"));
            if (clearButton != null)
                clearButton.setText(bundle.getString("Clear"));
            if (exitButton != null)
                exitButton.setText(bundle.getString("Exit"));
            if (collectionTable != null) {
                collectionTable.repaint();
            }
        });

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
            JDialog removeItemKeyDialog = new JDialog(this,labLocale.getString("Remove_item_by_key"), Dialog.ModalityType.DOCUMENT_MODAL);
            removeItemKeyDialog.setResizable(false);
            removeItemKeyDialog.setLayout(new BoxLayout(removeItemKeyDialog.getContentPane(), BoxLayout.Y_AXIS));

            Box keyBox = createHorizontalBox(),
                    dialogActionsBox = createHorizontalBox();

            JTextField keyField;

            JLabel keyLabel;

            JButton submitButton, cancelButton;

            keyLabel = (JLabel) keyBox.add(new JLabel(labLocale.getString("Key")));
            keyBox.add(createHorizontalStrut(6));
            keyField = (JTextField) keyBox.add(
                    new JTextField(collectionTable.getSelectedRow() >= 0
                            ? String.valueOf(collectionTable.getModel().getValueAt(collectionTable.getSelectedRow(), 0))
                            : "",
                            15));

            submitButton = (JButton) dialogActionsBox.add(new JButton(labLocale.getString("Remove_item")));
            cancelButton = (JButton) dialogActionsBox.add(new JButton(labLocale.getString("Cancel")));

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
        collectionTable.setDefaultEditor(OffsetDateTime.class, new CollectionTableDateCellEditor());
        collectionTable.setDefaultEditor(LabColor.class, new CollectionTableColorCellEditor());
        collectionTable.setDefaultRenderer(LabColor.class, (table, value, isSelected, hasFocus, row, column) -> {
            LabColor labColor = (LabColor) value;
            if (labColor != null) {
                JLabel colorLabel = new JLabel(labColor.toString());
                colorLabel.setOpaque(true);
                colorLabel.setBackground(labColor.toAWTColor());

                return colorLabel;
            }

            return null;
        });
        collectionTable.setDefaultRenderer(Double.class, (table, value, isSelected, hasFocus, row, column) -> {
            Double number = (Double) value;
            if (number != null) {
                JLabel numberLabel = new JLabel(NumberFormat
                        .getNumberInstance(labLocale.getLocale()).format(number));
//                numberLabel.setOpaque(true);

                return numberLabel;
            }

            return null;
        });
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