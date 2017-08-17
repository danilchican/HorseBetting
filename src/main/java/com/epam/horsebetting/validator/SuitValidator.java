package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.util.Locale;

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
     * Validate number of page.
     *
     * @param page
     * @return boolean
     */
    public boolean validatePage(String page) {
        return validateInteger(page, RequestFieldConfig.Common.PAGE_FIELD, "common.page", false);
    }

    /**
     * Validate name.
     *
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        return validateString(name, RequestFieldConfig.Suit.NAME_FIELD, "suit.name", false, DEFAULT_STRING_REGEX);
    }

    /**
     * Validate id number.
     *
     * @param id
     * @return boolean
     */
    public boolean validateId(String id) {
        return validateInteger(id, RequestFieldConfig.Suit.ID_FIELD, "suit.id", false);
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
