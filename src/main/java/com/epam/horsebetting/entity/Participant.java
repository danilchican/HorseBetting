package com.epam.horsebetting.entity;

import java.math.BigDecimal;

import static com.epam.horsebetting.config.SQLFieldConfig.Participant.JOCKEY;

public class Participant extends Entity {
    private int id;
    private int horseId;
    private int raceId;

    private BigDecimal coefficient;

    private boolean isWinner;

    /**
     * Default constructor.
     */
    public Participant() {
        this.setWinner(false);
    }

    /**
     * Constructor with id.
     *
     * @param id
     */
    public Participant(int id) {
        this();
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    /**
     * Get referenced horse id.
     *
     * @return horse id
     */
    public int getHorseId() {
        return horseId;
    }

    /**
     * Ger referenced race id.
     *
     * @return race id
     */
    public int getRaceId() {
        return raceId;
    }

    /**
     * Get coefficient.
     *
     * @return coefficient
     */
    public BigDecimal getCoefficient() {
        return coefficient;
    }

    /**
     * Check if the participant is winner.
     *
     * @return boolean
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * Get jockey name.
     *
     * @return jockey name
     */
    public String getJockeyName() {
        return String.valueOf(findAttribute(JOCKEY));
    }

    /**
     * Set id of participant.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set reference to horse id.
     *
     * @param horseId
     */
    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    /**
     * Set reference to race id.
     *
     * @param raceId
     */
    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    /**
     * Set coefficient.
     *
     * @param coefficient
     */
    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Set winner.
     *
     * @param winner
     */
    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", horseId=" + horseId +
                ", raceId=" + raceId +
                ", coefficient=" + coefficient +
                ", isWinner=" + isWinner + "}";
    }
}
