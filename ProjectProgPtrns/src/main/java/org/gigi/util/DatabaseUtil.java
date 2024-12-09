package org.gigi.util;
import org.gigi.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseUtil {
    //todo:
    // inserting with plain value for all databases or some,
    // check book columns. do we need to put the total copies? it should increment (+1) once we add a book
    // query methods
    private static final String DATABASE_URL = "jdbc:sqlite:ProjectProgPtrns/src/main/resources/database/database.db";
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();


    public static void DELETE_ALL_TABLES_SQL() {
        String[] deleteTableSQLs = {
                "DROP TABLE IF EXISTS students",
                "DROP TABLE IF EXISTS librarians",
                "DROP TABLE IF EXISTS books",
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

    /*
    public static List<User> fetchAllUsers() {
        READ_LOCK.lock();
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String role = resultSet.getString("role"); // WE NEED A ROLE???
                if ("Student".equalsIgnoreCase(role)) {
                    users.add(new Student(
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email")
                    ));
                } else if ("Librarian".equalsIgnoreCase(role)) {
                    users.add(new Librarian(
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
    }*/

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

    public static List<BorrowedBookRecord> fetchAllBorrowedBookRecords() {
        READ_LOCK.lock();
        String sql = "SELECT * FROM borrowed_books";
        List<BorrowedBookRecord> records = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Retrieve related data from the borrowed_books table
                String bookIsbn = resultSet.getString("book_isbn");
                int studentId = resultSet.getInt("student_id");
                int librarianId = resultSet.getInt("librarian_id");

                // Fetch associated objects
                Book book = fetchBookByISBN(bookIsbn);
                Student student = fetchStudentById(studentId); // Only fetch students
                Librarian librarian = fetchLibrarianById(librarianId); // Fetch librarian for the record

                // Parse dates
                LocalDate borrowDate = LocalDate.parse(resultSet.getString("borrow_date"));
                LocalDate dueDate = LocalDate.parse(resultSet.getString("due_date"));
                String returnDateStr = resultSet.getString("return_date");
                LocalDate returnDate = returnDateStr != null ? LocalDate.parse(returnDateStr) : null;

                // Create BorrowedBookRecord object
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

    public static Student fetchStudentById(int userId) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Student(
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

    public static Librarian fetchLibrarianById(int userId) {
        READ_LOCK.lock();
        String sql = "SELECT * FROM librarians WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Librarian(
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
        return null; // Return null if no librarian is found with the given ID
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
}
