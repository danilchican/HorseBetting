package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.util.Locale;

import static com.epam.horsebetting.validator.CommonValidator.COMMON_PAGE;

public class SuitValidator extends AbstractValidator {

    /**
     * Default constructor.
     *
     * @param locale
     */
    public SuitValidator(Locale locale) {
        super(locale);
    }

    /**
     * Constants of suit validation data.
     */
    private static final String PREFIX = "suit.";
    private static final String SUIT_ID = PREFIX + "id";
    private static final String SUIT_NAME = PREFIX + "name";

    /**
     * Validate number of page.
     *
     * @param page
     * @return boolean
     */
    public boolean validatePage(String page) {
        return validateInteger(page, RequestFieldConfig.Common.PAGE_FIELD, COMMON_PAGE, false);
    }

    /**
     * Validate name.
     *
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        return validateString(name, RequestFieldConfig.Suit.NAME_FIELD, SUIT_NAME, false, DEFAULT_STRING_REGEX);
    }

    /**
     * Validate id number.
     *
     * @param id
     * @return boolean
     */
    public boolean validateId(String id) {
        return validateInteger(id, RequestFieldConfig.Suit.ID_FIELD, SUIT_ID, false);
    }

    /**
     * Validate suit.
     *
     * @param id
     * @param name
     * @return boolean
     */
    public boolean validateSuit(String id, String name) {
        boolean isValidate = true;

        if (!validateId(id)) {
            isValidate = false;
        }

        if (!validateName(name)) {
            isValidate = false;
        }

        return isValidate;
    }
}
