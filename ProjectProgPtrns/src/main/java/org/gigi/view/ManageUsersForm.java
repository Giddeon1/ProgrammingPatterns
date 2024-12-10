package org.gigi.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageUsersForm extends JFrame {
    private JButton viewUserButton;
    private JButton removeUserButton;
    private JButton removingUserButton;
    private JButton searchUserButton;
    private JButton goBackButton;
    private JButton exitButton;
    private JButton searchingUserButton;
    private JTextField usernameTextField;
    private JTextField userIdTextField;
    private JLabel enterUserIdLabel;
    private JLabel enterUsernameLabel;
    private JLabel formLabel;

    public ManageUsersForm() {
        // Frame setup
        setTitle("Manage Users - Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null); // Using null layout for precise positioning

        // Initialize Buttons
        viewUserButton = new JButton("View User");
        removeUserButton = new JButton("Remove User");
        searchUserButton = new JButton("Search User");
        goBackButton = new JButton("Go Back");
        exitButton = new JButton("Exit");
        searchingUserButton = new JButton("Search");
        removingUserButton = new JButton("Remove");

        // Initialize Labels
        formLabel = new JLabel("Please Enter the Username Below");
        enterUsernameLabel = new JLabel("Username:");
        enterUserIdLabel = new JLabel("User ID:"); // Not used in this case but can be used in remove functionality

        // Initialize TextFields
        usernameTextField = new JTextField(20);
        userIdTextField = new JTextField(20); // Not used here but initialized for completeness

        // Set bounds for Buttons
        viewUserButton.setBounds(20, 20, 150, 30);
        searchUserButton.setBounds(20, 60, 150, 30);
        removeUserButton.setBounds(20, 100, 150, 30);
        goBackButton.setBounds(20, 140, 150, 30);
        exitButton.setBounds(20, 180, 150, 30);
        searchingUserButton.setBounds(350, 140, 150, 30);
        removingUserButton.setBounds(350, 140, 150, 30);

        // Set bounds for Labels and TextFields
        formLabel.setBounds(200, 20, 300, 30);
        enterUsernameLabel.setBounds(200, 100, 100, 30);
        usernameTextField.setBounds(300, 100, 200, 30);
        enterUserIdLabel.setBounds(200, 100, 100, 30);
        userIdTextField.setBounds(300, 100, 200, 30);

        // Add static buttons to frame
        add(viewUserButton);
        add(searchUserButton);
        add(removeUserButton);
        add(goBackButton);
        add(exitButton);
        add(formLabel);
        add(enterUsernameLabel);
        add(usernameTextField);
        add(enterUserIdLabel);
        add(userIdTextField);
        add(searchingUserButton);
        add(removingUserButton);

        formLabel.setVisible(false);
        enterUsernameLabel.setVisible(false);
        usernameTextField.setVisible(false);
        enterUserIdLabel.setVisible(false);
        userIdTextField.setVisible(false);
        searchingUserButton.setVisible(false);
        removingUserButton.setVisible(false);


        // Add ActionListener for "Search User" button
        searchUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchUserUI();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new LibrarianForm(); // Assuming LibrarianForm is the previous screen
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemoveUserUI();
            }
        });

        searchingUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUser();
            }
        });

        removingUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });

        setVisible(true);
    }

    private void showSearchUserUI() {
        // Make static buttons visible

        formLabel.setText("Please Enter the Username Below");
        formLabel.setVisible(true);
        enterUsernameLabel.setVisible(true);
        usernameTextField.setVisible(true);
        searchingUserButton.setVisible(true);


        // Show only the components needed for the "Search User" form
        formLabel.setVisible(true);
        enterUsernameLabel.setVisible(true);
        usernameTextField.setVisible(true);
        searchingUserButton.setVisible(true);

        // Hide components for "Remove User"
        enterUserIdLabel.setVisible(false);
        userIdTextField.setVisible(false);
        removingUserButton.setVisible(false);


        if (!usernameTextField.isVisible()) {
            usernameTextField.setText(usernameTextField.getText());
        }


        revalidate();
        repaint();
    }


    private void searchUser() {
        String username = usernameTextField.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Replace this with actual search logic (e.g., database query)
            JOptionPane.showMessageDialog(this, "User " + username + " found successfully!", "Search User", JOptionPane.INFORMATION_MESSAGE);
            usernameTextField.setText(""); // Clear the text field after search
        }
    }

    private void removeDynamicComponentsExceptStatic() {
        // Remove only dynamic components
        remove(formLabel);
        remove(enterUsernameLabel);
        remove(usernameTextField);
        remove(searchingUserButton);
    }

    private void showRemoveUserUI() {
        // Show only the components needed for the "Remove User" form
        formLabel.setText("Please Enter the User ID Below");
        formLabel.setVisible(true);
        enterUserIdLabel.setVisible(true);
        userIdTextField.setVisible(true);
        removingUserButton.setVisible(true);

        // Hide components for "Search User"
        enterUsernameLabel.setVisible(false);
        usernameTextField.setVisible(false);
        searchingUserButton.setVisible(false);


        if (!userIdTextField.isVisible()) {
            userIdTextField.setText(userIdTextField.getText());
        }

        revalidate();
        repaint();
    }

    private void removeUser() {
        String userId = userIdTextField.getText();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Replace this with actual remove logic (e.g., database update)
            JOptionPane.showMessageDialog(this, "Removed user with ID " + userId + " successfully!", "Remove User", JOptionPane.INFORMATION_MESSAGE);
            userIdTextField.setText(""); // Clear the text field after removal
        }
    }

    public static void main(String[] args) {
        new ManageUsersForm();
    }
}
