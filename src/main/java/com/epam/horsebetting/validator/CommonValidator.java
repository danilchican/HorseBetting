package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.util.Locale;

public class CommonValidator extends AbstractValidator {

    /**
     * Default constructor.
     *
     * @param locale
     */
    public CommonValidator(Locale locale) {
        super(locale);
    }

    /**
     * Constants of common validation data.
     */
    private static final String PREFIX = "common.";
    static final String COMMON_PAGE = PREFIX + "page";
    private static final String COMMON_ID = PREFIX + "id";

    /**
     * Validate page number.
     *
     * @param pageNum
     * @return boolean
     */
    public boolean validatePage(String pageNum) {
        return pageNum == null
                || validateInteger(pageNum, RequestFieldConfig.Common.PAGE_FIELD, COMMON_PAGE, false);
    }

    /**
     * Validate id number.
     *
     * @param idNum
     * @return boolean
     */
    public boolean validateId(String idNum) {
        return validateInteger(idNum, RequestFieldConfig.Common.REQUEST_ID, COMMON_ID, false);
    }
}
