package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Librarian extends User {
    private static int nextId = 1;
    //private static final int MAX_BOOKS_ALLOWED = 5;

    public Librarian (String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return "Librarian details:" +
                "\nId: " + this.userId +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail:" + email;
    }

    @Override
    public List<BorrowedBookRecord> getOverdueRecords() {
        System.out.println("Librarians do not borrow books.");
        return new ArrayList<>();
    }

    @Override
    public boolean canBorrowBook(Book book) {
        System.out.println("Librarian cannot borrow a book");
        return false;
    }

    @Override
    public boolean returnBook(Book book) {
        System.out.println("Librarians do not return books");
        return false;
    }

    @Override
    public List<Book> searchBook(String keyword) {
        List<Book> searchResults = new ArrayList<>();
        List<Book> books = LibrarySystem.getInstance().getBooks();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthorFName().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthorLName().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(book);
            }
        }
            return searchResults;
    }


    public void addBook(Book book) {
        LibrarySystem.getInstance().getBooks().add(book);
        System.out.println("Book added succesfully: " + book.getTitle());
    }

    public void removeBook(String isbn) {
        List<Book> books = LibrarySystem.getInstance().getBooks();
        boolean removed = books.removeIf(book -> book.getIsbn().equals(isbn));
        if (removed) {
            System.out.println("Book with ISBN: " + isbn + " removed successfully");
        } else {
            System.out.println("Book with ISBN: " + isbn + " not found");
        }
    }

    public boolean issueBook(String bookISBN, User user) {
       List<Book> books = LibrarySystem.getInstance().getBooks();
       List<BorrowedBookRecord> borrowedBookRecords = LibrarySystem.getInstance().getBorrowedBookRecords();

       for (Book book : books) {
           if (book.getIsbn().equals(bookISBN) && book.getAvailableCopies() > 0) {
               book.decrementAvailableCopies();
               BorrowedBookRecord record = new BorrowedBookRecord(book, user);
               borrowedBookRecords.add(record);
               System.out.println("Book issued successfully: " + book.getTitle());
               return true;
           }
       }
        System.out.println("Book with ISBN:" + bookISBN + " is either unavailable or does not exist");
        return false;
    }


    public boolean returnBook(String bookISBN) {
        List<BorrowedBookRecord> borrowedBookRecords = LibrarySystem.getInstance().getBorrowedBookRecords();
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().getIsbn().equals(bookISBN)) {
                record.getBook().incrementAvailableCopies();
                borrowedBookRecords.remove(record);
                System.out.println("Book returned successfully: " + record.getBook().getTitle());
                return true;
            }
        }
        System.out.println("No record found for book with ISBN " + bookISBN);
        return false;
    }

    public List<Book> trackOverdueBooks() {
        List<BorrowedBookRecord> borrowedBookRecords = LibrarySystem.getInstance().getBorrowedBookRecords();
        List<Book> overdueBooks = new ArrayList<>();

        for (BorrowedBookRecord record : borrowedBookRecords) {
            if (record.isOverDue()) {
                overdueBooks.add(record.getBook());
            }
        }
        return overdueBooks;
    }

}
