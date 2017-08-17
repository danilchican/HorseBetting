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

        if (!validateInteger(participantId, RequestFieldConfig.Bet.PARTICIPANT_ID, "bets.participant_id", false)) {
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
        return validateInteger(id, RequestFieldConfig.Bet.ID, "bets.id", false);
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
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "bets.amount.greater")
                        + " " + MIN_RATE + "$.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "bets.amount.incorrect"));
            return false;
        }
    }

}
