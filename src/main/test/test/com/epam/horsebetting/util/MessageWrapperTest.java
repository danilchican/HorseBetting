package test.com.epam.horsebetting.util;

import com.epam.horsebetting.util.MessageWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageWrapperTest {

    private MessageWrapper wrapper;

    @Before
    public void setUp() throws Exception {
        wrapper = new MessageWrapper();
    }

    @Test
    public void addSuccessScenarioTest() {
        List<String> list = new ArrayList<String>() {{
           add("Test message");
        }};

        wrapper.add("Test message");
        Assert.assertEquals(list, wrapper.findAll());
    }

    @Test
    public void addNullFailScenarioTest() {
        wrapper.add(null);
        Assert.assertEquals(wrapper.size(), 0);
    }
}
