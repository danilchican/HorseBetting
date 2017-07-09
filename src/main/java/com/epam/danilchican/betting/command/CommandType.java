package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.CommandTypeNotFoundException;

public enum CommandType {
    INDEX_PAGE("index", new IndexPageCommand()),
    AUTH_LOGIN("auth.login", new AuthCommand()),
    AUTH_REGISTER("auth.register", new RegisterCommand());

    String commandValue;
    ICommand command;

    /**
     * Constructor with value & command.
     *
     * @param value
     * @param command
     */
    CommandType(String value, ICommand command) {
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
    public ICommand getCurrentCommand() {
        return command;
    }

    /**
     * Get device connection type by tag name.
     *
     * @param tag
     * @return
     */
    public static CommandType findByTag(String tag) throws CommandTypeNotFoundException {
        for (CommandType type : CommandType.values()) {
            if (tag.equals(type.getValue())) {
                return type;
            }
        }

        throw new CommandTypeNotFoundException("Illegal command[" + tag + "].");
    }
}
