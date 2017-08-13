package com.epam.horsebetting.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.epam.horsebetting.config.SQLFieldConfig.Bet.PARTICIPANT_NAME;

public class Bet extends Entity {
    private int id;
    private int userId;
    private int participantId;

    private BigDecimal amount;

    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public Bet() {
    }

    /**
     * Constructor with id.
     *
     * @param id
     */
    public Bet(int id) {
        setId(id);
    }

    /**
     * Get bet id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get referenced user id.
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get referenced participant id.
     *
     * @return participant id
     */
    public int getParticipantId() {
        return participantId;
    }

    /**
     * Get amount.
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get created at timestamp.
     *
     * @return created at timestamp
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Get participant name.
     *
     * @return participant name
     */
    public String getParticipantName() {
        return String.valueOf(findAttribute(PARTICIPANT_NAME));
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
     * Set user id.
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Set participant id.
     *
     * @param participantId
     */
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    /**
     * Set amount.
     *
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Set created at.
     *
     * @param createdAt
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
