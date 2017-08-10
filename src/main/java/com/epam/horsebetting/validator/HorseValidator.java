package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;

public class HorseValidator extends AbstractValidator {

    /**
     * Validate data to create horse.
     *
     * @param name
     * @param gender
     * @param age
     * @param suitId
     * @return boolean
     */
    public boolean validateCreateHorse(String name, String gender, String age, String suitId) {
        boolean isValidate = true;

        if (!validateName(name, "horse-name", "Name")) {
            isValidate = false;
        }

        if (!validateGender(gender, "gender", "Gender")) {
            isValidate = false;
        }

        if (!validateAge(age, "horse-age", "Age")) {
            isValidate = false;
        }

        if (!validateSuitId(suitId, "horse-suit", "Suit id")) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate name.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateName(String name, String attributeName, String key) {
        return validateDefaultName(name, attributeName, key, "can contains only characters, numbers and spaces.");
    }

    /**
     * Validate horse age.
     *
     * @param age
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateAge(String age, String attributeName, String key) {
        return validateByte(age, attributeName, key);
    }

    /**
     * Validate suit id to horse.
     *
     * @param suitId
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateSuitId(String suitId, String attributeName, String key) {
        return validateInteger(suitId, attributeName, key);
    }

    /**
     * Validate horse gender.
     *
     * @param gender
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateGender(String gender, String attributeName, String key) {
        if (gender != null && !gender.trim().isEmpty()) {
            this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, gender);

            if (!gender.equals("male") && !gender.equals("female")) {
                this.addErrorMessage(key + " is invalid.");
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }
}
