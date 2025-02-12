package util.db;



import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;


public class DBConnection {
    private static DBConnection instance ;
    private final Connection connection;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DBConnection.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/megacity";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String DRIVER = "org.postgresql.Driver";


    private DBConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            connection = java.sql.DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Error occurred while connecting to the database", e);
            throw new SQLException("Error occurred while connecting to the database", e);
        }
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }else if (instance.getConnection().isClosed()){
            instance = new DBConnection();
        }
        return instance;
    }
    public Connection getConnection() {
        log.info("Returning the connection to database");
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Connection is valid");
            } else {
                log.warn("Connection is closed or invalid");
            }
        } catch (SQLException e) {
            log.error("Error while checking connection status", e);
        }
        return connection;
    }
}
