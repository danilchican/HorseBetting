package com.epam.horsebetting.type;

public enum RaceStatusType {
    COMPLETED("completed"),
    FAILED("failed");

    private String status;

    /**
     * Constructor.
     *
     * @param status
     */
    RaceStatusType(String status) {
        this.status = status;
    }

    /**
     * Get name of the race status.
     *
     * @return status name
     */
    public String getName() {
        return status;
    }

    /**
     * Get role type by id.
     *
     * @param name
     * @return boolean
     */
    public static boolean contains(String name) {
        for (RaceStatusType type : RaceStatusType.values()) {
            if (type.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find race by name.
     *
     * @param name
     * @return race status type or if does not exist
     *         then throw exception.
     */
    public static RaceStatusType findByName(String name) throws EnumConstantNotPresentException {
        for (RaceStatusType type : RaceStatusType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        throw new EnumConstantNotPresentException(RaceStatusType.class, "Cannot find race status[name=" + name + "]");
    }
}
