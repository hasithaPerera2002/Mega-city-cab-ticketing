package util.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

public class DBConnetionTest {
    private static final Logger log = LoggerFactory.getLogger(DBConnetionTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConnectionLogging() throws SQLException {
        try (MockedStatic<DriverManager> driverManagerMockedStatic = Mockito.mockStatic(DriverManager.class)) {
            //create mock connection
            Connection mockConnection = Mockito.mock(Connection.class);
            //return mock connection when DriverManager.getConnection() is called with any string

            driverManagerMockedStatic.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConnection);
            DBConnection dbConnection = DBConnection.getInstance();


            Connection connection = dbConnection.getConnection();
            //check connection is null or not
            assertNotNull(connection);
            //check connection is same as mockConnection
            assertSame(mockConnection, connection);
            dbConnection.getConnection().close();

        }
    }
}
