package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface PageReceiver {

    /**
     * Present index page.
     *
     * @param content
     */
    void presentIndexPage(RequestContent content);

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
     * Present profile page.
     *
     * @param content
     */
    void presentProfilePage(RequestContent content);

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
     * Present dashboard races page.
     *
     * @param content
     */
    void presentDashboardRacesPage(RequestContent content) throws ReceiverException;

    /**
     * Present dashboard suits page.
     *
     * @param content
     */
    void presentDashboardSuitsPage(RequestContent content) throws ReceiverException;
}
