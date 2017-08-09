package com.epam.horsebetting.type;

public enum FlashMessageType {
    ERROR("alert-danger"),
    MESSAGE("alert-success");

    /**
     * Class name of the message type.
     */
    private String blockClassName;

    /**
     * Constructor.
     *
     * @param className
     */
    FlashMessageType(String className) {
        this.blockClassName = className;
    }

    /**
     * Get block class name.
     *
     * @return blockClassName
     */
    public String getBlockClassName() {
        return blockClassName;
    }
}
