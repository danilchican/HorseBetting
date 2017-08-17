package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

public class RaceValidator extends AbstractValidator {

    /**
     *
     */
    private static final int TOTAL_ATTRIBUTES = 6;
    private static final int TITLE_INDEX = 0;
    private static final int PLACE_INDEX = 1;
    private static final int MIN_RATE_INDEX = 2;
    private static final int TRACK_LENGTH_INDEX = 3;
    private static final int BET_END_DATE_INDEX = 4;
    private static final int STARTED_AT_INDEX = 5;

    public boolean validateCreateRaceForm(String ... attributes) {
        boolean isValidate = true;

        // TODO finish validate
        if(attributes.length < TOTAL_ATTRIBUTES) {
            return false;
        }

        if (!validateTitle(attributes[TITLE_INDEX], RequestFieldConfig.Race.TITLE_FIELD, "Title")) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate edit race form.
     *
     * @param title
     * @param jockeys
     * @param coeffs
     * @return boolean
     */
    public boolean validateEditRaceForm(String title, String[] jockeys, String[] coeffs) {
        boolean isValidate = true;

//        if (!validateTitle(title, RequestFieldConfig.Race.TITLE_FIELD, "Title")) {
//            isValidate = false;
//        }

        // TODO finish validate

        return isValidate;
    }

    /**
     * Validate jockeys.
     *
     * @param jockeys
     * @param coeffs
     * @return boolean
     */
    public boolean validateJockeys(String[] jockeys, String[] coeffs) {
        boolean isValidate = true;

        if(jockeys.length != coeffs.length) {
            this.addErrorMessage("Jockeys data is invalid.");
            return false;
        }

        // TODO validate jockeys int & dig decimal

        return isValidate;
    }

    /**
     * Validate title of race.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateTitle(String name, String attributeName, String key) {
        return validateDefaultName(name, attributeName, key, "can contains only characters, numbers and spaces.", true);
    }
}
