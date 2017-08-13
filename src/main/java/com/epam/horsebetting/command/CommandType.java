package com.epam.horsebetting.command;

import com.epam.horsebetting.command.auth.*;
import com.epam.horsebetting.command.bet.AjaxPlaceBetCommand;
import com.epam.horsebetting.command.horse.*;
import com.epam.horsebetting.command.profile.ProfileBetsPresentCommand;
import com.epam.horsebetting.command.profile.ProfilePaymentPresentCommand;
import com.epam.horsebetting.command.profile.ProfileSettingsPresentCommand;
import com.epam.horsebetting.command.race.*;
import com.epam.horsebetting.command.suit.*;
import com.epam.horsebetting.command.user.*;
import com.epam.horsebetting.command.dashboard.DashboardPresentCommand;
import com.epam.horsebetting.command.locale.ChangeLocaleCommand;
import com.epam.horsebetting.command.profile.ProfilePresentCommand;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.impl.*;
import com.epam.horsebetting.request.RequestContent;

public enum CommandType {
    INDEX_PAGE("index::get", new IndexPageCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
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
    RACES_INDEX("races::get", new RacesPresetCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentRacesPage(content);
        }
    },
    RACES_VIEW("races.view::get", new ViewRacePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentRaceViewPage(content);
        }
    },
    AJAX_BETS_PLACE("ajax.bets.place::post", new AjaxPlaceBetCommand(new BetReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((BetReceiverImpl) getCommand().getReceiver()).createBet(content);
        }
    },
    PROFILE_INDEX("profile::get", new ProfilePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentProfilePage(content);
        }
    },
    PROFILE_SETTINGS("profile.settings::get", new ProfileSettingsPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentProfileSettingsPage(content);
        }
    },
    PROFILE_SETTINGS_UPDATE("profile.settings.update::post", new UpdateProfileUserSettingsCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).updateProfileSettings(content);
        }
    },
    PROFILE_SECURITY_UPDATE("profile.security.update::post", new UpdateUserSecurityCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).updateSecurity(content);
        }
    },
    PROFILE_PAYMENT("profile.payment::get", new ProfilePaymentPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentProfilePaymentPage(content);
        }
    },
    PROFILE_PAYMENT_POST("profile.payment::post", new UpdateProfileBalanceCommand(new UserReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiverImpl) getCommand().getReceiver()).updateProfileBalance(content);
        }
    },
    PROFILE_BETS("profile.bets::get", new ProfileBetsPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentProfileBetsPage(content);
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
    AJAX_DASHBOARD_HORSES_LIST("ajax.dashboard.horses::get", new AjaxDashboardHorsesListCommand(new HorseReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((HorseReceiverImpl) getCommand().getReceiver()).ajaxObtainHorsesList(content);
        }
    },
    DASHBOARD_HORSES_CREATE_PRESENT("dashboard.horses.create::get", new DashboardCreateHorsePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardHorseCreatePage(content);
        }
    },
    DASHBOARD_HORSES_EDIT("dashboard.horses.edit::get", new DashboardEditHorsePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardHorseEditPage(content);
        }
    },
    DASHBOARD_HORSES_UPDATE("dashboard.horses.update::post", new DashboardUpdateHorseCommand(new HorseReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((HorseReceiverImpl) getCommand().getReceiver()).updateHorse(content);
        }
    },
    DASHBOARD_HORSES_CREATE("dashboard.horses.create::post", new DashboardCreateHorseCommand(new HorseReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((HorseReceiverImpl) getCommand().getReceiver()).createHorse(content);
        }
    },
    DASHBOARD_HORSES_REMOVE("dashboard.horses.remove::post", new DashboardRemoveHorseCommand(new HorseReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((HorseReceiverImpl) getCommand().getReceiver()).removeHorse(content);
        }
    },
    DASHBOARD_RACES_INDEX("dashboard.races::get", new DashboardRacesPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardRacesPage(content);
        }
    },
    DASHBOARD_RACES_CREATE_PRESENT("dashboard.races.create::get", new DashboardCreateRacePresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardRaceCreatePage(content);
        }
    },
    DASHBOARD_RACES_CREATE("dashboard.races.create::post", new DashboardCreateRaceCommand(new RaceReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((RaceReceiverImpl) getCommand().getReceiver()).createRace(content);
        }
    },
    DASHBOARD_SUITS_INDEX("dashboard.suits::get", new DashboardSuitsPresentCommand(new PageReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((PageReceiverImpl) getCommand().getReceiver()).presentDashboardSuitsPage(content);
        }
    },
    AJAX_DASHBOARD_SUITS_LIST("ajax.dashboard.suits::get", new AjaxDashboardSuitsListCommand(new SuitReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((SuitReceiverImpl) getCommand().getReceiver()).ajaxObtainSuitsList(content);
        }
    },
    AJAX_DASHBOARD_SUITS_CREATE("ajax.dashboard.suits.create::post", new AjaxDashboardCreateSuitCommand(new SuitReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((SuitReceiverImpl) getCommand().getReceiver()).createSuit(content);
        }
    },
    AJAX_DASHBOARD_SUITS_REMOVE("ajax.dashboard.suits.remove::post", new AjaxDashboardRemoveSuitCommand(new SuitReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((SuitReceiverImpl) getCommand().getReceiver()).removeSuit(content);
        }
    },
    AJAX_DASHBOARD_SUITS_FIND("ajax.dashboard.suits.update::post", new AjaxDashboardUpdateSuitCommand(new SuitReceiverImpl())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((SuitReceiverImpl) getCommand().getReceiver()).updateSuit(content);
        }
    };

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
