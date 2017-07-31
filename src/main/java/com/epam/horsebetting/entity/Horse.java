package com.epam.horsebetting.entity;

public class Horse extends Entity {
    private int id;
    private int suitId;

    private String name;
    private byte age;
    private boolean sex;

    /**
     * Get id of horse.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get suit id of horse.
     *
     * @return suit id
     */
    public int getSuitId() {
        return suitId;
    }

    /**
     * Get the name of horse.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get age of horse.
     *
     * @return age
     */
    public byte getAge() {
        return age;
    }

    /**
     * Get sex of horse.
     *
     * @return sex
     */
    public boolean getSex() {
        return sex;
    }

    /**
     * Set id of horse.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set suit id of horse.
     *
     * @param suitId
     */
    public void setSuitId(int suitId) {
        this.suitId = suitId;
    }

    /**
     * Set the name of horse.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set age of horse.
     *
     * @param age
     */
    public void setAge(byte age) {
        this.age = age;
    }

    /**
     * Set sex of horse.
     *
     * @param sex
     */
    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", suitId=" + suitId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                "}";
    }
}
