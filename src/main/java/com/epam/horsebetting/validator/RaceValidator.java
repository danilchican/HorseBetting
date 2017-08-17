package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.tag.OldInputFormAttributeTag;
import com.epam.horsebetting.type.RaceStatusType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RaceValidator extends AbstractValidator {

    /**
     * Constant values.
     */
    private static final int TOTAL_ATTRIBUTES = 6;
    private static final int MIN_JOCKEYS_COUNT = 5;
    private static final int MIN_TRACK_LENGTH = 100;
    private static final BigDecimal MIN_RATE = BigDecimal.valueOf(20);
    private static final BigDecimal MAX_COEFFICIENT = BigDecimal.valueOf(30);
    private static final BigDecimal MIN_COEFFICIENT = BigDecimal.valueOf(1);
    private static final String DEFAULT_STRING_MESSAGE = "should contain only numbers, characters, spaces and punctuation";

    /**
     * Indexes of attributes.
     */
    private static final int TITLE_INDEX = 0;
    private static final int PLACE_INDEX = 1;
    private static final int MIN_RATE_INDEX = 2;
    private static final int TRACK_LENGTH_INDEX = 3;
    private static final int BET_END_DATE_INDEX = 4;
    private static final int STARTED_AT_INDEX = 5;

    /**
     * Validate create race form.
     *
     * @param attributes
     * @return boolean
     */
    public boolean validateCreateRaceForm(String... attributes) {
        boolean isValidate = true;

        if (attributes.length < TOTAL_ATTRIBUTES) {
            return false;
        }

        if (!validateString(attributes[TITLE_INDEX], RequestFieldConfig.Race.TITLE_FIELD,
                "Title", DEFAULT_STRING_MESSAGE, true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateString(attributes[PLACE_INDEX], RequestFieldConfig.Race.PLACE_FIELD,
                "Place", DEFAULT_STRING_MESSAGE, true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateRate(attributes[MIN_RATE_INDEX], RequestFieldConfig.Race.MIN_RATE_FIELD,
                "Min rate", true)) {
            isValidate = false;
        }

        if (!validateTrackLength(attributes[TRACK_LENGTH_INDEX], RequestFieldConfig.Race.TRACK_LENGTH_FIELD,
                "Track length", true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[BET_END_DATE_INDEX], RequestFieldConfig.Race.BET_END_DATE_FIELD,
                "Bet end date", true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[STARTED_AT_INDEX], RequestFieldConfig.Race.STARTED_AT_FIELD,
                "Started at", true)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate edit race form.
     *
     * @param status
     * @param jockeys
     * @param coeffs
     * @return boolean
     */
    public boolean validateEditRaceForm(String status, String[] jockeys, String[] coeffs) {
        boolean isValidate = true;

        if (!validateStatus(status, "Status")) {
            isValidate = false;
        }

        if (!validateJockeys(jockeys, coeffs)) {
            isValidate = false;
        }

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
        int i = 0;

        if ((jockeys == null || coeffs == null) || jockeys.length != coeffs.length) {
            this.addErrorMessage("Jockeys not selected. Race should have at least 6 jockeys.");
            return false;
        }

        if (jockeys.length < MIN_JOCKEYS_COUNT) {
            this.addErrorMessage("Race should have at least 6 jockeys.");
            return false;
        }

        Set<String> localSet = new HashSet<>(Arrays.asList(jockeys));

        if (localSet.size() < jockeys.length) {
            isValidate = false;
            this.addErrorMessage("Jockeys are duplicated. The race should have only unique jockeys.");
        }

        while (i < jockeys.length && isValidate) {
            if (!jockeys[i].matches(DEFAULT_INTEGER_REGEX)) {
                isValidate = false;
                this.addErrorMessage("Jockeys are not selected.");
            }

            if (isValidate && !coeffs[i].matches(DEFAULT_BIG_DECIMAL_REGEX)) {
                isValidate = false;
                this.addErrorMessage("Jockey coefficients is invalid.");
            } else if (isValidate) {
                BigDecimal num = new BigDecimal(coeffs[i]);

                if (MAX_COEFFICIENT.compareTo(num) != 1 || MIN_COEFFICIENT.compareTo(num) != -1) {
                    isValidate = false;
                    this.addErrorMessage("Jockey coefficient should be in range " +
                            MIN_COEFFICIENT + ".." + MAX_COEFFICIENT + ".");
                }
            }

            i++;
        }

        return isValidate;
    }

    /**
     * Validate status of the race.
     *
     * @param status
     * @param key
     * @return boolean
     */
    private boolean validateStatus(String status, String key) {
        if (status != null && !status.trim().isEmpty()) {
            if (!RaceStatusType.contains(status)) {
                this.addErrorMessage("Pick valid status of the race.");
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is empty.");
        return false;
    }

    /**
     * Validate rate of race.
     *
     * @param number
     * @param attributeName
     * @param key
     * @param saveInput
     * @return boolean
     */
    private boolean validateRate(String number, String attributeName, String key, boolean saveInput) {
        try {
            if (number != null && !number.trim().isEmpty()) {
                if (saveInput) {
                    this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, number);
                }

                BigDecimal n = new BigDecimal(number);

                if (MIN_RATE.compareTo(n) == 1) {
                    this.addErrorMessage(key + " should be greater than " + MIN_RATE + ".");
                    return false;
                }
                return true;
            }

            this.addErrorMessage(key + " is empty.");
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(key + " is incorrect.");
            return false;
        }
    }

    /**
     * Validate track length of race.
     *
     * @param number
     * @param attributeName
     * @param key
     * @param saveInput
     * @return boolean
     */
    private boolean validateTrackLength(String number, String attributeName, String key, boolean saveInput) {
        try {
            if (number != null && !number.trim().isEmpty()) {
                if (saveInput) {
                    this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, number);
                }

                int n = Integer.parseInt(number);

                if (n < MIN_TRACK_LENGTH) {
                    this.addErrorMessage(key + " should be greater than " + MIN_TRACK_LENGTH + "(m).");
                    return false;
                }
                return true;
            }

            this.addErrorMessage(key + " is empty.");
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(key + " is incorrect.");
            return false;
        }
    }
}
