package com.epam.horsebetting.command;

import com.epam.horsebetting.command.auth.*;
import com.epam.horsebetting.command.race.DashboardRacesPresentCommand;
import com.epam.horsebetting.command.suit.AjaxDashboardSuitsListCommand;
import com.epam.horsebetting.command.suit.DashboardSuitsPresentCommand;
import com.epam.horsebetting.command.user.AjaxDashboardUsersListCommand;
import com.epam.horsebetting.command.dashboard.DashboardPresentCommand;
import com.epam.horsebetting.command.user.DashboardUsersPresentCommand;
import com.epam.horsebetting.command.horse.DashboardCreateHorsePresentCommand;
import com.epam.horsebetting.command.horse.DashboardHorsesPresentCommand;
import com.epam.horsebetting.command.locale.ChangeLocaleCommand;
import com.epam.horsebetting.command.profile.ProfilePresentCommand;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.impl.PageReceiverImpl;
import com.epam.horsebetting.receiver.impl.SuitReceiverImpl;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.receiver.impl.UserReceiverImpl;

public enum CommandType {
    INDEX_PAGE("index::get", new IndexPageCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiverImpl) getCommand().getReceiver()).presentIndexPage(content);
        }
    },
    AUTH_LOGIN("auth.login::get", new LoginPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiverImpl) getCommand().getReceiver()).presentLoginPage(content);
        }
    },
    AUTH_LOGIN_FORM("auth.login::post", new LoginCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).login(content);
        }
    },
    AUTH_REGISTER("auth.register::get", new RegisterPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiverImpl) getCommand().getReceiver()).presentRegisterPage(content);
        }
    },
    AUTH_REGISTER_FORM("auth.register::post", new RegisterCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).register(content);
        }
    },
    AUTH_LOGOUT_FORM("auth.logout::post", new LogoutCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).logout(content);
        }
    },
    CHANGE_LOCALE("locale.change::get", new ChangeLocaleCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).changeLocale(content);
        }
    },
    PROFILE_INDEX("profile::get", new ProfilePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentProfilePage(content);
        }
    },
    DASHBOARD_INDEX("dashboard::get", new DashboardPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardPage(content);
        }
    },
    DASHBOARD_USERS_INDEX("dashboard.users::get", new DashboardUsersPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardUsersPage(content);
        }
    },
    DASHBOARD_HORSES_INDEX("dashboard.horses::get", new DashboardHorsesPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardHorsesPage(content);
        }
    },
    DASHBOARD_HORSES_CREATE("dashboard.horses.create::get", new DashboardCreateHorsePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardHorseCreatePage(content);
        }
    },
    DASHBOARD_RACES_INDEX("dashboard.races::get", new DashboardRacesPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardRacesPage(content);
        }
    },
    DASHBOARD_SUITS_INDEX("dashboard.suits::get", new DashboardSuitsPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardSuitsPage(content);
        }
    },
    AJAX_DASHBOARD_USERS_LIST("ajax.dashboard.users::get", new AjaxDashboardUsersListCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).ajaxObtainUsersList(content);
        }
    },
    AJAX_DASHBOARD_SUITS_LIST("ajax.dashboard.suits::get", new AjaxDashboardSuitsListCommand(new SuitReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((SuitReceiverImpl) getCommand().getReceiver()).ajaxObtainSuitsList(content);
        }
    };;

    /**
     * Command name.
     */
    private String commandName;

    /**
     * Command type instance.
     */
    private AbstractCommand command;

    /**
     * Constructor with value & command.
     *
     * @param value
     * @param command
     */
    CommandType(String value, AbstractCommand command) {
        this.commandName = value;
        this.command = command;
    }

    /**
     * Get command value.
     *
     * @return
     */
    public String getValue() {
        return commandName;
    }

    /**
     * Get command object.
     *
     * @return
     */
    public AbstractCommand getCommand() {
        return command;
    }

    /**
     * Execute receiver.
     *
     * @param content
     */
    public abstract void doReceiver(RequestContent content) throws ReceiverException;

    /**
     * Get command type by tag name.
     *
     * @param tag
     * @return
     */
    public static CommandType findByTag(String tag) throws IllegalCommandTypeException {
        for (CommandType type : CommandType.values()) {
            if (tag.equals(type.getValue())) {
                return type;
            }
        }

        throw new IllegalCommandTypeException("Command[" + tag + "] not found.");
    }
}
