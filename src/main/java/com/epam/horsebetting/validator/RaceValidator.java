package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.tag.OldInputFormAttributeTag;
import com.epam.horsebetting.type.RaceStatusType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RaceValidator extends AbstractValidator {

    /**
     * Constant values.
     */
    static final BigDecimal MIN_RATE = BigDecimal.valueOf(20);
    private static final BigDecimal MAX_COEFFICIENT = BigDecimal.valueOf(30);
    private static final BigDecimal MIN_COEFFICIENT = BigDecimal.valueOf(1);
    private static final int TOTAL_ATTRIBUTES = 6;
    private static final int MIN_JOCKEYS_COUNT = 5;
    private static final int MIN_TRACK_LENGTH = 100;

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
     * Default constructor.
     *
     * @param locale
     */
    public RaceValidator(Locale locale) {
        super(locale);
    }

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
                "race.title", true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateString(attributes[PLACE_INDEX], RequestFieldConfig.Race.PLACE_FIELD,
                "race.place", true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateRate(attributes[MIN_RATE_INDEX], RequestFieldConfig.Race.MIN_RATE_FIELD,
                "race.min_rate", true)) {
            isValidate = false;
        }

        if (!validateTrackLength(attributes[TRACK_LENGTH_INDEX], RequestFieldConfig.Race.TRACK_LENGTH_FIELD,
                "race.track_length", true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[BET_END_DATE_INDEX], RequestFieldConfig.Race.BET_END_DATE_FIELD,
                "race.bet_end_date", true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[STARTED_AT_INDEX], RequestFieldConfig.Race.STARTED_AT_FIELD,
                "race.started_at", true)) {
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

        if (!validateStatus(status, "race.status")) {
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
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys" + VALIDATION_REQUIRED));
            return false;
        }

        if (jockeys.length < MIN_JOCKEYS_COUNT) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys.least"));
            return false;
        }

        Set<String> localSet = new HashSet<>(Arrays.asList(jockeys));

        if (localSet.size() < jockeys.length) {
            isValidate = false;
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys.duplicated"));
        }

        while (i < jockeys.length && isValidate) {
            if (!jockeys[i].matches(DEFAULT_INTEGER_REGEX)) {
                isValidate = false;
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys" + VALIDATION_REQUIRED));
            }

            if (isValidate && !coeffs[i].matches(DEFAULT_BIG_DECIMAL_REGEX)) {
                isValidate = false;
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys.coefficients" + VALIDATION_INCORRECT));
            } else if (isValidate) {
                BigDecimal num = new BigDecimal(coeffs[i]);

                if (MAX_COEFFICIENT.compareTo(num) != 1 || MIN_COEFFICIENT.compareTo(num) != -1) {
                    isValidate = false;
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "race.jockeys.coefficients.range")
                            + " " + MIN_COEFFICIENT + ".." + MAX_COEFFICIENT);
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
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_REQUIRED));
                return false;
            }

            return true;
        }

        this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_REQUIRED));
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
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + ".greater")
                            + " " + MIN_RATE + "$.");
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
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + ".greater")
                            + " " + MIN_TRACK_LENGTH + "(m).");
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
}
