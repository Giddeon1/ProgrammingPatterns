package org.gigi.view;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.Book;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentForm extends JFrame {
    private JButton viewButton;
    private JButton returnBookButton;
    private JButton goBackButton;
    private JButton exitButton;
    private JButton searchButton;
    private JButton borrowBookButton;
    private JLabel searchBookIdLabel;
    private JLabel searchBookLabel;
    private JComboBox<String> typeComboBox;
    private JTextField searchBookTextField;
    private JButton borrowBookIDButton;
    private JTextField borrowBookIDTextField;
    private JButton searchBookButton;
    private JButton returnBookIDButton;
    private JLabel returnBookIdLabel;
    private JTextField returnBookIDTextField;
    private JButton returningBookButton;
    private final LibrarySystemController librarySystemController = LibrarySystemController.getInstance();

    public StudentForm() {
        setTitle("Student - Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null); // Using null layout for precise control

        // Initialize Components
        viewButton = new JButton("View Books");
        returnBookButton = new JButton("Return Books");
        goBackButton = new JButton("Go Back");
        exitButton = new JButton("Exit");
        searchButton = new JButton("Search Books");
        borrowBookButton = new JButton("Borrow Books");

        searchBookLabel = new JLabel("How would you like to search your book?");
        typeComboBox = new JComboBox<>(new String[]{"By Title", "By Author", "By ID"});
        searchBookTextField = new JTextField(15);
        searchBookButton = new JButton("Search Book");

        searchBookIdLabel = new JLabel("Please enter the book ID:");
        borrowBookIDTextField = new JTextField(10);
        borrowBookIDButton = new JButton("Borrow Book");

        returnBookIdLabel = new JLabel("Please enter the book ID:");
        returnBookIDTextField = new JTextField(10);
        returningBookButton = new JButton("Return Book");

        // Position Buttons and Labels
        viewButton.setBounds(20, 20, 150, 30);
        searchButton.setBounds(20, 60, 150, 30);
        borrowBookButton.setBounds(20, 100, 150, 30);
        returnBookButton.setBounds(20, 140, 150, 30);
        goBackButton.setBounds(20, 180, 150, 30);
        exitButton.setBounds(20, 220, 150, 30);

        searchBookLabel.setBounds(200, 60, 300, 30);
        typeComboBox.setBounds(200, 100, 150, 30);
        searchBookTextField.setBounds(360, 100, 150, 30);
        searchBookButton.setBounds(520, 100, 150, 30);

        searchBookIdLabel.setBounds(200, 140, 300, 30);
        borrowBookIDTextField.setBounds(200, 180, 150, 30);
        borrowBookIDButton.setBounds(360, 180, 150, 30);

        returnBookIdLabel.setBounds(200, 140, 300, 30);
        returnBookIDTextField.setBounds(200, 180, 150, 30);
        returningBookButton.setBounds(360, 180, 150, 30);

        // Add Buttons
        add(viewButton);
        add(searchButton);
        add(borrowBookButton);
        add(returnBookButton);
        add(goBackButton);
        add(exitButton);

        // Add Action Listeners
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBooks();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchUI();
            }
        });

        borrowBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBorrowUI();
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReturnUI();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideDynamicComponents();
                new MainForm();
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        borrowBookIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });

        returningBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        setVisible(true);
    }

    private void displayBooks() {
        List<Book> bookList = librarySystemController.getAllBooks();
        if (bookList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available.", "Search Book", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create column headers for the table
        String[] columnNames = {"ID", "Title", "Author", "Genre", "Year Published", "ISBN", "Copies Available", "Availability"};

        // Convert the book list to a 2D array for JTable
        String[][] bookData = new String[bookList.size()][columnNames.length];
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            bookData[i][0] = String.valueOf(book.getIsbn());
            bookData[i][1] = book.getTitle();
            bookData[i][2] = book.getAuthorFName();
            bookData[i][3] = book.getAuthorLName(); //can you make this into genre?
            bookData[i][4] = String.valueOf(book.getYear());
            bookData[i][5] = book.getIsbn();
            bookData[i][6] = String.valueOf(book.getAvailableCopies());
            bookData[i][7] = book.isAvailable() ? "Available" : "Not Available";
        }

        JTable bookTable = new JTable(bookData, columnNames);
        bookTable.setEnabled(false); // Disable editing

        JScrollPane scrollPane = new JScrollPane(bookTable);
        
        JFrame bookListFrame = new JFrame("List of Books");
        bookListFrame.setSize(800, 400);
        bookListFrame.setLocationRelativeTo(null);
        bookListFrame.add(scrollPane);
        bookListFrame.setVisible(true);
    }

    private void showSearchUI() {
        hideDynamicComponents();
        add(searchBookLabel);
        add(typeComboBox);
        add(searchBookTextField);
        add(searchBookButton);
        revalidate();
        repaint();
    }

    private void searchBook() {
        String searchTerm = searchBookTextField.getText();
        String searchType = (String) typeComboBox.getSelectedItem();
        if (searchType.equals("By ID")) {

        }
        JOptionPane.showMessageDialog(this, "Searching for '" + searchTerm + "' by " + searchType, "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBorrowUI() {
        hideDynamicComponents();
        add(searchBookIdLabel);
        add(borrowBookIDTextField);
        add(borrowBookIDButton);
        revalidate();
        repaint();
    }

    private void borrowBook() {
        String bookID = borrowBookIDTextField.getText();
        JOptionPane.showMessageDialog(this, "Book " + bookID + " has been borrowed successfully", "Borrow Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showReturnUI() {
        hideDynamicComponents();
        add(returnBookIdLabel);
        add(returnBookIDTextField);
        add(returningBookButton);
        revalidate();
        repaint();
    }

    private void returnBook() {
        String bookID = returnBookIDTextField.getText();
        JOptionPane.showMessageDialog(this, "Book " + bookID + " has been returned successfully", "Return Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hideDynamicComponents() {
        remove(searchBookLabel);
        remove(typeComboBox);
        remove(searchBookTextField);
        remove(searchBookButton);

        remove(searchBookIdLabel);
        remove(borrowBookIDTextField);
        remove(borrowBookIDButton);

        remove(returnBookIdLabel);
        remove(returnBookIDTextField);
        remove(returningBookButton);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new StudentForm();
    }
}
