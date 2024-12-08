package org.gigi.view;

import org.gigi.Main;

import javax.swing.*;

public class MainForm extends JFrame {
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
