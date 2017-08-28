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
    public void getInstanceTest() throws Exception {
        assertEquals(connectionPool, ConnectionPool.getInstance());
    }

    @Test
    public void fetchConnectionTest() throws Exception {
        ProxyConnection connection = connectionPool.fetchConnection();
        assertNotNull(connection);
        connectionPool.releaseConnection(connection);
    }

    @Test
    public void releaseConnectionTest() throws Exception {
        ProxyConnection connection = connectionPool.fetchConnection();
        connectionPool.releaseConnection(connection);
    }

    @Test
    public void destroyPoolTest() throws Exception {
        connectionPool.destroy();
    }
}
