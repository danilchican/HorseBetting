package com.epam.horsebetting.entity;

import java.sql.Timestamp;

public class PasswordRecover extends Entity {

    private int id;

    private String email;
    private String token;

    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public PasswordRecover() {
    }

    /**
     * Constructor.
     *
     * @param email
     * @param token
     */
    public PasswordRecover(String email, String token) {
        setEmail(email);
        setToken(token);
    }

    /**
     * Get id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get token.
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Get created at date.
     *
     * @return
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Set id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set token.
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Set created at date.
     *
     * @param createdAt
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
