package org.gigi.view;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.Librarian;
import org.gigi.model.Student;
import org.gigi.model.User;
import org.gigi.util.DatabaseUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;

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
    private JComboBox<String> languageComboBox;
    private String loggedInUserEmail;
    private final LibrarySystemController librarySystemController = LibrarySystemController.getInstance();


    public MainForm() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);

        JLabel firstNameLabel = new JLabel("First Name:");

        firstNameTextField = new JTextField();

        JLabel lastNameLabel = new JLabel("Last Name:");

        lastNameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");

        emailTextField = new JTextField();

        String[] userTypes = {"Type of Users","Student", "Librarian"};
        userComboBox = new JComboBox<>(userTypes);


        validateButton = new JButton("Validate");

        languageComboBox = new JComboBox<>(new String[] {"Type of Language","English", "French"});

        // Sets the layout manager
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        //Adds components to the frame
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
        add(languageComboBox);

        /**
         * Adding an actionListener to our Main Form
         * It gets all of the TextField and saves the users
         */
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //handleValidateButtonClick();
                if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
                        ||emailTextField.getText().isEmpty()||
                        userComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(MainForm.this, "Please make sure all fields" +
                            " are not empty and make sure user type is chosen. and make sure email is valid!", "Book display", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    saveUser();

                }
            }
        });
        setVisible(true);
    }

    /**
     * Saves a new user (Student or Librarian) to the system based on input fields
     * and navigates to the appropriate form. Displays a success or error message
     * depending on the operation's outcome.
     */
    private void saveUser() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        loggedInUserEmail = emailTextField.getText();
        String userType = (String) userComboBox.getSelectedItem();
        try {
            if (userType.equals("Student")) {
                librarySystemController.addUser(new Student(firstName, lastName, email));
                User loggedInUser = DatabaseUtil.fetchUserByEmail(loggedInUserEmail);
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                new StudentForm();
                this.dispose();
            } else if (userType.equals("Librarian")) {
                librarySystemController.addUser(new Librarian(firstName,lastName, email));
                User loggedInUser = DatabaseUtil.fetchUserByEmail(loggedInUserEmail);
                JOptionPane.showMessageDialog(this, "Librarian added successfully!");
                new LibrarianForm();
                this.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        //sir, you need to delete it for every run so it won't say oh you created this table before
        DatabaseUtil.DELETE_ALL_TABLES_SQL();
        new MainForm();
    }
}
