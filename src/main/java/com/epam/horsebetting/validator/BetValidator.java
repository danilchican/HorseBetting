package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.math.BigDecimal;

public class BetValidator extends AbstractValidator {

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

        if (!validateInteger(participantId, RequestFieldConfig.Bet.PARTICIPANT_ID, "Participant id", false)) {
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
        return validateInteger(id, RequestFieldConfig.Bet.ID, "Id", false);
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
            return true;
        } catch (NumberFormatException e) {
            this.addErrorMessage("Amount is incorrect.");
            return false;
        }
    }

}
