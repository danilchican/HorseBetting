package test.com.epam.horsebetting.dao;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.impl.SuitDAOImpl;
import com.epam.horsebetting.database.ConnectionPool;
import com.epam.horsebetting.database.ProxyConnection;
import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;
import com.mysql.jdbc.PreparedStatement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SuitDAOImplTest {

    @Spy
    private ConnectionPool pool = ConnectionPool.getInstance();

    @Mock
    private ProxyConnection conn;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet resultSet;

    private Suit suit;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(pool);

        doReturn(conn).when(pool).fetchConnection();
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);

        suit = new Suit();
        suit.setName("SuitMockito");

        when(resultSet.first()).thenReturn(true);
        when(resultSet.getString(SQLFieldConfig.Suit.NAME)).thenReturn(suit.getName());
        when(stmt.executeQuery()).thenReturn(resultSet);
    }

    @Test(expected = NullPointerException.class)
    public void createNullSuitTest() throws DAOException {
        new SuitDAOImpl(false).create(null);
    }

    @Test
    public void createSuitTest() throws DAOException {
        Suit s = new SuitDAOImpl(false).create(suit);
        Assert.assertEquals(s.getName(), suit.getName());
    }
}