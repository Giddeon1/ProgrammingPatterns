package org.gigi.util;
import org.gigi.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseUtil {
    private static final String DATABASE_URL = "jdbc:sqlite:ProjectProgPtrns/src/main/resources/database/database.db";
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();

    /**
     * method to initialize the database
     */
    public static void initTables() {
        DatabaseUtil.CREATE_STUDENT_TABLE_SQL();
        DatabaseUtil.CREATE_BOOK_TABLE_SQL();
        DatabaseUtil.CREATE_LIBRARIAN_TABLE_SQL();
        DatabaseUtil.CREATE_BORROWED_BOOK_TABLE_SQL();
    }

    public static void DELETE_ALL_TABLES_SQL() {
        String[] deleteTableSQLs = {
                "DROP TABLE IF EXISTS students",
                "DROP TABLE IF EXISTS librarians",
                "DROP TABLE IF EXISTS librarian",
                "DROP TABLE IF EXISTS books",
                "DROP TABLE IF EXISTS book",
                "DROP TABLE IF EXISTS borrowed_books"
        };

        for (String sql : deleteTableSQLs) {
            deleteTable(sql);
        }
    }

    /**
     * Executes a SQL statement to delete a table.
     * @param statementStr the SQL statement string to delete the table.
     */
    private static void deleteTable(String statementStr) {
        WRITE_LOCK.lock();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(statementStr);
            System.out.println("Deleted table: " + statementStr.split(" ")[2]); // Extracts the table name
        } catch (SQLException e) {
            System.err.println("Error deleting table: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * establishes a connection to the sqlite database
     * @return a connection object representing the database connection
     */
    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the total copies of a book in the database.
     * @param isbn The ISBN of the book to update.
     * @param totalCopies The new total copies.
     */
    public static void updateBookCopies(String isbn, int totalCopies) {
        WRITE_LOCK.lock();
        String sql = "UPDATE books SET total_copies = ? WHERE isbn = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, totalCopies);
            preparedStatement.setString(2, isbn);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No book found with ISBN: " + isbn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book copies: " + e.getMessage(), e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * Fetches a book by its ISBN.
     * @param isbn The ISBN of the book.
     * @return The matching book or null if not found.
     */
    public static Book fetchBookByISBN(String isbn) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RegularBook(
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author_first_name"),
                        resultSet.getString("author_last_name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("total_copies")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching book by ISBN: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return null; // If no book is found
    }

    /**
     * Fetches all books with a given title.
     * @param title The title to search for.
     * @return A list of matching books.
     */
    public static List<Book> fetchBooksByTitle(String title) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new RegularBook(
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author_first_name"),
                        resultSet.getString("author_last_name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("total_copies")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching books by title: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return books;
    }

    /**
     * method to create the student table
     */
    public static void CREATE_STUDENT_TABLE_SQL() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;
        createTable(sql);
    }

    /**
     * method to create the librarian table
     */
    public static void CREATE_LIBRARIAN_TABLE_SQL() {
        String sql = """
        CREATE TABLE IF NOT EXISTS librarians (
        id INTEGER PRIMARY KEY,
        first_name TEXT NOT NULL,
        last_name TEXT NOT NULL,
        email TEXT NOT NULL
        )
        """;
        createTable(sql);
    }


    /**
     * method to create the book table
     */
    public static void CREATE_BOOK_TABLE_SQL() {
        String sql = """
        CREATE TABLE IF NOT EXISTS books (
        isbn TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        author_first_name TEXT NOT NULL,
        author_last_name TEXT NOT NULL,
        year INTEGER NOT NULL,
        total_copies INTEGER NOT NULL
        )
        """;
        createTable(sql);
    }

    /**
     * method to create the borrowed book table
     */
    public static void CREATE_BORROWED_BOOK_TABLE_SQL() {
        String sql = """
                CREATE TABLE borrowed_books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    book_isbn TEXT NOT NULL,
                    student_id INTEGER NOT NULL,
                    librarian_id INTEGER NOT NULL,
                    borrow_date TEXT NOT NULL,
                    due_date TEXT NOT NULL,
                    return_date TEXT, -- Nullable; null means not returned
                    FOREIGN KEY (book_isbn) REFERENCES book(isbn),
                    FOREIGN KEY (student_id) REFERENCES student(id),
                    FOREIGN KEY (librarian_id) REFERENCES librarian(id)
                );
        """;
        createTable(sql);
    }

    /**
     * method to insert a record to the borrowed book record
     * @param record the record to be inserted
     */
    public static void insertBorrowRecord(BorrowedBookRecord record) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO borrowed_books (book_isbn, student_id, librarian_id, borrow_date, due_date, return_date) VALUES (?, ?, ?, ?, ?, NULL)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, record.getBook().getIsbn());
            preparedStatement.setInt(2, record.getOwner().getUserId());
            preparedStatement.setInt(3, record.getIssuer().getUserId());
            preparedStatement.setString(4, record.getBorrowDate().toString());
            preparedStatement.setString(5, record.getDueDate().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting borrow record: " + e.getMessage(), e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * method that updates the return date in the borrowed book table
     * @param isbn the isbn of the book in the borrowed book table
     * @param userId the userid of the user in the borrowed book table
     */
    public static void updateReturnDate(String isbn, int userId) {
        WRITE_LOCK.lock();
        String sql = "UPDATE borrowed_books SET return_date = ? WHERE book_isbn = ? AND student_id = ? AND return_date IS NULL";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, LocalDate.now().toString());
            preparedStatement.setString(2, isbn);
            preparedStatement.setInt(3, userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No matching borrow record found to update.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating return date: " + e.getMessage(), e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * fetchs a list of all users from the database
     * @return a list of all users
     */
    public static List<User> fetchAllUsers() {
        READ_LOCK.lock();
        List<User> users = new ArrayList<>();
        try {
            // Fetch all students
            String studentSql = "SELECT * FROM students";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(studentSql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email")
                    ));
                }
            }

            String librarianSql = "SELECT * FROM librarians";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(librarianSql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new Librarian(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all users: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return users;
    }


    /**
     * fetches all books in the database
     * @return a list of books
     */
    public static List<Book> fetchAllBooks() {
        READ_LOCK.lock();
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                books.add(new RegularBook(
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author_first_name"),
                        resultSet.getString("author_last_name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("total_copies")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all books: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return books;
    }

    /**
     * method to fetch all borrowed book record
     * @return a list of borrowed book record
     */
    public static List<BorrowedBookRecord> fetchAllBorrowedBookRecords() {
        READ_LOCK.lock();
        String sql = "SELECT * FROM borrowed_books";
        List<BorrowedBookRecord> records = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String bookIsbn = resultSet.getString("book_isbn");
                int studentId = resultSet.getInt("student_id");
                int librarianId = resultSet.getInt("librarian_id");
                Book book = fetchBookByISBN(bookIsbn);
                Student student = fetchStudentById(studentId);
                Librarian librarian = fetchLibrarianById(librarianId);
                LocalDate borrowDate = LocalDate.parse(resultSet.getString("borrow_date"));
                LocalDate dueDate = LocalDate.parse(resultSet.getString("due_date"));
                String returnDateStr = resultSet.getString("return_date");
                LocalDate returnDate = returnDateStr != null ? LocalDate.parse(returnDateStr) : null;
                BorrowedBookRecord record = new BorrowedBookRecord(book, student, librarian, dueDate);
                record.setBorrowDate(borrowDate);
                if (returnDate != null) {
                    record.setReturnDate(returnDate);
                }
                records.add(record);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all borrowed book records: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return records;
    }

    /**
     * method to find a student by their id from the database
     * @param userId the userid of the student that we want to get
     * @return the student if found or null if not
     */
    public static Student fetchStudentById(int userId) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching student by ID: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return null;
    }

    /**
     * fetch a librarian by their id
     * @param userId the userid of the wanted librarian
     * @return the librarian object if found or null if not found
     */
    public static Librarian fetchLibrarianById(int userId) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM librarians WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Librarian(
                        resultSet.getInt("id"), // Pass the ID from the database
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching librarian by ID: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return null;
    }

    /**
     * method to insert into the book database
     * @param book the book object to be inserted into the database
     */
    public static void insertIntoBookTable(Book book) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO books(isbn, title, author_first_name, author_last_name, year, total_copies) VALUES(?, ?, ?, ?, ?, ?)";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,book.getIsbn());
            preparedStatement.setString(2,book.getTitle());
            preparedStatement.setString(3,book.getAuthorFName());
            preparedStatement.setString(4,book.getAuthorLName());
            preparedStatement.setInt(5,book.getYear());
            preparedStatement.setInt(6,book.getTotalCopies());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * method to insert a librarian into the librarian database
     * @param librarian the librarian object to be used
     */
    public static void insertIntoLibrarianTable(Librarian librarian) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO librarians(id,first_name,last_name,email) VALUES(?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,librarian.getUserId());
            preparedStatement.setString(2,librarian.getFirstName());
            preparedStatement.setString(3,librarian.getLastName());
            preparedStatement.setString(4,librarian.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * method to insert into the student database
     * @param student the student object to be used
     */
    public static void insertIntoStudentTable(Student student) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO students(id,first_name,last_name,email) VALUES(?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,student.getUserId());
            preparedStatement.setString(2,student.getFirstName());
            preparedStatement.setString(3,student.getLastName());
            preparedStatement.setString(4,student.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * creates a database table using the provided SQL statement.
     * @param statementStr the SQL statement string to create the table.
     */
   private static void createTable(String statementStr) {
        WRITE_LOCK.lock();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(statementStr);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
   }

    /**
     *
     * @param isbn
     * @throws SQLException
     */
    public static void deleteBookByISBN(String isbn) throws SQLException {
        String query = "DELETE FROM books WHERE isbn = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No book found with ISBN: " + isbn);
            }
        }
    }

    /**
     * method to remove a specific user by their id
     * searches both teacher and student table
     * @param userId the user id to be removed
     * @throws SQLException exception that may or may not arise
     */
    public static void removeUser(int userId) throws SQLException {
        String studentQuery = "DELETE FROM students WHERE id = ?";
        String librarianQuery = "DELETE FROM librarians WHERE id = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(studentQuery)) {
                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User with ID " + userId + " removed from students table.");
                    return; // Stop after successful deletion
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(librarianQuery)) {
                pstmt.setInt(1, userId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User with ID " + userId + " removed from librarians table.");
                    return;
                }
            }
            throw new IllegalArgumentException("No user found with ID " + userId);
        }
    }

    /**
     * method to find users by a specific keyword, checks their firstname and last name
     * @param keyword the keyword to use to search the db
     * @return a list of users that their fname or lname match the keyword
     */
    public static List<User> searchUsersByKeyword(String keyword) {
        List<User> users = new ArrayList<>();
        READ_LOCK.lock();
        String studentQuery = "SELECT * FROM students WHERE first_name LIKE ? OR last_name LIKE ?";
        String librarianQuery = "SELECT * FROM librarians WHERE first_name LIKE ? OR last_name LIKE ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement studentStmt = connection.prepareStatement(studentQuery)) {
                studentStmt.setString(1, "%" + keyword + "%");
                studentStmt.setString(2, "%" + keyword + "%");
                ResultSet studentResults = studentStmt.executeQuery();
                while (studentResults.next()) {
                    users.add(new Student(
                            studentResults.getInt("id"),
                            studentResults.getString("first_name"),
                            studentResults.getString("last_name"),
                            studentResults.getString("email")
                    ));
                }
            }

            try (PreparedStatement librarianStmt = connection.prepareStatement(librarianQuery)) {
                librarianStmt.setString(1, "%" + keyword + "%");
                librarianStmt.setString(2, "%" + keyword + "%");
                ResultSet librarianResults = librarianStmt.executeQuery();
                while (librarianResults.next()) {
                    users.add(new Librarian(
                            librarianResults.getInt("id"),
                            librarianResults.getString("first_name"),
                            librarianResults.getString("last_name"),
                            librarianResults.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching users by keyword: " + e.getMessage(), e);
        } finally {
            READ_LOCK.unlock();
        }
        return users;
    }

    /**
     * Fetches books from the database by a search term that matches
     * the author's first or last name.
     * @param searchTerm the term to search for in the author's first or last name
     * @return a list of Book objects matching the search term
     * @throws SQLException if a database error occurs
     */
    public static List<Book> fetchBooksByAuthor(String searchTerm) throws SQLException {
        String query = "SELECT * FROM books WHERE author_first_name LIKE ? OR author_last_name LIKE ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String likePattern = "%" + searchTerm + "%"; // Allow partial matches
            pstmt.setString(1, likePattern);
            pstmt.setString(2, likePattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new RegularBook(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author_first_name"),
                        rs.getString("author_last_name"),
                        rs.getInt("year"),
                        rs.getInt("total_copies")
                ));
            }
        }
        return books;
    }

    /**
     * this method is for trying to know who is logged in
     * it gets a user by their email
     * @param email the email that belongs to the user
     * @return the user
     */
    public static User fetchUserByEmail(String email) {
        String studentQuery = "SELECT * FROM students WHERE email = ?";
        String librarianQuery = "SELECT * FROM librarians WHERE email = ?";

        try (Connection conn = getConnection()) {
            // Check students table
            try (PreparedStatement pstmt = conn.prepareStatement(studentQuery)) {
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    );
                }
            }

            // Check librarians table
            try (PreparedStatement pstmt = conn.prepareStatement(librarianQuery)) {
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Librarian(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by email: " + e.getMessage(), e);
        }

        return null; // Return null if no user is found
    }

}
