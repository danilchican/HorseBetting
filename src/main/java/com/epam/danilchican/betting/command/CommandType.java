package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.receiver.UserReceiver;
import com.epam.danilchican.betting.request.RequestContent;

public enum CommandType {
    INDEX_PAGE("index", new IndexPageCommand(new UserReceiver())) {

        @Override
        public void doReceiver(RequestContent content) {
            //..
        }
    },
    AUTH_LOGIN("auth.login", new AuthCommand(new UserReceiver())) {

        @Override
        public void doReceiver(RequestContent content) {
            //..
        }
    },
    AUTH_REGISTER("auth.register", new RegisterCommand(new UserReceiver())) {

        @Override
        public void doReceiver(RequestContent content) {
            //..
        }
    };

    String commandValue;
    AbstractCommand command;

    /**
     * Constructor with value & command.
     *
     * @param value
     * @param command
     */
    CommandType(String value, AbstractCommand command) {
        this.commandValue = value;
        this.command = command;
    }

    /**
     * Get command value.
     *
     * @return
     */
    public String getValue() {
        return commandValue;
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
    public abstract void doReceiver(RequestContent content);

    /**
     * Get device connection type by tag name.
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

        throw new IllegalCommandTypeException("Illegal command[" + tag + "].");
    }
}
