package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.PageReceiver;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class PageReceiverImpl extends AbstractReceiver implements PageReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

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
    @Override
    public void presentProfilePage(RequestContent content) {
        this.setPageSubTitle("Личный кабинет");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard page.
     *
     * @param content
     */
    @Override
    public void presentDashboardPage(RequestContent content) {
        this.setPageSubTitle("Админ-панель");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard users page.
     *
     * @param content
     */
    @Override
    public void presentDashboardUsersPage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Пользователи");
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard horses page.
     *
     * @param content
     */
    @Override
    public void presentDashboardHorsesPage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Лошади");
        this.setDefaultContentAttributes(content);

        final int limit = 10;

        String pageNum = content.findParameter("page");

        try (HorseDAOImpl horseDAO = new HorseDAOImpl()) {
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;
            final int totalHorses = horseDAO.getTotalCount();

            List<Horse> horses = horseDAO.obtainPart(limit, offset);

            content.insertRequestAttribute("horses", horses);
            content.insertRequestAttribute("totalHorses", totalHorses);
            content.insertRequestAttribute("limitHorses", limit);

            LOGGER.log(Level.DEBUG, "Horses list: " + Arrays.toString(horses.toArray()));
        } catch (NumberFormatException e) {
            throw new ReceiverException("Cannot convert page to number. GET[page]=" + e.getMessage(), e);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }
}
