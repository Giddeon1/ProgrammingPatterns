package org.gigi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    public static final String CREATE_STUDENT_TABLE_SQL =
            """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;

    public static final String CREATE_STAFF_TABLE_SQL =
            """
            CREATE TABLE IF NOT EXISTS staff (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;

    public static final String CREATE_LIBRARIAN_TABLE_SQL =
            """
            CREATE TABLE IF NOT EXISTS librarian (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL
            )
            """;

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

    /**
     * creates a database table using the provided SQL statement.
     * @param statementStr the SQL statement string to create the table.
     */
    public static void createTable(String statementStr) {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(statementStr);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DatabaseUtil(){}

}
