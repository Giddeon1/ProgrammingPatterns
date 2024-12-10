package org.gigi.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageBooksForm extends JFrame {
    private JButton searchBookButton;
    private JButton deleteButton;
    private JButton addBookButton;
    private JButton goBackButton;
    private JButton exitButton;
    private JTextField deleteBookTextField;
    private JButton deleteBookButton;
    private JButton addingBookButton;
    private JTextField bookTextField;
    private JTextField genreTextField;
    private JTextField yearsPublishedTextField;
    private JTextField isbnTextField;
    private JTextField copiesTextField;
    private JTextField availabilityTextField;
    private JLabel formLabel;
    private JLabel titleBookLabel;
    private JLabel genreLabel;
    private JLabel yearsPublishedLabel;
    private JLabel isbnLabel;
    private JLabel bookIdLabel;
    private JLabel copiesAvailableLabel;
    private JLabel availabilityLabel;
    private JTextField authorTextField;
    private JLabel authorLabel;

    public ManageBooksForm() {
        setTitle("Manage Books - Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null); // Using null layout for precise control

        // Initialize Buttons
        searchBookButton = new JButton("Search Book");
        deleteButton = new JButton("Delete Book");
        addBookButton = new JButton("Add Book");
        goBackButton = new JButton("Go Back");
        exitButton = new JButton("Exit");
        addingBookButton = new JButton("Add Book");

        // Initialize Labels
        formLabel = new JLabel("Add Book Form");
        titleBookLabel = new JLabel("Title:");
        authorLabel = new JLabel("Author:");
        genreLabel = new JLabel("Genre:");
        yearsPublishedLabel = new JLabel("Year Published:");
        isbnLabel = new JLabel("ISBN:");
        copiesAvailableLabel = new JLabel("Copies Available:");
        availabilityLabel = new JLabel("Availability:");

        // Initialize TextFields
        bookTextField = new JTextField(20);
        authorTextField = new JTextField(20);
        genreTextField = new JTextField(20);
        yearsPublishedTextField = new JTextField(20);
        isbnTextField = new JTextField(20);
        copiesTextField = new JTextField(20);
        availabilityTextField = new JTextField(20);

        // Set Bounds for Buttons
        searchBookButton.setBounds(20, 20, 150, 30);
        addBookButton.setBounds(20, 60, 150, 30);
        deleteButton.setBounds(20, 100, 150, 30);
        goBackButton.setBounds(20, 140, 150, 30);
        exitButton.setBounds(20, 180, 150, 30);
        addingBookButton.setBounds(200, 350, 150, 30);

        // Set Bounds for Form Components
        formLabel.setBounds(200, 20, 200, 30);
        titleBookLabel.setBounds(200, 60, 150, 30);
        bookTextField.setBounds(350, 60, 200, 30);
        authorLabel.setBounds(200, 100, 150, 30);
        authorTextField.setBounds(350, 100, 200, 30);
        genreLabel.setBounds(200, 140, 150, 30);
        genreTextField.setBounds(350, 140, 200, 30);
        yearsPublishedLabel.setBounds(200, 180, 150, 30);
        yearsPublishedTextField.setBounds(350, 180, 200, 30);
        isbnLabel.setBounds(200, 220, 150, 30);
        isbnTextField.setBounds(350, 220, 200, 30);
        copiesAvailableLabel.setBounds(200, 260, 150, 30);
        copiesTextField.setBounds(350, 260, 200, 30);
        availabilityLabel.setBounds(200, 300, 150, 30);
        availabilityTextField.setBounds(350, 300, 200, 30);

        // Add Static Buttons
        add(searchBookButton);
        add(addBookButton);
        add(deleteButton);
        add(goBackButton);
        add(exitButton);

        // Add Action Listeners
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookUI();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for going back (e.g., navigate to previous form)
                dispose(); // Close current window
                new LibrarianForm(); // Assuming LoginForm is the previous screen
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit application
            }
        });

        addingBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        setVisible(true);
    }

    private void showAddBookUI() {
        // Add dynamic components for the "Add Book" form
        add(formLabel);
        add(titleBookLabel);
        add(bookTextField);
        add(authorLabel);
        add(authorTextField);
        add(genreLabel);
        add(genreTextField);
        add(yearsPublishedLabel);
        add(yearsPublishedTextField);
        add(isbnLabel);
        add(isbnTextField);
        add(copiesAvailableLabel);
        add(copiesTextField);
        add(availabilityLabel);
        add(availabilityTextField);
        add(addingBookButton);

        revalidate();
        repaint();
    }

    private void addBook() {
        // Mock logic for adding a book
        String title = bookTextField.getText();
        String author = authorTextField.getText();
        String genre = genreTextField.getText();
        String year = yearsPublishedTextField.getText();
        String isbn = isbnTextField.getText();
        String copies = copiesTextField.getText();
        String availability = availabilityTextField.getText();

        JOptionPane.showMessageDialog(this, "Book added successfully:\nTitle: " + title + "\nAuthor: " + author, "Add Book", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new ManageBooksForm();
    }
}
