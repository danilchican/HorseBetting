package com.epam.horsebetting.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MessageWrapper {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Messages storage.
     */
    private List<String> messages;

    /**
     * Default constructor.
     */
    public MessageWrapper() {
        this.messages = new ArrayList<>();
    }

    /**
     * Add message to list.
     *
     * @param message
     */
    public void add(String message) {
        if(message != null) {
            this.messages.add(message);
        } else {
            LOGGER.log(Level.ERROR, "MessageWrapper received null message.");
        }
    }

    /**
     * Size of message list.
     *
     * @return size
     */
    public int size() {
        return messages.size();
    }

    /**
     * Find all messages in list.
     *
     * @return messages
     */
    public List<String> findAll() {
        return this.messages;
    }
}
