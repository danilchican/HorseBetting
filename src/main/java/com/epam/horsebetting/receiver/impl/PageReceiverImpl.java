package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.*;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.*;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.PageReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.validator.CommonValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;

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
    public void presentIndexPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.index"));
        this.setDefaultContentAttributes(content);

        final int limit = 10;
        final int offset = 0;

        try (RaceDAOImpl raceDAO = new RaceDAOImpl(false)) {
            List<Race> races = raceDAO.obtainNearest(limit, offset);
            content.insertRequestAttribute("races", races);

            LOGGER.log(Level.DEBUG, "Races list: " + Arrays.toString(races.toArray()));
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }

    /**
     * Present races page.
     *
     * @param content
     */
    @Override
    public void presentRacesPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[page=" + pageNum + "] is incorrect.");
        }

        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.races.index"));
        this.setDefaultContentAttributes(content);

        try (RaceDAOImpl raceDAO = new RaceDAOImpl(false)) {
            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;
            final int totalRaces = raceDAO.getTotalCount();

            List<Race> races = raceDAO.obtainPart(limit, offset);

            content.insertRequestAttribute("races", races);
            content.insertRequestAttribute("totalRaces", totalRaces);
            content.insertRequestAttribute("limitRaces", limit);

            LOGGER.log(Level.DEBUG, "Races list: " + Arrays.toString(races.toArray()));
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }

    /**
     * Present race view page.
     *
     * @param content
     */
    @Override
    public void presentRaceViewPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        String idNum = content.findParameter(RequestFieldConfig.Common.REQUEST_ID);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validateId(idNum)) {
            throw new ReceiverException("GET[id=" + idNum + "] is incorrect.");
        }

        RaceDAOImpl raceDAO = new RaceDAOImpl(true);
        ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);

        TransactionManager transaction = new TransactionManager(raceDAO, participantDAO);
        transaction.beginTransaction();

        try {
            final int id = Integer.parseInt(idNum);
            Race race = raceDAO.find(id);

            if (race == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find race with id=" + id);
            }

            List<Participant> participants = participantDAO.findByRaceId(race.getId());

            this.setPageSubTitle(race.getTitle());
            this.setDefaultContentAttributes(content);

            transaction.commit();

            content.insertRequestAttribute("race", race);
            content.insertRequestAttribute("participants", participants);
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
     * Present login page.
     *
     * @param content
     */
    @Override
    public void presentLoginPage(RequestContent content) {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.auth.login"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present register page.
     *
     * @param content
     */
    @Override
    public void presentRegisterPage(RequestContent content) {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.auth.register"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present reset password page.
     *
     * @param content
     */
    @Override
    public void presentResetPasswordPage(RequestContent content) {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.password.reset"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present password to reset by link page.
     *
     * @param content
     */
    @Override
    public void presentResetLinkPasswordPage(RequestContent content) throws ReceiverException {
        String token = content.findParameter(RequestFieldConfig.Common.PASSWORD_RESET_TOKEN);

        if (token == null) {
            throw new ReceiverException("Token does not exist.");
        }

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.password.reset"));
        this.setDefaultContentAttributes(content);

        try (PasswordRecoverDAOImpl recoverDAO = new PasswordRecoverDAOImpl(false)) {
            if (recoverDAO.findByToken(token) == null) {
                throw new ReceiverException("Token expired or does not exist.");
            }

            content.insertRequestAttribute(RequestFieldConfig.Common.PASSWORD_RESET_TOKEN, token);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }

    /**
     * Present profile page.
     *
     * @param content
     */
    @Override
    public void presentProfilePage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));

        this.setPageSubTitle(messageResource.get("page.title.profile.index"));
        this.setDefaultContentAttributes(content);

        try (BetDAOImpl betDAO = new BetDAOImpl(false)) {
            final int id = Integer.parseInt(userIdAttr);
            final int totalUserBets = betDAO.getTotalForUser(id);

            content.insertRequestAttribute("totalUserBets", totalUserBets);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        }
    }

    /**
     * Present profile settings page.
     *
     * @param content
     */
    @Override
    public void presentProfileSettingsPage(RequestContent content) {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.profile.settings"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present profile payment page.
     *
     * @param content
     */
    @Override
    public void presentProfilePaymentPage(RequestContent content) {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.profile.payment"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present profile bets page.
     *
     * @param content
     */
    @Override
    public void presentProfileBetsPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.profile.bets"));
        this.setDefaultContentAttributes(content);

        String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));
        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);

        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[page=" + pageNum + "] is incorrect.");
        }

        BetDAOImpl betDAO = new BetDAOImpl(true);

        TransactionManager transaction = new TransactionManager(betDAO);
        transaction.beginTransaction();

        try {
            final int id = Integer.parseInt(userIdAttr);

            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;

            final int totalBets = betDAO.getTotalForUser(id);

            List<Bet> bets = betDAO.obtainPart(id, limit, offset);
            transaction.commit();

            content.insertRequestAttribute("bets", bets);
            content.insertRequestAttribute("totalBets", totalBets);
            content.insertRequestAttribute("limitBets", limit);

            LOGGER.log(Level.DEBUG, "Bets list: " + Arrays.toString(bets.toArray()));
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
     * Present profile view bet page.
     *
     * @param content
     */
    @Override
    public void presentProfileViewBetPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String idNum = content.findParameter(RequestFieldConfig.Common.REQUEST_ID);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validateId(idNum)) {
            throw new ReceiverException("GET[id=" + idNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.profile.bets.view") + "# " + idNum);
        this.setDefaultContentAttributes(content);

        BetDAOImpl betDAO = new BetDAOImpl(true);
        RaceDAOImpl raceDAO = new RaceDAOImpl(true);
        ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);

        TransactionManager transaction = new TransactionManager(betDAO, raceDAO, participantDAO);
        transaction.beginTransaction();

        try {
            final int id = Integer.parseInt(idNum);
            Bet bet = betDAO.find(id);

            if (bet == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find bet with id=" + id);
            }

            Participant participant = participantDAO.find(bet.getParticipantId());

            if (participant == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find participant with id=" + bet.getParticipantId());
            }

            Race race = raceDAO.find(participant.getRaceId());

            if (race == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find race with id=" + participant.getRaceId());
            }

            List<Participant> participants = participantDAO.findByRaceId(race.getId());

            transaction.commit();

            content.insertRequestAttribute("bet", bet);
            content.insertRequestAttribute("race", race);
            content.insertRequestAttribute("jockeys", participants);
            content.insertRequestAttribute("participant", participant);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard page.
     *
     * @param content
     */
    @Override
    public void presentDashboardPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.dashboard.index"));
        this.setDefaultContentAttributes(content);

        UserDAOImpl userDAO = new UserDAOImpl(true);
        HorseDAOImpl horseDAO = new HorseDAOImpl(true);
        RaceDAOImpl raceDAO = new RaceDAOImpl(true);
        BetDAOImpl betDAO = new BetDAOImpl(true);

        TransactionManager transaction = new TransactionManager(userDAO, horseDAO, raceDAO, betDAO);
        transaction.beginTransaction();

        try {
            final int totalUsers = userDAO.getTotalCount();
            final int totalHorses = horseDAO.getTotalCount();
            final int totalRaces = raceDAO.getTotalCount();
            final int totalBets = betDAO.getTotalCount();

            HashMap<String, Integer> raceStats = raceDAO.calcStatistics();

            transaction.commit();

            content.insertRequestAttribute("totalUsers", totalUsers);
            content.insertRequestAttribute("totalHorses", totalHorses);
            content.insertRequestAttribute("totalRaces", totalRaces);
            content.insertRequestAttribute("totalBets", totalBets);
            content.insertRequestAttribute("raceStats", raceStats);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard users page.
     *
     * @param content
     */
    @Override
    public void presentDashboardUsersPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[page=" + pageNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.users.index"));
        this.setDefaultContentAttributes(content);

        UserDAOImpl userDAO = new UserDAOImpl(true);

        TransactionManager transaction = new TransactionManager(userDAO);
        transaction.beginTransaction();

        try {
            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;

            final int totalUsers = userDAO.getTotalCount();

            List<User> users = userDAO.obtainPart(limit, offset);
            transaction.commit();

            content.insertRequestAttribute("users", users);
            content.insertRequestAttribute("totalUsers", totalUsers);
            content.insertRequestAttribute("limitUsers", limit);

            LOGGER.log(Level.DEBUG, "Users list: " + Arrays.toString(users.toArray()));
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
     * Present dashboard user profile page.
     *
     * @param content
     */
    @Override
    public void presentDashboardUserProfilePage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String idNum = content.findParameter(RequestFieldConfig.Common.REQUEST_ID);
        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validateId(idNum) || !validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[id=" + idNum + " or page=" + pageNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.users.profile"));
        this.setDefaultContentAttributes(content);

        UserDAOImpl userDAO = new UserDAOImpl(true);
        BetDAOImpl betDAO = new BetDAOImpl(true);

        TransactionManager transaction = new TransactionManager(userDAO, betDAO);
        transaction.beginTransaction();

        try {
            final int userId = Integer.parseInt(idNum);
            final int totalBets = betDAO.getTotalForUser(userId);

            final int limit = 10;
            final int page = pageNum != null ? Integer.parseInt(pageNum) : 1;
            final int offset = (page - 1) * limit;

            User user = userDAO.find(userId);

            List<Role> roles = userDAO.findAllRoles();
            List<Bet> bets = betDAO.obtainPart(userId, limit, offset);

            transaction.commit();

            content.insertRequestAttribute("viewedUser", user);
            content.insertRequestAttribute("roles", roles);
            content.insertRequestAttribute("bets", bets);
            content.insertRequestAttribute("totalBets", totalBets);
            content.insertRequestAttribute("limitBets", limit);
        } catch (NumberFormatException e) {
            transaction.rollback();
            throw new ReceiverException("Cannot convert user id. GET[id]=" + e.getMessage(), e);
        } catch (DAOException e) {
            transaction.rollback();
            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }

    /**
     * Present dashboard suits page.
     *
     * @param content
     */
    @Override
    public void presentDashboardSuitsPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.dashboard.suits.index"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard horses page.
     *
     * @param content
     */
    @Override
    public void presentDashboardHorsesPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[page=" + pageNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.horses.index"));
        this.setDefaultContentAttributes(content);

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.dashboard.horses.create"));
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String idNum = content.findParameter(RequestFieldConfig.Common.REQUEST_ID);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validateId(idNum)) {
            throw new ReceiverException("GET[id=" + idNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.horses.edit"));
        this.setDefaultContentAttributes(content);

        HorseDAOImpl horseDAO = new HorseDAOImpl(true);
        SuitDAOImpl suitDAO = new SuitDAOImpl(true);

        TransactionManager transaction = new TransactionManager(horseDAO, suitDAO);
        transaction.beginTransaction();

        try {
            final int id = Integer.parseInt(idNum);
            Horse horse = horseDAO.find(id);

            if (horse == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find horse with id=" + id);
            }

            content.insertRequestAttribute("horse", horse);
            LOGGER.log(Level.DEBUG, "Editing horse: " + horse);

            List<Suit> suits = suitDAO.findAll();
            transaction.commit();

            content.insertRequestAttribute("suits", suits);
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String pageNum = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validatePage(pageNum)) {
            throw new ReceiverException("GET[page=" + pageNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.races.index"));
        this.setDefaultContentAttributes(content);

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        this.setPageSubTitle(messageResource.get("page.title.dashboard.races.create"));
        this.setDefaultContentAttributes(content);
    }

    /**
     * Present dashboard race edit page.
     *
     * @param content
     */
    @Override
    public void presentDashboardRaceEditPage(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        String idNum = content.findParameter(RequestFieldConfig.Common.REQUEST_ID);
        CommonValidator validator = new CommonValidator(locale);

        if (!validator.validateId(idNum)) {
            throw new ReceiverException("GET[id=" + idNum + "] is incorrect.");
        }

        this.setPageSubTitle(messageResource.get("page.title.dashboard.races.edit"));
        this.setDefaultContentAttributes(content);

        RaceDAOImpl raceDAO = new RaceDAOImpl(true);
        ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);

        TransactionManager transaction = new TransactionManager(raceDAO, participantDAO);
        transaction.beginTransaction();

        try {
            final int id = Integer.parseInt(idNum);
            Race race = raceDAO.find(id);

            if (race == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find race with id=" + id);
            }

            content.insertRequestAttribute("race", race);
            LOGGER.log(Level.DEBUG, "Editing race: " + race);

            List<Participant> participants = participantDAO.findByRaceId(race.getId());
            transaction.commit();

            content.insertRequestAttribute("participants", participants);
            LOGGER.log(Level.DEBUG, "Participants list: " + Arrays.toString(participants.toArray()));
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
}
