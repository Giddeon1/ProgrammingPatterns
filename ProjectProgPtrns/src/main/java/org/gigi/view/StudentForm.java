package org.gigi.view;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.Book;
import org.gigi.util.DatabaseUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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

        // Initializing all Components
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

        // Adding Buttons
        add(viewButton);
        add(searchButton);
        add(borrowBookButton);
        add(returnBookButton);
        add(goBackButton);
        add(exitButton);

        /**
         * ActionListener tp view All Books
         */
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBooks();
            }
        });

        /**
         * Displays a list of all Books
         */
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchUI();
            }
        });

        /**
         * Borrowing a book by searching the ID
         */
        borrowBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBorrowUI();
            }
        });
        /**
         * Returning a book by the ID
         */
        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReturnUI();
            }
        });

        /**
         * Going back to the Librarian Form
         */
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideDynamicComponents();
                new MainForm();
                dispose();
            }
        });

        /**
         * Exiting out the Application
         */
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /**
         *Confirms that the book has been borrowed
         */
        borrowBookIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });

        /***
         * Confirms that the book has been returned
         */
        returningBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        /**
         * Searching a book either through the title, ID or author
         */
        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        setVisible(true);
    }

    /**
     * Displays all books in the library system in a tabular format.
     * If no books are available, a message is shown to the user.
     */

    private void displayBooks() {
        List<Book> bookList = librarySystemController.getAllBooks();
        if (bookList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available.", "Search Book", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"ID", "Title", "Author", "Genre", "Year Published", "ISBN", "Copies Available", "Availability"};

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

    /**
     * Displays the search interface for finding books by title, author, or ISBN.
     */
    private void showSearchUI() {
        hideDynamicComponents();
        add(searchBookLabel);
        add(typeComboBox);
        add(searchBookTextField);
        add(searchBookButton);
        revalidate();
        repaint();
    }

    /**
     * Searches for books based on the selected search criteria (title, author, or ISBN).
     * Displays search results or an appropriate message if no results are found.
     */
    private void searchBook() {
        String searchTerm = searchBookTextField.getText();
        String searchType = (String) typeComboBox.getSelectedItem();
        if (searchType.equals("By Title")) {
            List<Book> books = librarySystemController.fetchBooksByTitle(searchTerm);
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books available.", "Search Book", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                viewBookTable(books);
            }
        } else if(searchType.equals("By Author")) {
            try {
                List<Book> books = librarySystemController.fetchBookByAuthor(searchTerm);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No books available.", "Search Book", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            Book book = DatabaseUtil.fetchBookByISBN(searchTerm);
            String[] columnNames = {"ID", "Title", "Author Name", "Year Published", "ISBN", "Copies Available", "Availability"};
            String[][] bookData = new String[1][columnNames.length]; // Only one book, so array size is [1][columns]
            if (book != null) {
                bookData[0][0] = String.valueOf(book.getIsbn());
                bookData[0][1] = book.getTitle();
                bookData[0][2] = book.getAuthorFName() + " " + book.getAuthorLName();
                bookData[0][3] = String.valueOf(book.getYear());
                bookData[0][4] = book.getIsbn();
                bookData[0][5] = String.valueOf(book.getAvailableCopies());
                bookData[0][6] = book.isAvailable() ? "Available" : "Not Available";

                JTable bookTable = new JTable(bookData, columnNames);
                bookTable.setEnabled(false); // Disable editing
                JScrollPane scrollPane = new JScrollPane(bookTable);

                JFrame bookDetailsFrame = new JFrame("Book Details");
                bookDetailsFrame.setSize(600, 300);
                bookDetailsFrame.setLocationRelativeTo(null);
                bookDetailsFrame.add(scrollPane);
                bookDetailsFrame.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "No book found with the given ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    /**
     * Displays a list of books in a table format.
     *
     * @param books the list of books to display in the table.
     */
    private void viewBookTable(List<Book> books) {
        // Create column headers for the table
        String[] columnNames = {"ID", "Title", "Author Name", "Year Published", "ISBN", "Copies Available", "Availability"};

        // Convert the book list to a 2D array for JTable
        String[][] bookData = new String[books.size()][columnNames.length];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            bookData[i][0] = String.valueOf(book.getIsbn());
            bookData[i][1] = book.getTitle();
            bookData[i][2] = book.getAuthorFName() + " " + book.getAuthorLName();
            bookData[i][3] = String.valueOf(book.getYear());
            bookData[i][4] = book.getIsbn();
            bookData[i][5] = String.valueOf(book.getAvailableCopies());
            bookData[i][6] = book.isAvailable() ? "Available" : "Not Available";
        }

        // Create a JTable with book data
        JTable bookTable = new JTable(bookData, columnNames);
        bookTable.setEnabled(false); // Disable editing

        // Add the table to a JScrollPane for better usability
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Create a new frame to display the books
        JFrame bookListFrame = new JFrame("List of Books");
        bookListFrame.setSize(800, 400);
        bookListFrame.setLocationRelativeTo(null);
        bookListFrame.add(scrollPane);
        bookListFrame.setVisible(true);
    }

    /**
     * Displays the interface for borrowing a book by its ID.
     */
    private void showBorrowUI() {
        hideDynamicComponents();
        add(searchBookIdLabel);
        add(borrowBookIDTextField);
        add(borrowBookIDButton);
        revalidate();
        repaint();
    }

    /**
     * Simulates borrowing a book by ID and shows a confirmation message.
     */
    private void borrowBook() {
        String bookID = borrowBookIDTextField.getText();
        JOptionPane.showMessageDialog(this, "Book " + bookID + " has been borrowed successfully", "Borrow Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the interface for returning a book by its ID.
     */
    private void showReturnUI() {
        hideDynamicComponents();
        add(returnBookIdLabel);
        add(returnBookIDTextField);
        add(returningBookButton);
        revalidate();
        repaint();
    }

    /**
     * Simulates returning a book by ID and shows a confirmation message.
     */
    private void returnBook() {
        String bookID = returnBookIDTextField.getText();
        JOptionPane.showMessageDialog(this, "Book " + bookID + " has been returned successfully", "Return Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Hides all dynamic components from the UI to prepare for the next action or screen.
     */
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
