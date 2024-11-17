package org.gigi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Librarian extends User {
    private static int nextId = 1;
    //private static final int MAX_BOOKS_ALLOWED = 5;

    public Librarian (int id,String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return "Librarian details:" +
                "\nId: " + this.userId +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail:" + email +
                "\nAmount of overdue books" + overDueBookCount +
                "\nAmount of books owned" + issuedBooks;
    }

    @Override
    public List<Book> getOverdueBooks() {
        return null;
    }

    @Override
    public boolean canBorrowBook(Book book) {
        return false;
    }

    @Override
    public boolean returnBook(Book book) {
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().equals(book)) {
                issuedBooks.remove(record);
                book.incrementAvailableCopies();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> searchBook(String keyword) {
//        List<Book> results = new ArrayList<>();
//        for (Book book : availableBooks) { // Assume availableBooks is a List<Book> but there is an error and i need to fix it later
//            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getAuthorFName().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getAuthorLName().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getIsbn().contains(keyword)) {
//                results.add(book);
//            }
//        }
        return null;
    }


    //librarian own methods
    public boolean addBook(Book book, List<Book> books) {
//        for (Book b : books) {
//            if (b.getIsbn() == book.getIsbn()) {
//                System.out.println("Book with ID " + book.getBookId() + " already exists.");
//                return false; // Book already exists
//            }
//        }
//        books.add(book);
//        System.out.println("Book added successfully: " + book.getTitle());
       return true;
    }

    public boolean issueBook(String bookId, User user, List<Book> books, List<BorrowedBookRecord> issuedBooks) {
        for (Book book : books) {
            if (book.getIsbn().equals(bookId)  && book.isAvailable()) {
                book.setAvailable(false);
                issuedBooks.add(new BorrowedBookRecord(book, user));
                System.out.println("Book issued successfully: " + book.getTitle());
                return true;
            }
        }
        System.out.println("Book with ID " + bookId + " is either unavailable or does not exist.");
        return false;
    }

    public boolean removeBook(String bookId, List<Book> books) {
        for (Book book : books) {
            if (book.getIsbn().equals(bookId)) {
                books.remove(book);
                System.out.println("Book removed successfully: " + book.getTitle());
                return true;
            }
        }
        System.out.println("Book with ID " + bookId + " does not exist.");
        return false;
    }

    public boolean returnBook(String bookId, List<BorrowedBookRecord> issuedBooks) {
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().getIsbn().equals(bookId)) {
                record.getBook().setAvailable(true);
                issuedBooks.remove(record);
                System.out.println("Book returned successfully: " + record.getBook().getTitle());
                return true;
            }
        }
        System.out.println("No record found for book with ID " + bookId);
        return false;
    }

    public List<Book> trackOverdueBooks(List<BorrowedBookRecord> issuedBooks) {
        List<Book> overdueBooks = new ArrayList<>();
        for (BorrowedBookRecord record : issuedBooks) {
            overdueBooks.add(record.getBook());
        }
        return overdueBooks;
    }

}
