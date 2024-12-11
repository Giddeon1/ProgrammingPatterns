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
        //firstNameLabel.setBounds(50, 50, 100, 30);
        //add(firstNameLabel);

        firstNameTextField = new JTextField();
      //  firstNameTextField.setBounds(150, 50, 200, 30);
       // add(firstNameTextField);

        JLabel lastNameLabel = new JLabel("Last Name:");
       // lastNameLabel.setBounds(50, 100, 100, 30);
        //add(lastNameLabel);

        lastNameTextField = new JTextField();
       // lastNameTextField.setBounds(150, 100, 200, 30);
       // add(lastNameTextField);

        JLabel emailLabel = new JLabel("Email:");
       // emailLabel.setBounds(50, 150, 100, 30);
       // add(emailLabel);

        emailTextField = new JTextField();
       // emailTextField.setBounds(150, 150, 200, 30);
       // add(emailTextField);

        String[] userTypes = {"Type of Users","Student", "Librarian"};
        userComboBox = new JComboBox<>(userTypes);


        validateButton = new JButton("Validate");



       /* // Initialize components with localized text  I COMMENTED OUT THE I18N STUFF HERE
        titleLabel = new JLabel(I18NHelper.getString("form.titleLabel"));
        signInLabel = new JLabel(I18NHelper.getString("form.signInLabel"));
        firstNameLabel = new JLabel(I18NHelper.getString("form.firstNameLabel"));
        lastNameLabel = new JLabel(I18NHelper.getString("form.lastNameLabel"));
        emailLabel = new JLabel(I18NHelper.getString("form.emailLabel"));*/

        /*firstNameTextField = new JTextField(15);
        lastNameTextField = new JTextField(15);
        emailTextField = new JTextField(15);*/

       // validateButton = new JButton("Validate");

       /* validateButton = new JButton(I18NHelper.getString("button.validate"));*/

        /*userComboBox = new JComboBox<>(new String[] {"Type of Customer","Librarian", "Student"});*/
        languageComboBox = new JComboBox<>(new String[] {"Type of Language","English", "French"});

        // Set layout manager
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        //Add components to the frame
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

        // Add ActionListener for the button
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


       /* userComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    handleComboBoxSelectionChange();
                }
            }
        });*/

        setVisible(true);
    }


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
   /* private void handleValidateButtonClick() { I COMMENTED OUT THE I18N STUFF HERE
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String selectedRole = (String) userComboBox.getSelectedItem();

        // Perform validation or processing
        JOptionPane.showMessageDialog(this,
                I18NHelper.getString("validation.success") +
                        "\n" + I18NHelper.getString("form.firstNameLabel") + ": " + firstName +
                        "\n" + I18NHelper.getString("form.lastNameLabel") + ": " + lastName +
                        "\n" + I18NHelper.getString("form.emailLabel") + ": " + email +
                        "\n" + I18NHelper.getString("combobox.user") + ": " + selectedRole,
                I18NHelper.getString("form.titleLabel"),
                JOptionPane.INFORMATION_MESSAGE);
    }*/

   /* private void handleComboBoxSelectionChange() {
        String selectedRole = (String) userComboBox.getSelectedItem();
        // Do something with the selected role, e.g., update the UI or perform logic
        if ("Librarian".equals(selectedRole)) {
            new LibrarianForm();
            this.dispose();
        } else if ("Student".equals(selectedRole)) {
            new StudentForm();
            this.dispose();
        } else {
            System.out.println("No valid role selected.");
        }
    }*/

  /*  private void handleComboBoxSelectionChange() { I COMMENTED OUT THE I18N STUFF HERE
        String selectedRole = (String) userComboBox.getSelectedItem();
        if (I18NHelper.getString("user.librarian").equals(selectedRole)) {
            new LibrarianForm();
            this.dispose();
        } else if (I18NHelper.getString("user.student").equals(selectedRole)) {
            new StudentForm();
            this.dispose();
        } else {
            System.out.println(I18NHelper.getString("message.noRole"));
        }
    }*/

    public static class I18NHelper {
        private static ResourceBundle resourceBundle;

        // Initialize with a default locale
        public static void initialize(Locale locale) {
            resourceBundle = ResourceBundle.getBundle("messages.Messages", locale);
        }

        public static String getString(String key) {
            return resourceBundle.getString(key);
        }
    }
    
    public static void main(String[] args) {
        //sir, you need to delete it for every run so it wont say oh you created this table before
        DatabaseUtil.DELETE_ALL_TABLES_SQL();
        /*Locale defaultLocale = Locale.ENGLISH; I COMMENTED OUT THE I18N STUFF HERE
        I18NHelper.initialize(defaultLocale);*/
        new MainForm();
    }
}
