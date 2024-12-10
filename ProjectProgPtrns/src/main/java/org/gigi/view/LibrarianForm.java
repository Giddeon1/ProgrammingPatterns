package org.gigi.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarianForm extends JFrame {
    private JLabel welcomeLabel;
    private JButton manageBookButton;
    private JButton manageUsersButton;
    private JButton returnToLoginButton;

    public LibrarianForm() {
        setTitle("Librarian - Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);

        welcomeLabel = new JLabel("Welcome Librarian!");
        manageBookButton = new JButton("Manage Books");
        manageUsersButton = new JButton("Manage Users");
        returnToLoginButton = new JButton("Return to Login");


        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(welcomeLabel);
        add(manageBookButton);
        add(manageUsersButton);
        add(returnToLoginButton);

        returnToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true); // Show the MainForm
                dispose(); // Close the current form
            }
        });

        setVisible(true);
    }
}
