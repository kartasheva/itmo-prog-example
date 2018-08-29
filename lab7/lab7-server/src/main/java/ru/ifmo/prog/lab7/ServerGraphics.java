package ru.ifmo.prog.lab7;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ServerGraphics extends JFrame {
    private JButton load;
    private JButton save;
    private JButton remove;
    private JButton removeAll;
    private JButton clear;
    private Collection col;
    private Server server;
    private JTextField f1;
    private JPasswordField f2;
    private JLabel login;
    private JLabel password;
    private boolean pw;
    private String l = "user";
    private String p = "user";


    public ServerGraphics() {
        super("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setSize(500, 400);

        pw = false;
        Box box1 = Box.createHorizontalBox();
        login = new JLabel("Login");
        f1 = new JTextField(15);
        box1.add(login);
        box1.add(Box.createHorizontalStrut(6));
        box1.add(f1);

        Box box2 = Box.createHorizontalBox();
        password = new JLabel("Password");
        f2 = new JPasswordField(10);
        box2.add(password);
        box2.add(Box.createHorizontalStrut(6));
        box2.add(f2);

        Box box3 = Box.createHorizontalBox();
        JButton ok = new JButton("OK");
        ActionListener alok = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((f1.getText().equals(l)) && (f2.getPassword().equals(p))) {
                    truePassword();
                }
                else {
                    falsePassword();
                }
            }
        };
        JButton cancel = new JButton("CANCEL");
        ActionListener alcancel = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        box3.add(Box.createHorizontalGlue());
        box3.add(ok);
        box3.add(Box.createHorizontalStrut(12));
        box3.add(cancel);

        login.setPreferredSize(password.getPreferredSize());
        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(12, 12, 12, 12));
        mainBox.add(box1);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box2);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(box3);
        setContentPane(mainBox);
        pack();
        setResizable(false);
    }

    public void truePassword() {
        MyTableModel model = new MyTableModel();


        JTable table = new JTable(model);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        load = new JButton("Load");
        ActionListener al1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.load(server.getCollection(), server.getClient());
            }
        };
        load.addActionListener(al1);
        panel.add(load);

        remove = new JButton("Remove");
        ActionListener al2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.remove(server.getCollection(), server.getClient(), server.getMessage());
            }
        };
        load.addActionListener(al2);
        panel.add(remove);

        removeAll = new JButton("Remove all");
        ActionListener al3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.removeAll(server.getCollection(), server.getClient(), server.getMessage());
            }
        };
        load.addActionListener(al3);
        panel.add(removeAll);

        clear = new JButton("Clear");
        ActionListener al4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.clear(server.getCollection(), server.getClient());
            }
        };
        load.addActionListener(al4);
        panel.add(clear);

        add(panel);
        pack();
    }
    public void falsePassword(){
        JLabel fp = new JLabel("password is false");
//        mainBox.add(fp);
    }
}
//вариант 311363