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
}
