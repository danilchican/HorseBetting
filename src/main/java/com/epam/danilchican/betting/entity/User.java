package com.epam.danilchican.betting.entity;

public class User extends Entity {

    private int id;
    private int roleId;
    private String name;
    private String email;
    private String password;
    private String createdAt;

    /**
     * Get id of the User.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get Role id of a User.
     *
     * @return role id
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * Get name of the User.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get email of a User.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get User password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get user created time.
     *
     * @return user created time
     */
    public String getCreatedAt() {
        return createdAt;
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
     * Set Role id of a User.
     *
     * @param roleId
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * Set name of the Role.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set user email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set password.
     *
     * @param password
     */
    public void setPassword(String password) {
        // add makeHash(password) util
        this.password = password;
    }
}
