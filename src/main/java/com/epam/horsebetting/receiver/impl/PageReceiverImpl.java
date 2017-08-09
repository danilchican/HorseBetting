package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.dao.impl.RaceDAOImpl;
import com.epam.horsebetting.dao.impl.SuitDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.entity.Suit;
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
     * Present dashboard suits page.
     *
     * @param content
     */
    @Override
    public void presentDashboardSuitsPage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Масти лошадей");
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

        // TODO create validators

        String pageNum = content.findParameter("page");

        HorseDAOImpl horseDAO = new HorseDAOImpl(true);

        TransactionManager transaction = new TransactionManager(horseDAO);
        transaction.beginTransaction();

        try {
            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;
            final int totalHorses = horseDAO.getTotalCount();

            List<Horse> horses = horseDAO.obtainPart(limit, offset);

            transaction.commit();

            content.insertRequestAttribute("horses", horses);
            content.insertRequestAttribute("totalHorses", totalHorses);
            content.insertRequestAttribute("limitHorses", limit);

            LOGGER.log(Level.DEBUG, "Horses list: " + Arrays.toString(horses.toArray()));
        } catch (NumberFormatException e) {
            transaction.rollback();
            throw new ReceiverException("Cannot convert page to number. GET[page]=" + e.getMessage(), e);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard horse create page.
     *
     * @param content
     */
    @Override
    public void presentDashboardHorseCreatePage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Создание лошади");
        this.setDefaultContentAttributes(content);

        try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
            List<Suit> suits = suitDAO.findAll();
            content.insertRequestAttribute("suits", suits);

            LOGGER.log(Level.DEBUG, "Suits list: " + Arrays.toString(suits.toArray()));
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }

    /**
     * Present dashboard horse edit page.
     *
     * @param content
     */
    @Override
    public void presentDashboardHorseEditPage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Редактирование лошади");
        this.setDefaultContentAttributes(content);

        // TODO create validators

        String idNum = content.findParameter("id");

        HorseDAOImpl horseDAO = new HorseDAOImpl(true);
        SuitDAOImpl suitDAO = new SuitDAOImpl(true);

        TransactionManager transaction = new TransactionManager(horseDAO, suitDAO);
        transaction.beginTransaction();

        try {
            final int id = idNum != null ? Integer.parseInt(idNum) : 1;
            Horse horse = horseDAO.find(id);

            if(horse == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find horse with id=" + id);
            }

            content.insertRequestAttribute("horse", horse);
            LOGGER.log(Level.DEBUG, "Editing horse: " + horse);

            List<Suit> suits = suitDAO.findAll();
            content.insertRequestAttribute("suits", suits);

            transaction.commit();

            LOGGER.log(Level.DEBUG, "Suits list: " + Arrays.toString(suits.toArray()));
        } catch (NumberFormatException e) {
            transaction.rollback();
            throw new ReceiverException("Cannot convert page to number. GET[page]=" + e.getMessage(), e);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard races page.
     *
     * @param content
     */
    @Override
    public void presentDashboardRacesPage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Скачки");
        this.setDefaultContentAttributes(content);

        // TODO create validators

        String pageNum = content.findParameter("page");

        RaceDAOImpl raceDAO = new RaceDAOImpl(true);

        TransactionManager transaction = new TransactionManager(raceDAO);
        transaction.beginTransaction();

        try {
            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;
            final int totalRaces = raceDAO.getTotalCount();

            List<Race> races = raceDAO.obtainPart(limit, offset);

            transaction.commit();

            content.insertRequestAttribute("races", races);
            content.insertRequestAttribute("totalRaces", totalRaces);
            content.insertRequestAttribute("limitRaces", limit);

            LOGGER.log(Level.DEBUG, "Races list: " + Arrays.toString(races.toArray()));
        } catch (NumberFormatException e) {
            transaction.rollback();
            throw new ReceiverException("Cannot convert page to number. GET[page]=" + e.getMessage(), e);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard race create page.
     *
     * @param content
     */
    @Override
    public void presentDashboardRaceCreatePage(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Создание забега");
        this.setDefaultContentAttributes(content);
    }
}
