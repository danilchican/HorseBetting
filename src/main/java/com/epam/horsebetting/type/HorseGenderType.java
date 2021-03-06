package com.epam.horsebetting.type;

public enum HorseGenderType {
    MALE("male"),
    FEMALE("female");

    /**
     * The gender of horse.
     */
    private String gender;

    /**
     * Constructor.
     *
     * @param gender
     */
    HorseGenderType(String gender) {
        this.gender = gender;
    }

    /**
     * Get name of gender.
     *
     * @return gender name
     */
    public String getName() {
        return gender;
    }

    /**
     * Check if the horse gender exist.
     *
     * @param name
     * @return boolean
     */
    public static boolean contains(String name) {
        for (HorseGenderType type : HorseGenderType.values()) {
            if (type.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the name of gender is male.
     *
     * @param name
     * @return boolean
     */
    public static boolean isMale(String name) {
        return MALE.getName().equals(name);
    }
}
