package com.epam.horsebetting.entity;

public class Suit extends Entity {
    private int id;
    private String name;

    public Suit() {
    }

    public Suit(String name) {
        this.name = name;
    }

    /**
     * Get id of suit.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of suit.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set id of suit.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the name of suit.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Suit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}";
    }
}
