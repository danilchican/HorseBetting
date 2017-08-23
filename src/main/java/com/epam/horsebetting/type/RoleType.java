package com.epam.horsebetting.type;

public enum RoleType {
    GUEST(0, "Guest"),
    ADMINISTRATOR(1, "Administrator"),
    CLIENT(2, "Client");

    /**
     * Role id.
     */
    private final int id;

    /**
     * Name of the role type.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param id
     */
    RoleType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the name of role.
     *
     * @return name
     */
    public String getRoleName() {
        return name;
    }

    /**
     * Get the value of role.
     *
     * @return id
     */
    public int getRoleId() {
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
            if (type.getRoleId() == id) {
                return type;
            }
        }

        return GUEST;
    }
}
