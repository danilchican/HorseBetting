package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.PageReceiver;
import com.epam.horsebetting.request.RequestContent;

public class PageReceiverImpl extends AbstractReceiver implements PageReceiver {

    /**
     * Present index page with collected data.
     *
     * @param content
     */
    @Override
    public void presentIndexPage(RequestContent content) {
        this.setPageSubTitle("Главная");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present login page.
     *
     * @param content
     */
    @Override
    public void presentLoginPage(RequestContent content) {
        this.setPageSubTitle("Авторизация");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present register page.
     *
     * @param content
     */
    @Override
    public void presentRegisterPage(RequestContent content) {
        this.setPageSubTitle("Регистрация");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present profile page.
     *
     * @param content
     */
    public void presentProfilePage(RequestContent content) {
        this.setPageSubTitle("Личный кабинет");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard page.
     *
     * @param content
     */
    public void presentDashboardPage(RequestContent content) {
        this.setPageSubTitle("Админ-панель");
        this.setDefaultContentAttributes(content);
    }
}
