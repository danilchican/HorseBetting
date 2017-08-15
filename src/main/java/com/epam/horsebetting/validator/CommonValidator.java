package com.epam.horsebetting.validator;

public class CommonValidator extends AbstractValidator {

    /**
     * Validate page number.
     *
     * @param pageNum
     * @return boolean
     */
    public boolean validatePage(String pageNum) {
        return pageNum == null || validateInteger(pageNum, "page", "Page", false);
    }

    /**
     * Validate id number.
     *
     * @param idNum
     * @return boolean
     */
    public boolean validateId(String idNum) {
        return validateInteger(idNum, "id", "Id", false);
    }
}
