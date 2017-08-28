package test.com.epam.horsebetting.validator;

import com.epam.horsebetting.validator.HorseValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class HorseValidatorTest {

    private HorseValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new HorseValidator(new Locale("RU"));
    }

    @Test
    public void validateCreateHorseSuccessScenarioTest() {
        String name = "Horse Name";
        String gender = "male";
        String age = "40";
        String suitId = "123";

        boolean actual = validator.validateCreateHorse(name, gender, age, suitId);
        Assert.assertTrue(actual);
    }

    @Test
    public void validateCreateHorseFailScenarioTest() {
        String name = "Hors_ae Name 1";
        String gender = "malse";
        String age = "4_0";
        String suitId = "12;3";

        boolean actual = validator.validateCreateHorse(name, gender, age, suitId);
        Assert.assertFalse(actual);
    }

    @Test
    public void validateRemoveHorseSuccessScenarioTest() {
        String id = "123";

        boolean actual = validator.validateRemoveHorse(id);
        Assert.assertTrue(actual);
    }

    @Test
    public void validateRemoveHorseFailScenarioTest() {
        String id = "12_ 3";

        boolean actual = validator.validateRemoveHorse(id);
        Assert.assertFalse(actual);
    }
}
