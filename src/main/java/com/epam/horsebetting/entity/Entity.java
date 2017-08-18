package com.epam.horsebetting.entity;

import java.util.HashMap;

public class Entity {

    /**
     * Attributes for entity from JOIN query.
     */
    private HashMap<String, Object> attributes;

    /**
     * Default constructor.
     */
    Entity() {
        this.attributes = new HashMap<>();
    }

    /**
     * Insert new attribute to map.
     *
     * @param key
     * @param value
     */
    public final void insertAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * Find and return attribute from map.
     *
     * @param key
     * @return attribute
     */
    public final Object findAttribute(String key) {
        return this.attributes.get(key);
    }
}
