package com.epam.horsebetting.type;

public enum RaceStatusType {
    COMPLETED("completed", "Completed"),
    FAILED("failed", "Failed");

    /**
     * Status attribute.
     */
    private final String attribute;

    /**
     * Name of the role type.
     */
    private final String title;

    /**
     * Constructor.
     *
     * @param attribute
     * @param title
     */
    RaceStatusType(String attribute, String title) {
        this.attribute = attribute;
        this.title = title;
    }

    /**
     * Get the title of status.
     *
     * @return title
     */
    public String getStatusTitle() {
        return title;
    }

    /**
     * Get the attribute of status.
     *
     * @return attribute
     */
    public String getAttributeName() {
        return attribute;
    }

    /**
     * Get status title by attribute.
     *
     * @param attribute
     * @return string
     */
    public static String findTitleByAttribute(String attribute) {
        for (RaceStatusType type : RaceStatusType.values()) {
            if (type.getAttributeName().equals(attribute)) {
                return type.getStatusTitle();
            }
        }

        return null;
    }
}
