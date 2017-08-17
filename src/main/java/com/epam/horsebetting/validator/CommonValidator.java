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
     * Validate page number.
     *
     * @param pageNum
     * @return boolean
     */
    public boolean validatePage(String pageNum) {
        return pageNum == null
                || validateInteger(pageNum, RequestFieldConfig.Common.PAGE_FIELD, "common.page", false);
    }

    /**
     * Validate id number.
     *
     * @param idNum
     * @return boolean
     */
    public boolean validateId(String idNum) {
        return validateInteger(idNum, RequestFieldConfig.Common.REQUEST_ID, "common.id", false);
    }
}
