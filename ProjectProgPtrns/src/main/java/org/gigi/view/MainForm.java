package org.gigi.view;

import javax.swing.*;

public class MainForm extends JFrame {
    private JComboBox userComboBox;
    private JPanel emailLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;

    public MainForm() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);



    }

    public static void main(String[] args) {
        new MainForm();
    }
}
