package test.com.epam.horsebetting.config;

import com.epam.horsebetting.config.MessageConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

public class MessageConfigTest {

    private static MessageConfig messageResource;

    @BeforeClass
    public static void setUp() {
        messageResource = new MessageConfig(new Locale("en", "US"));
    }

    @Test
    public void getSuccessScenarioTest() {
        String key = "user.not_found";

        String expectedMessage = "Such user does not exists.";
        String actualMessage = messageResource.get(key);

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getFailScenarioTest() {
        String key = "user.some_field";
        String message = messageResource.get(key);

        Assert.assertNull(message);
    }
}
