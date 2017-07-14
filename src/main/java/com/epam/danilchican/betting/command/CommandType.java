package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.receiver.PageReceiver;
import com.epam.danilchican.betting.receiver.UserReceiver;
import com.epam.danilchican.betting.request.RequestContent;

public enum CommandType {
    INDEX_PAGE("index::get", new IndexPageCommand(new PageReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            ((PageReceiver) getCommand().getReceiver()).presentIndexPage(content);
        }
    },
    AUTH_LOGIN("auth.login::get", new AuthCommand(new UserReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            //..
        }
    },
    AUTH_REGISTER("auth.register::get", new RegisterCommand(new UserReceiver())) {
        @Override
        public void doReceiver(RequestContent content) {
            //..
        }
    };

    /**
     * Command name.
     */
    String commandName;

    /**
     * Command type instance.
     */
    AbstractCommand command;

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

        throw new IllegalCommandTypeException("Command[" + tag + "] not found.");
    }
}
