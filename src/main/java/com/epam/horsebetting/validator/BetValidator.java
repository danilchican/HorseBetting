package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static com.epam.horsebetting.validator.RaceValidator.MIN_RATE;

public class BetValidator extends AbstractValidator {

    /**
     * Default constructor.
     *
     * @param locale
     */
    public BetValidator(Locale locale) {
        super(locale);
    }

    /**
     * Constants of bet validation data.
     */
    private static final String PREFIX = "bets.";
    private static final String BET_ID = PREFIX + "id";
    private static final String BET_AMOUNT_GREATER = PREFIX + "amount.greater";
    private static final String BET_AMOUNT_INCORRECT = PREFIX + "amount.incorrect";
    private static final String BET_PARTICIPANT_ID = PREFIX + "participant_id";

    /**
     * Validate create bet.
     *
     * @param amount
     * @param participantId
     * @return boolean
     */
    public boolean validateCreateBet(String amount, String participantId) {
        boolean isValidate = true;

        if (!validateAmount(amount)) {
            isValidate = false;
        }

        if (!validateInteger(participantId, RequestFieldConfig.Bet.PARTICIPANT_ID, BET_PARTICIPANT_ID, false)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate bet id.
     *
     * @param id
     * @return boolean
     */
    public boolean validateId(String id) {
        return validateInteger(id, RequestFieldConfig.Bet.ID, BET_ID, false);
    }

    /**
     * Validate amount.
     *
     * @param number
     * @return
     */
    private boolean validateAmount(String number) {
        try {
            BigDecimal n = new BigDecimal(number);

            if (MIN_RATE.compareTo(n) == 1) {
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + BET_AMOUNT_GREATER)
                        + " " + MIN_RATE + "$.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + BET_AMOUNT_INCORRECT));
            return false;
        }
    }

}
