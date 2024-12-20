package org.gigi.view;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.Book;
import org.gigi.model.RegularBook;
import org.gigi.util.DatabaseUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
    private JTextField bookIDTextField;
    private final LibrarySystemController librarySystemController = LibrarySystemController.getInstance();

    public ManageBooksForm() {
        setTitle("Manage Books - Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        // Initializing Buttons
        searchBookButton = new JButton("Search Book");
        deleteButton = new JButton("Delete Book");
        addBookButton = new JButton("Add Book");
        goBackButton = new JButton("Go Back");
        exitButton = new JButton("Exit");
        addingBookButton = new JButton("Add Book");

        // Initializing Labels
        formLabel = new JLabel("Please Fill in the Form Below");
        titleBookLabel = new JLabel("Title:");
        authorLabel = new JLabel("Author First Name:");
        //authorLabel2 = new JLabel("Author Last Name:");
        genreLabel = new JLabel("Genre:");
        yearsPublishedLabel = new JLabel("Year Published:");
        isbnLabel = new JLabel("ISBN:");
        copiesAvailableLabel = new JLabel("Copies Available:");
        availabilityLabel = new JLabel("Availability:");
        bookIdLabel = new JLabel("Book ID:");

        // Initializing TextFields
        bookTextField = new JTextField(20);
        authorTextField = new JTextField(20);
        genreTextField = new JTextField(20);
        yearsPublishedTextField = new JTextField(20);
        isbnTextField = new JTextField(20);
        copiesTextField = new JTextField(20);
        availabilityTextField = new JTextField(20);
        bookIDTextField = new JTextField(20);

        // Set Bounds for Buttons
        searchBookButton.setBounds(20, 20, 150, 30);
        addBookButton.setBounds(20, 60, 150, 30);
        deleteButton.setBounds(20, 100, 150, 30);
        goBackButton.setBounds(20, 140, 150, 30);
        exitButton.setBounds(20, 180, 150, 30);
        addingBookButton.setBounds(200, 350, 150, 30);
        deleteBookButton.setBounds(200, 140, 150, 30);

        // Setting Bounds for Form Components
        formLabel.setBounds(200, 20, 200, 30);
        titleBookLabel.setBounds(200, 60, 150, 30);
        bookTextField.setBounds(350, 60, 200, 30);
        authorLabel.setBounds(200, 100, 150, 30);


        authorTextField.setBounds(350, 100, 200, 30);
        genreLabel.setBounds(200, 180, 150, 30);
        genreTextField.setBounds(350, 140, 200, 30);
        yearsPublishedLabel.setBounds(200, 180, 150, 30);
        yearsPublishedTextField.setBounds(350, 180, 200, 30);
        isbnLabel.setBounds(200, 220, 150, 30);
        isbnTextField.setBounds(350, 220, 200, 30);
        copiesAvailableLabel.setBounds(200, 260, 150, 30);
        copiesTextField.setBounds(350, 260, 200, 30);
        availabilityLabel.setBounds(200, 300, 150, 30);
        availabilityTextField.setBounds(350, 300, 200, 30);
        bookIdLabel.setBounds(200, 60, 150, 30);
        bookIDTextField.setBounds(350, 60, 200, 30);

        // Adding Static Buttons
        add(searchBookButton);
        add(addBookButton);
        add(deleteButton);
        add(goBackButton);
        add(exitButton);

        /**
         * Searching Books and displays a list of all books
         */
        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchBookUI();
            }
        });

        /**
         * Adding a book to the list of Books
         */
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookUI();
            }
        });

        /**
         * Goes back to the Librarian Form
         */
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LibrarianForm();
            }
        });

        /**
         * Exits out the Application
         */
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit application
            }
        });

        /**
         * Adds the book to the database and list of all Books
         */
        addingBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        /**
         * Deletes a book by putting the ISBN
         */
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteBookUI();
            }
        });

        /**
         * Confirms the deletion of the book
         */
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        setVisible(true);
    }

    /**
     * Displays a list of all books in a tabular format.
     * Shows a message if no books are available.
     */
    private void showSearchBookUI() {
        List<Book> bookList = librarySystemController.getAllBooks();
        if (bookList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available.", "Search Book", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Creates column headers for the table
        String[] columnNames = {"ID", "Title", "Author", "Genre", "Year Published", "ISBN", "Copies Available", "Availability"};

        // Converts the book of list to a 2D array for JTable
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

        // Creates a JTable with book data
        JTable bookTable = new JTable(bookData, columnNames);
        bookTable.setEnabled(false); // Disable editing

        // Adding the table to a JScrollPane for better usability
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Creates a new frame to display the books
        JFrame bookListFrame = new JFrame("List of Books");
        bookListFrame.setSize(800, 400);
        bookListFrame.setLocationRelativeTo(null);
        bookListFrame.add(scrollPane);
        bookListFrame.setVisible(true);

    }

    /**
     * Displays the user interface for adding a new book to the system.
     * Includes fields for title, author, genre, year, ISBN, and availability.
     */
    private void showAddBookUI() {
        // Add dynamic components for the "Add Book" form
       add(formLabel);
        add(titleBookLabel);
        add(bookTextField);
        add(authorLabel);
        //add(authorLabel2);
        add(authorTextField);
        //add(authorTextField2);
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
        remove(deleteBookButton);
        remove(bookIdLabel);

        revalidate();
        repaint();
    }

    /**
     * Adds a new book to the library system.
     * Validates input fields and clears them after successful addition.
     * Shows appropriate error messages for invalid input.
     */
    private void addBook() {
        // Mock logic for adding a book
        String title = bookTextField.getText();
        String authorFName = authorTextField.getText();
        //String authorLName = authorTextField2.getText();
        String genre = genreTextField.getText();
        String yearStr = yearsPublishedTextField.getText();
        String isbn = isbnTextField.getText();
        String copiesStr = copiesTextField.getText();
        String availabilityStr = availabilityTextField.getText();

        JOptionPane.showMessageDialog(this, "Book added successfully:\nTitle: " + title + "\nAuthor: " + authorFName+" "+genre, "Add Book", JOptionPane.INFORMATION_MESSAGE);

        if (title.isEmpty() || authorFName.isEmpty() ||genre.isEmpty() || isbn.isEmpty() || yearStr.isEmpty() || copiesStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            int copies = Integer.parseInt(copiesStr);
            boolean availability = availabilityStr.equalsIgnoreCase("yes") || availabilityStr.equalsIgnoreCase("true");

            // Inserting a book into the database
            librarySystemController.addBook(new RegularBook(isbn, title, authorFName, genre, year, copies));

            // Clearing the text fields after successful insertion
            bookTextField.setText("");
            authorTextField.setText("");
            genreTextField.setText("");
            yearsPublishedTextField.setText("");
            isbnTextField.setText("");
            copiesTextField.setText("");
            availabilityTextField.setText("");

            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year and Copies must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the user interface for deleting a book using its ISBN.
     * Removes components unrelated to book deletion.
     */
    private void showDeleteBookUI() {
        // Remove all dynamically added components first
        removeDynamicComponents();

        // Adding components specific to "Delete Book"
        add(formLabel);
        add(bookIdLabel);
        add(bookIDTextField);
        add(deleteBookButton);

        revalidate();
        repaint();
    }

    /**
     * Removes all dynamically added components from the user interface.
     * Prepares the UI for the next action or screen.
     */
    private void removeDynamicComponents() {
        // Removing all components for both Add and Delete book functionality
        remove(formLabel);
        remove(bookIdLabel);
        remove(bookIDTextField);
        remove(deleteBookButton);
        // Removing Add Book components (in case they are present)
        remove(titleBookLabel);
        remove(bookTextField);
        remove(authorLabel);
        remove(authorTextField);
        remove(genreLabel);
        remove(genreTextField);
        remove(yearsPublishedLabel);
        remove(yearsPublishedTextField);
        remove(isbnLabel);
        remove(isbnTextField);
        remove(copiesAvailableLabel);
        remove(copiesTextField);
        remove(availabilityLabel);
        remove(availabilityTextField);
        remove(addingBookButton);
    }

    /**
     * Deletes a book from the library system using its ISBN.
     * Validates input and shows a confirmation or error message.
     */
    private void deleteBook() {
        String isbn = bookIDTextField.getText();

        if (!isbn.isEmpty()) {
            librarySystemController.removeBook(isbn);
            JOptionPane.showMessageDialog(this, "Book with ISBN " + isbn + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input. Enter either a numeric ID or a valid ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        bookIDTextField.setText(""); // Clears the ID field
        isbnTextField.setText("");  // Clears the ISBN field
    }

    public static void main(String[] args) {
        new ManageBooksForm();
    }
}
