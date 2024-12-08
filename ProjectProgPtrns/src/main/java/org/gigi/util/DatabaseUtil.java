package org.gigi.util;
import org.gigi.model.Book;
import org.gigi.model.Librarian;
import org.gigi.model.Staff;
import org.gigi.model.Student;

import java.sql.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

public class DatabaseUtil {
    //todo:
    // Change exception for some methods,
    // java docs,
    // inserting with plain value for all databases or some,
    // check book columns. do we need to put the total copies?
    // query methods
    private static final String DATABASE_PATH = "jdbc:sqlite:src/main/resources/database/database.db";
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();

    /**
     * establishes a connection to the sqlite database
     * @return a connection object representing the database connection
     */
    private static Connection connect() {
        String url = "jdbc:sqlite:src/main/resources/database/database.db";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

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

    public static void CREATE_STAFF_TABLE_SQL() {
        String sql ="""
            CREATE TABLE IF NOT EXISTS staff (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;
        createTable(sql);
    }

    public static void CREATE_LIBRARIAN_TABLE_SQL() {
        String sql = """
        CREATE TABLE IF NOT EXISTS librarian (
        id INTEGER PRIMARY KEY,
        first_name TEXT NOT NULL,
        last_name TEXT NOT NULL,
        email TEXT NOT NULL
        )
        """;
        createTable(sql);
    }

    public static void CREATE_BOOK_TABLE_SQL() {
        String sql = """
        CREATE TABLE IF NOT EXISTS book (
        isbn TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        author_first_name TEXT NOT NULL,
        author_first_name TEXT NOT NULL,
        year INTEGER NOT NULL,
        total_copies INTEGER NOT NULL
        )
        """;
        createTable(sql);
    }

    public static void insertIntoBookTable(Book book) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO book(isbn, title, author_first_name, author_last_name, year, total_copies) VALUES(?, ?, ?, ?, ?, ?)";
        try(Connection connection = connect();
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

    public static void insertIntoLibrarianTable(Librarian librarian) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO librarian(id,first_name,last_name,email) VALUES(?,?,?,?)";
        try(Connection connection = connect();
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

    public static void insertIntoStudentTable(Student student) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO student(id,first_name,last_name,email) VALUES(?,?,?,?)";
        try(Connection connection = connect();
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

    public static void insertIntoStaffTable(Staff staff) {
        WRITE_LOCK.lock();
        String sql = "INSERT INTO staff(id,first_name,last_name,email) VALUES(?,?,?,?)";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,staff.getUserId());
            preparedStatement.setString(2,staff.getFirstName());
            preparedStatement.setString(3,staff.getLastName());
            preparedStatement.setString(4,staff.getEmail());
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
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(statementStr);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

}
