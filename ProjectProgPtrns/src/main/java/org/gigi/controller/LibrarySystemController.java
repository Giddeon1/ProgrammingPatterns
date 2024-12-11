package org.gigi.controller;

import lombok.Getter;
import lombok.Setter;
import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import javax.xml.stream.events.DTD;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Getter
@Setter
public class LibrarySystemController {
    private static LibrarySystemController librarySystemControllerInstance;
    private LibrarySystem librarySystem;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    @Getter
    @Setter
    private User currentUser;

    public static LibrarySystemController getInstance() {
        if (librarySystemControllerInstance == null) {
            synchronized (LibrarySystemController.class) {
                if (librarySystemControllerInstance == null) {
                    librarySystemControllerInstance = new LibrarySystemController();
                }
            }
        }
        return librarySystemControllerInstance;
    }

    private LibrarySystemController() {
        DatabaseUtil.initTables();
        this.librarySystem = LibrarySystem.getInstance();
    }

    public void addBook(Book book) {
        librarySystem.getBooks().add(book);
        DatabaseUtil.insertIntoBookTable(book);
    }

    /**
     * method that removes a book from the database using the db method
     * and removes the book from the library system list
     * @param isbn the isbn of the book we want to remove
     */
    public void removeBook(String isbn) {
        for (int i = 0; i < librarySystem.getBooks().size(); i++) {
            if (librarySystem.getBooks().get(i).getIsbn().equals(isbn)) {
                librarySystem.getBooks().remove(i);
                try {
                    DatabaseUtil.deleteBookByISBN(isbn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
    /**
     * Adds a user (either Student or Librarian) to the system.
     * If the user is a Student, inserts their data into the student table;
     * if the user is a Librarian, inserts their data into the librarian table.
     */
    public void addUser(User user) {
        librarySystem.getUsers().add(user);
        if (user instanceof Student) {
            DatabaseUtil.insertIntoStudentTable((Student) user);
        } else if (user instanceof Librarian) {
            DatabaseUtil.insertIntoLibrarianTable((Librarian) user);
        }
    }

    /**
     * Allows a user to borrow a book, updating the available copies and creating a borrow record.
     * Throws an IllegalStateException if no copies are available.
     */
    public void borrowBook(User user, RegularBook book, Librarian librarian) {

            if (book.getAvailableCopies() > 0) {
                book.decrementAvailableCopies();
                DatabaseUtil.updateBookCopies(book.getIsbn(), book.getAvailableCopies());
                BorrowedBookRecord record = new BorrowedBookRecord(book, user, librarian);
                user.borrowBook(record);
                librarySystem.getBorrowedBookRecords().add(record);
                DatabaseUtil.insertBorrowRecord(record);
            } else {
                throw new IllegalStateException("No copies of the book are available.");
            }

    }

    /**
     * Allows a user to return a borrowed book, updating the available copies and the return record.
     * Throws an IllegalArgumentException if no borrowed record is found for the given user and book.
     */
    public void returnBook(User user, RegularBook book) {

            BorrowedBookRecord record = librarySystem.findBorrowedRecord(user, book);
            if (record == null) {
                throw new IllegalArgumentException("No borrowed record found for this book and user.");
            }
            record.returnBook();
            book.incrementAvailableCopies();
            DatabaseUtil.updateBookCopies(book.getIsbn(), book.getAvailableCopies());
             DatabaseUtil.updateReturnDate(book.getIsbn(), user.getUserId());
            user.returnBook(record);

    }

    /**
     * uses db method to fetch all books in the db
     * @return all books from the db
     */
    public List<Book> getAllBooks() {
        return DatabaseUtil.fetchAllBooks();
    }

    /**
     * uses db method to fetch all users in the db
     * @return a list of users from the db
     */
    public List<User> getAllUser() {
        return DatabaseUtil.fetchAllUsers();
    }

    /**
     * uses the db method to fetch users with a keyword
     * @param keyword the word to be used to find users
     * @return a list of users where their first or last name matches the keyword
     */
    public List<User> searchUsers(String keyword) {
        return DatabaseUtil.searchUsersByKeyword(keyword);
    }

    /**
     * uses the db method to remove a user from the database and the library system list
     * @param userId the userId to be removed from the db and the library system list
     */
    public void removeUser(int userId) throws SQLException {
        for (int i = 0; i < librarySystem.getUsers().size(); i++) {
            if (librarySystem.getUsers().get(i).getUserId() == userId) {
                librarySystem.getUsers().remove(i);
                DatabaseUtil.removeUser(userId);
            }
        }
    }

    /**
     * method that gets a list of books matching the title
     * @param title the title of the book that we want to get
     * @return a list of books that match the title someway
     */
    public List<Book> fetchBooksByTitle(String title) {
        return DatabaseUtil.fetchBooksByTitle(title);
    }

    /**
     * method to get a book by isbn
     * @param isbn the isbn of the book wanted
     * @return the book
     */
    public Book fetchBooksByIsbn(String isbn) {
        return DatabaseUtil.fetchBookByISBN(isbn);
    }

    /**
     * Fetches books from the database by
     * search term that matches the author's first or last name.
     * @param authorName the author that we want to find books of
     * @return a list of books that have as author a resemblance to the author name
     * @throws SQLException if something goes wrong
     */
    public List<Book> fetchBookByAuthor(String authorName) throws SQLException {
        return DatabaseUtil.fetchBooksByAuthor(authorName);
    }


}
