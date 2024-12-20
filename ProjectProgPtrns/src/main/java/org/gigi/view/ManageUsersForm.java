package org.gigi.view;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.Student;
import org.gigi.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

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
    private final LibrarySystemController librarySystemController = LibrarySystemController.getInstance();

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

        // Sets bounds for Buttons
        viewUserButton.setBounds(20, 20, 150, 30);
        searchUserButton.setBounds(20, 60, 150, 30);
        removeUserButton.setBounds(20, 100, 150, 30);
        goBackButton.setBounds(20, 140, 150, 30);
        exitButton.setBounds(20, 180, 150, 30);
        searchingUserButton.setBounds(350, 140, 150, 30);
        removingUserButton.setBounds(350, 140, 150, 30);

        // Sets bounds for Labels and TextFields
        formLabel.setBounds(200, 20, 300, 30);
        enterUsernameLabel.setBounds(200, 100, 100, 30);
        usernameTextField.setBounds(300, 100, 200, 30);
        enterUserIdLabel.setBounds(200, 100, 100, 30);
        userIdTextField.setBounds(300, 100, 200, 30);

        // Adding static buttons to frame
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

        /**
         * Viewing all Users
         */
        viewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllUsers();
            }
        });

        /**
         * Searching a user thanks to their username
         */
        searchUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchUserUI();
            }
        });

        /**
         * Goes back to the LibrarianForm
         */
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LibrarianForm();
            }
        });

        /**
         * Exits out the application
         */
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        /**
         * removing a user by putting in their ID
         */
        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemoveUserUI();
            }
        });

        /**
         * Searching a user
         */
        searchingUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUsers();
            }
        });

        /**
         * Confirms the Deletion of the user
         */
        removingUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });

        setVisible(true);
    }

    /**
     * Displays a list of all users (Students and Librarians) in a tabular format.
     * Shows a message if no users are found.
     */
    private void viewAllUsers() {
        List<User> users = librarySystemController.getAllUser();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.", "View Users", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Role"};
        String[][] userData = new String[users.size()][columnNames.length];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userData[i][0] = String.valueOf(user.getUserId());
            userData[i][1] = user.getFirstName();
            userData[i][2] = user.getLastName();
            userData[i][3] = user.getEmail();
            if (user instanceof Student) {
                userData[i][4] = "Student";
            } else {
                userData[i][4] = "Librarian";
            }
        }

        JTable userTable = new JTable(userData, columnNames);
        userTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(userTable);

        JFrame userListFrame = new JFrame("List of Users");
        userListFrame.setSize(600, 300);
        userListFrame.setLocationRelativeTo(null);
        userListFrame.add(scrollPane);
        userListFrame.setVisible(true);

    }

    /**
     * Displays the user interface for searching users by username.
     * Hides components related to removing users and shows search-specific fields.
     */
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

    /**
     * Searches for users based on a keyword entered in the username text field.
     * Displays search results in a table. Shows a message if no users are found.
     */
    private void searchUsers() {
        String keyword = usernameTextField.getText();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search keyword.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<User> users = librarySystemController.searchUsers(keyword);
            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No users found for the search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Role"};
            String[][] userData = new String[users.size()][columnNames.length];

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                userData[i][0] = String.valueOf(user.getUserId());
                userData[i][1] = user.getFirstName();
                userData[i][2] = user.getLastName();
                userData[i][3] = user.getEmail();
                if (user instanceof Student) {
                    userData[i][4] = "Student";
                } else {
                    userData[i][4] = "Librarian";
                }

            }

            JTable userTable = new JTable(userData, columnNames);
            userTable.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(userTable);

            JFrame searchResultsFrame = new JFrame("Search Results");
            searchResultsFrame.setSize(600, 300);
            searchResultsFrame.setLocationRelativeTo(null);
            searchResultsFrame.add(scrollPane);
            searchResultsFrame.setVisible(true);

        } catch (HeadlessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the user interface for removing a user by their ID.
     * Hides components related to searching users and shows remove-specific fields.
     */
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

    /**
     * Removes a user from the system based on the provided user ID.
     * Handles errors for invalid or non-numeric IDs and shows appropriate messages.
     */
    private void removeUser() {
        String userIdStr = userIdTextField.getText();
        if (userIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            librarySystemController.removeUser(Integer.parseInt(userIdTextField.getText()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error removing user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid User ID. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            userIdTextField.setText("");
        }
    }

    public static void main(String[] args) {
        new ManageUsersForm();
    }
}
