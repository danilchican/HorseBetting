package test.com.epam.horsebetting.database;

import com.epam.horsebetting.database.ConnectionPool;
import com.epam.horsebetting.database.ProxyConnection;
import org.junit.*;

import static org.junit.Assert.*;

public class ConnectionPoolTest {

    private static ConnectionPool connectionPool;

    @BeforeClass
    public static void setUp() throws Exception {
        connectionPool = ConnectionPool.getInstance();
    }

    @Test
    public void fetchConnection() throws Exception {
        ProxyConnection connection = connectionPool.fetchConnection();
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
        ProxyConnection connection = connectionPool.fetchConnection();

        try {
            connectionPool.releaseConnection(connection);
        } catch (Throwable t) {
            fail(t.getMessage());
        }

        connectionPool.destroy();
    }
}
