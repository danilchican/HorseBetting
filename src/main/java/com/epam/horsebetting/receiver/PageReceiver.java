package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface PageReceiver {

    /**
     * Present index page.
     *
     * @param content
     */
    void presentIndexPage(RequestContent content) throws ReceiverException;

    /**
     * Present races page.
     *
     * @param content
     */
    void presentRacesPage(RequestContent content) throws ReceiverException;

    /**
     * Present race view page.
     *
     * @param content
     */
    void presentRaceViewPage(RequestContent content) throws ReceiverException;

    /**
     * Present login page.
     *
     * @param content
     */
    void presentLoginPage(RequestContent content);

    /**
     * Present register page.
     *
     * @param content
     */
    void presentRegisterPage(RequestContent content);

    /**
     * Present reset password page.
     *
     * @param content
     */
    void presentResetPasswordPage(RequestContent content);

    /**
     * Present password to reset by link page.
     *
     * @param content
     */
    void presentResetLinkPasswordPage(RequestContent content) throws ReceiverException;

    /**
     * Present profile page.
     *
     * @param content
     */
    void presentProfilePage(RequestContent content) throws ReceiverException;

    /**
     * Present profile settings page.
     *
     * @param content
     */
    void presentProfileSettingsPage(RequestContent content);

    /**
     * Present profile payment page.
     *
     * @param content
     */
    void presentProfilePaymentPage(RequestContent content);

    /**
     * Present profile bets page.
     *
     * @param content
     */
    void presentProfileBetsPage(RequestContent content) throws ReceiverException;

    /**
     * Present profile view bet page.
     *
     * @param content
     */
    void presentProfileViewBetPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard page.
     *
     * @param content
     */
    void presentDashboardPage(RequestContent content);

    /**
     * Present dashboard users page.
     *
     * @param content
     */
    void presentDashboardUsersPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard suits page.
     *
     * @param content
     */
    void presentDashboardSuitsPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard horses page.
     *
     * @param content
     */
    void presentDashboardHorsesPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard horse create page.
     *
     * @param content
     */
    void presentDashboardHorseCreatePage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard horse edit page.
     *
     * @param content
     */
    void presentDashboardHorseEditPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard races page.
     *
     * @param content
     */
    void presentDashboardRacesPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard race create page.
     *
     * @param content
     */
    void presentDashboardRaceCreatePage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard race edit page.
     *
     * @param content
     */
    void presentDashboardRaceEditPage(RequestContent content) throws ReceiverException;
}
