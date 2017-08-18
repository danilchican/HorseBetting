package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.tag.OldInputFormAttributeTag;
import com.epam.horsebetting.type.HorseGenderType;

import java.util.Locale;

public class HorseValidator extends AbstractValidator {

    /**
     * Constant values.
     */
    private static final byte MIN_AGE = 1;
    private static final byte MAX_AGE = 40;

    /**
     * Default constructor.
     *
     * @param locale
     */
    public HorseValidator(Locale locale) {
        super(locale);
    }

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

        if (!validateName(name, RequestFieldConfig.Horse.NAME_FIELD, "horse.name")) {
            isValidate = false;
        }

        if (!validateGender(gender, RequestFieldConfig.Horse.GENDER_FIELD, "horse.gender")) {
            isValidate = false;
        }

        if (!validateAge(age, RequestFieldConfig.Horse.AGE_FIELD, "horse.age")) {
            isValidate = false;
        }

        if (!validateSuitId(suitId, RequestFieldConfig.Horse.SUIT_FIELD, "horse.suit_id")) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate data to update horse.
     *
     * @param id
     * @param name
     * @param gender
     * @param age
     * @param suitId
     * @return boolean
     */
    public boolean validateUpdateHorse(String id, String name, String gender, String age, String suitId) {
        boolean isValidate = true;

        if (!validateInteger(id, RequestFieldConfig.Horse.ID_FIELD, "horse.id", false)) {
            isValidate = false;
        }

        if (!validateName(name, RequestFieldConfig.Horse.NAME_FIELD, "horse.name")) {
            isValidate = false;
        }

        if (!validateGender(gender, RequestFieldConfig.Horse.GENDER_FIELD, "horse.gender")) {
            isValidate = false;
        }

        if (!validateAge(age, RequestFieldConfig.Horse.AGE_FIELD, "horse.age")) {
            isValidate = false;
        }

        if (!validateSuitId(suitId, RequestFieldConfig.Horse.SUIT_FIELD, "horse.suit_id")) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate remove horse data.
     *
     * @param id
     * @return boolean
     */
    public boolean validateRemoveHorse(String id) {
        return validateInteger(id, RequestFieldConfig.Horse.ID_FIELD, "horse.id", true);
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
        return validateString(name, attributeName, key, true, DEFAULT_NAME_REGEX);
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
        try {
            if (age != null && !age.trim().isEmpty()) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, age);

                byte b = Byte.parseByte(age);

                if (b < MIN_AGE || b > MAX_AGE) {
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + ".less")
                            + " " + MIN_AGE + ".." + MAX_AGE);
                    return false;
                }

                return true;
            }

            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_REQUIRED));
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_INCORRECT));
            return false;
        }
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
        return validateInteger(suitId, attributeName, key, true);
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

            if (!HorseGenderType.contains(gender)) {
                this.addErrorMessage(VALIDATION_PREFIX + key + VALIDATION_INCORRECT);
                return false;
            }

            return true;
        }

        this.addErrorMessage(VALIDATION_PREFIX + key + VALIDATION_REQUIRED);
        return false;
    }
}
