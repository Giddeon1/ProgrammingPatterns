package org.gigi;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
      // DatabaseUtil.DELETE_ALL_TABLES_SQL();

        DatabaseUtil.CREATE_BOOK_TABLE_SQL();
        DatabaseUtil.CREATE_STUDENT_TABLE_SQL();
        DatabaseUtil.CREATE_LIBRARIAN_TABLE_SQL();
        DatabaseUtil.CREATE_BORROWED_BOOK_TABLE_SQL();

        // Add a book, user, librarian, and borrow record to the database for testing
        RegularBook book = new RegularBook("12345", "Java Basics", "John", "Doe", 2021, 5);
        Student user = new Student("Jane", "Doe", "jane.doe@example.com");
        Librarian librarian = new Librarian("Alice", "Johnson", "alice.johnson@example.com");
        BorrowedBookRecord record = new BorrowedBookRecord(book, user, librarian, LocalDate.now().plusDays(14));

        DatabaseUtil.insertIntoBookTable(book);
        DatabaseUtil.insertIntoStudentTable(user);
        DatabaseUtil.insertBorrowRecord(record);

        // Fetch all borrowed records
        List<BorrowedBookRecord> records = DatabaseUtil.fetchAllBorrowedBookRecords();
        records.forEach(System.out::println);



    }
}