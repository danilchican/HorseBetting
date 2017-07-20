package com.epam.horsebetting.receiver;

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
}
