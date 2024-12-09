package org.gigi.controller;

import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibrarySystemController {
    private LibrarySystem librarySystem;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public LibrarySystemController() {
        this.librarySystem = LibrarySystem.getInstance();
        initTables();
    }


    private void initTables() {
        DatabaseUtil.CREATE_STUDENT_TABLE_SQL();
        DatabaseUtil.CREATE_BOOK_TABLE_SQL();
       // DatabaseUtil.CREATE_STAFF_TABLE_SQL();
        DatabaseUtil.CREATE_LIBRARIAN_TABLE_SQL();
        DatabaseUtil.CREATE_BORROWED_BOOK_TABLE_SQL();
    }

    public void addBook(Book book) {
        threadPool.submit(() -> {
            librarySystem.getBooks().add(book);
            DatabaseUtil.insertIntoBookTable(book);
        });
    }

    public void addUser(User user) {
        threadPool.submit(() -> {
            librarySystem.getUsers().add(user);
            if (user instanceof Student) {
                DatabaseUtil.insertIntoStudentTable((Student) user);
            } else if (user instanceof Librarian) {
                DatabaseUtil.insertIntoLibrarianTable((Librarian) user);
            }
        });
    }

    public void borrowBook(User user, RegularBook book, Librarian librarian) {
        threadPool.submit(() -> {
            if (book.getAvailableCopies() > 0) {
                BorrowedBookRecord record = new BorrowedBookRecord(book, user, librarian);
                user.borrowBook(record);
                librarySystem.getBorrowedBookRecords().add(record);
                book.decrementAvailableCopies();
                DatabaseUtil.updateBookCopies(book.getIsbn(), book.getAvailableCopies());
            } else {
                throw new IllegalStateException("No copies of the book are available.");
            }
        });
    }

    public void returnBook(User user, RegularBook book) {
        threadPool.submit(() -> {
            BorrowedBookRecord record = librarySystem.findBorrowedRecord(user, book);
            if (record == null) {
                throw new IllegalArgumentException("No borrowed record found for this book and user.");
            }
            record.returnBook();
            user.returnBook(record);
            book.incrementAvailableCopies();
            DatabaseUtil.updateBookCopies(book.getIsbn(), book.getAvailableCopies());
            DatabaseUtil.updateReturnDate(book.getIsbn(), user.getUserId());
        });
    }

}
