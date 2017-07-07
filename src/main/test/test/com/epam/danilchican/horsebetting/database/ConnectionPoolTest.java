package test.com.epam.danilchican.horsebetting.database;

import com.epam.danilchican.betting.database.ConnectionPool;
import com.epam.danilchican.betting.database.ProxyConnection;
import org.junit.*;

import java.sql.Connection;

import static org.junit.Assert.*;

public class ConnectionPoolTest {

    private static ConnectionPool connectionPool;

    @BeforeClass
    public static void setUp() throws Exception {
        connectionPool = ConnectionPool.getInstance();
    }

    @Test
    public void obtainConnection() throws Exception {
        Connection connection = connectionPool.fetchConnection();
        assertNotNull(connection);
    }

    @Test
    public void releaseConnection() throws Exception {
        ProxyConnection connection = connectionPool.fetchConnection();

        try {
            connectionPool.releaseConnection(connection);
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void getInstance() throws Exception {
        assertEquals(connectionPool, ConnectionPool.getInstance());
    }

    @Test
    public void closePool() throws Exception {
        connectionPool.close();
    }
}
