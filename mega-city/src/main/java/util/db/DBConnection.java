package util.db;

import org.slf4j.Logger;
import util.logger.GlobalLogger;

import java.sql.Connection;
import java.sql.SQLException;


public class DBConnection {
    private static DBConnection instance ;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/megacity";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String DRIVER = "org.postgresql.Driver";
    Logger LOGGER = GlobalLogger.getLogger();

    private DBConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            connection = java.sql.DriverManager.getConnection(URL, USERNAME, PASSWORD);
            LOGGER.info("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Error occurred while connecting to the database", e);
            throw new SQLException("Error occurred while connecting to the database", e);
        }
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
