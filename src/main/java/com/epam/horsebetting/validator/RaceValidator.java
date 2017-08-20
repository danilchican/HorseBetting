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
     * Constants of race validation data.
     */
    private static final String PREFIX = "race.";
    private static final String RACE_JOCKEYS_PREFIX = PREFIX + "jockeys";
    private static final String RACE_JOCKEYS_LEAST = RACE_JOCKEYS_PREFIX + ".least";
    private static final String RACE_JOCKEYS_DUPLICATED = RACE_JOCKEYS_PREFIX + ".duplicated";
    private static final String RACE_JOCKEYS_COEFFICIENTS = RACE_JOCKEYS_PREFIX + ".coefficients";
    private static final String RACE_JOCKEYS_COEFFICIENTS_RANGE = RACE_JOCKEYS_COEFFICIENTS + ".range";
    private static final String RACE_TITLE = PREFIX + "title";
    private static final String RACE_PLACE = PREFIX + "place";
    private static final String RACE_MIN_RATE = PREFIX + "min_rate";
    private static final String RACE_TRACK_LENGTH = PREFIX + "track_length";
    private static final String RACE_GREATER = ".greater";
    private static final String RACE_BET_END_DATE = PREFIX + "bet_end_date";
    private static final String RACE_STARTED_AT = PREFIX + "started_at";
    private static final String RACE_STATUS = PREFIX + "status";
    private static final String RACE_WINNER = PREFIX + "winner";
    private static final String RACE_ID = PREFIX + "id";

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
                RACE_TITLE, true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateString(attributes[PLACE_INDEX], RequestFieldConfig.Race.PLACE_FIELD,
                RACE_PLACE, true, DEFAULT_STRING_REGEX)) {
            isValidate = false;
        }

        if (!validateRate(attributes[MIN_RATE_INDEX], RequestFieldConfig.Race.MIN_RATE_FIELD,
                RACE_MIN_RATE, true)) {
            isValidate = false;
        }

        if (!validateTrackLength(attributes[TRACK_LENGTH_INDEX], RequestFieldConfig.Race.TRACK_LENGTH_FIELD,
                RACE_TRACK_LENGTH, true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[BET_END_DATE_INDEX], RequestFieldConfig.Race.BET_END_DATE_FIELD,
                RACE_BET_END_DATE, true)) {
            isValidate = false;
        }

        if (!validateDate(attributes[STARTED_AT_INDEX], RequestFieldConfig.Race.STARTED_AT_FIELD,
                RACE_STARTED_AT, true)) {
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
    public boolean validateEditRaceForm(String status, String winnerNum, String raceNum, String[] jockeys, String[] coeffs) {
        boolean isValidate = true;

        if (!validateStatus(status, RACE_STATUS)) {
            isValidate = false;
        }

        if (!validateWinner(winnerNum, RACE_WINNER)) {
            isValidate = false;
        }

        if (!validateInteger(raceNum, RequestFieldConfig.Race.ID_FIELD, RACE_ID, false)) {
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
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_PREFIX + VALIDATION_REQUIRED));
            return false;
        }

        if (jockeys.length < MIN_JOCKEYS_COUNT) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_LEAST));
            return false;
        }

        Set<String> localSet = new HashSet<>(Arrays.asList(jockeys));

        if (localSet.size() < jockeys.length) {
            isValidate = false;
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_DUPLICATED));
        }

        while (i < jockeys.length && isValidate) {
            if (!jockeys[i].matches(DEFAULT_INTEGER_REGEX)) {
                isValidate = false;
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_PREFIX + VALIDATION_REQUIRED));
            }

            if (isValidate && !coeffs[i].matches(DEFAULT_BIG_DECIMAL_REGEX)) {
                isValidate = false;
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_COEFFICIENTS + VALIDATION_INCORRECT));
            } else if (isValidate) {
                BigDecimal num = new BigDecimal(coeffs[i]);

                if (MAX_COEFFICIENT.compareTo(num) != 1 || MIN_COEFFICIENT.compareTo(num) != -1) {
                    isValidate = false;
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + RACE_JOCKEYS_COEFFICIENTS_RANGE)
                            + " " + MIN_COEFFICIENT + ".." + MAX_COEFFICIENT);
                }
            }

            i++;
        }

        return isValidate;
    }

    /**
     * Validate required winner.
     *
     * @param winnerNum
     * @return boolean
     */
    public boolean validateRequiredWinner(String winnerNum) {
        return validateInteger(winnerNum, RequestFieldConfig.Race.WINNER_FIELD, RACE_WINNER, false);
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
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_INCORRECT));
                return false;
            }
        }

        return true;
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
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + RACE_GREATER)
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
                    this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + RACE_GREATER)
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

    /**
     * Validate winner number.
     *
     * @param number
     * @param key
     * @return boolean
     */
    private boolean validateWinner(String number, String key) {
        try {
            if (number != null && !number.trim().isEmpty()) {
                int n = Integer.parseInt(number);
            }

            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_INCORRECT));
            return false;
        }
    }
}
