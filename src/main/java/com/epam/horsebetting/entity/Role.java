package com.epam.horsebetting.entity;

public class Role extends Entity {

    private int id;
    private String name;

    /**
     * Get id of the Role.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get name of the Role.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set id of the Role.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set name of the Role.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + "'}";
    }
}
