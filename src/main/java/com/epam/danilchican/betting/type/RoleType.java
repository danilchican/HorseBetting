package com.epam.danilchican.betting.type;

public enum RoleType {
    GUEST(0), ADMINISTRATOR(1), MODERATOR(2), CLIENT(3);

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

    /**
     * Get role type by id.
     *
     * @param id
     * @return
     */
    public static RoleType findById(int id) {
        for (RoleType type : RoleType.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }

        return GUEST;
    }
}
