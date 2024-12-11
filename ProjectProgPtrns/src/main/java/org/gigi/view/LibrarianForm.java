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

        //Sets a layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(welcomeLabel);
        add(manageBookButton);
        add(manageUsersButton);
        add(returnToLoginButton);

        /**
         * ActionListener for to the returnToLoginButton that goes back to the Login Form
         */
        returnToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current form
                new MainForm();
            }
        });

        /**
         * ActionListener to Manage All Books and leads to a new Form (ManageBooksForm)
         */
        manageBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the MainForm
                dispose(); // Close the current form
                new ManageBooksForm();
            }
        });
        /**
         * ActionListener to Manage All Users and leads to a new Form (ManageUsersForm)
         */
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the MainForm
                dispose(); // Close the current form
                new ManageUsersForm();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LibrarianForm();
    }
}

