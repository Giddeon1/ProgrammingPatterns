package org.gigi.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainForm extends JFrame {
    private JLabel titleLabel;
    private JLabel signInLabel;
    private JTextField lastNameTextField;
    private JTextField emailTextField;
    private JTextField firstNameTextField;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JButton validateButton;
    private JComboBox<String> userComboBox;

    public MainForm() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);


        // Initialize components
        titleLabel = new JLabel("Library Management System");
        signInLabel = new JLabel("Sign in");
        firstNameLabel = new JLabel("First Name");
        lastNameLabel = new JLabel("Last Name");
        emailLabel = new JLabel("Email:");

        firstNameTextField = new JTextField(15);
        lastNameTextField = new JTextField(15);
        emailTextField = new JTextField(15);

        validateButton = new JButton("Validate");

        userComboBox = new JComboBox<>(new String[] {"Librarian", "Student"});

        // Set layout manager
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add components to the frame
        add(titleLabel);
        add(signInLabel);
        add(firstNameLabel);
        add(firstNameTextField);
        add(lastNameLabel);
        add(lastNameTextField);
        add(emailLabel);
        add(emailTextField);
        add(validateButton);
        add(userComboBox);

        // Add ActionListener for the button
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleValidateButtonClick();
            }
        });


        userComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    handleComboBoxSelectionChange();
                }
            }
        });

        setVisible(true);
    }

    // This method should be defined outside of the ActionListener
    private void handleValidateButtonClick() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String selectedRole = (String) userComboBox.getSelectedItem();

        // Perform validation or processing
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Selected Role: " + selectedRole);
    }

    private void handleComboBoxSelectionChange() {
        String selectedRole = (String) userComboBox.getSelectedItem();
        // Do something with the selected role, e.g., update the UI or perform logic
        if ("Librarian".equals(selectedRole)) {
            new LibrarianForm(this);
            this.dispose();
        } else if ("Student".equals(selectedRole)) {
            new StudentForm();
            this.dispose();
        } else {
            System.out.println("No valid role selected.");
        }
    }


    public static void main(String[] args) {
        new MainForm();
    }
}
