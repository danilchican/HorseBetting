package com.epam.horsebetting.validator;

public class SuitValidator extends AbstractValidator {

    /**
     * Regular expressions for variables.
     */
    private static final String NAME_REGEX = "[a-zA-Zа-яА-ЯёЁ0-9 ]+";

    /**
     * Validate number of page.
     *
     * @param page
     * @return boolean
     */
    public boolean validatePage(String page) {
        try {
            int pageNumber = Integer.parseInt(page);
            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage("Page number is incorrent.");
            return false;
        }
    }

    /**
     * Validate name.
     *
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            if (!name.matches(NAME_REGEX)) {
                this.addErrorMessage("Name can contains only characters, numbers and spaces.");
                return false;
            }

            return true;
        }

        this.addErrorMessage("Name is required.");
        return false;
    }

    /**
     * Validate id number.
     *
     * @param id
     * @return boolean
     */
    public boolean validateId(String id) {
        try {
            int idNumber = Integer.parseInt(id);
            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage("Id number is incorrect.");
            return false;
        }
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
