package com.epam.horsebetting.command;

import com.epam.horsebetting.command.auth.*;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.PageReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.receiver.UserReceiver;

public enum CommandType {
    INDEX_PAGE("index::get", new IndexPageCommand(new PageReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiver) getCommand().getReceiver()).presentIndexPage(content);
        }
    },
    AUTH_LOGIN("auth.login::get", new LoginPresentCommand(new PageReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiver) getCommand().getReceiver()).presentLoginPage(content);
        }
    },
    AUTH_LOGIN_FORM("auth.login::post", new LoginCommand(new UserReceiver())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiver) getCommand().getReceiver()).login(content);
        }
    },
    AUTH_REGISTER("auth.register::get", new RegisterPresentCommand(new PageReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiver) getCommand().getReceiver()).presentRegisterPage(content);
        }
    },
    AUTH_REGISTER_FORM("auth.register::post", new RegisterCommand(new UserReceiver())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiver) getCommand().getReceiver()).register(content);
        }
    },
    AUTH_LOGOUT_FORM("auth.logout::post", new LogoutCommand(new UserReceiver())) {
        @Override
        public void doReceiver(RequestContent content) throws ReceiverException {
            ((UserReceiver) getCommand().getReceiver()).logout(content);
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
