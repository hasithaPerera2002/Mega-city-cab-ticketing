package org.icbt.hasitha.megacity.util.db;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;


public class DBConnection {
    private static DBConnection instance ;
    private Connection connection;

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/megacity";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String DRIVER = "org.postgresql.Driver";


    private DBConnection() {
        // Initialize driver
        try {
            Class.forName(DRIVER);
            LOGGER.info("MySQL JDBC Driver registered successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.info( "MySQL JDBC Driver not found", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }


    private Connection createConnection() throws SQLException {
        LOGGER.info("Creating new database connection");
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }


    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(1)) {
            connection = createConnection();
        }
        LOGGER.info("Returning database connection");
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed");
            } catch (SQLException e) {
                LOGGER.info( "Error closing connection", e);
            }
        }
    }


    public boolean isConnectionValid(int timeoutSeconds) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(timeoutSeconds);
        } catch (SQLException e) {
            LOGGER.info( "Error validating connection", e);
            return false;
        }
    }
}
