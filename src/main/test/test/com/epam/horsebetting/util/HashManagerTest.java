package test.com.epam.horsebetting.util;

import com.epam.horsebetting.util.HashManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HashManagerTest {

    private static String st;

    @BeforeClass
    public static void setUp() throws Exception {
        st = "danilchican123456";
    }

    @Test
    public void makeHashTrueScenarioTest() {
        String expected = "8767378b38f2435cec4326f1d55fbcfe";
        String actual = HashManager.make(st);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void makeHashFalseScenarioTest() {
        String expected = "someHashString";
        String actual = HashManager.make(st);

        Assert.assertNotEquals(expected, actual);
    }
}
