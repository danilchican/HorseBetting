package com.epam.danilchican.betting.receiver;

import com.epam.danilchican.betting.request.RequestContent;

public class PageReceiver extends AbstractReceiver {

    /**
     * Present index page with collected data.
     *
     * @param content
     */
    public void presentIndexPage(RequestContent content) {
        this.setPageSubTitle("Главная");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present login page.
     *
     * @param content
     */
    public void presentLoginPage(RequestContent content) {
        this.setPageSubTitle("Авторизация");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present login page.
     *
     * @param content
     */
    public void presentRegisterPage(RequestContent content) {
        this.setPageSubTitle("Регистрация");
        this.setDefaultContentAttributes(content);
    }
}
