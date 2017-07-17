package com.epam.danilchican.betting.type;

public enum RoleType {
    ADMINISTRATOR(1), MODERATOR(2), CLIENT(3);

    /**
     * Role id.
     */
    private final int id;

    /**
     * Constructor.
     *
     * @param id
     */
    RoleType(int id) {
        this.id = id;
    }

    /**
     * Get the value of role.
     *
     * @return id
     */
    public int getValue() {
        return id;
    }

}
