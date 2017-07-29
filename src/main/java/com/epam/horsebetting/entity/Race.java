package com.epam.horsebetting.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Race {
    private int id;
    private int trackLength;

    private boolean isFinished;

    private String title;
    private String place;

    private BigDecimal minRate;

    private Timestamp betEndDate;
    private Timestamp startedAt;
    private Timestamp createdAt;

    /**
     * Get id of a race.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get track length.
     *
     * @return track length
     */
    public int getTrackLength() {
        return trackLength;
    }

    /**
     * Check if race is finished.
     *
     * @return finished flag
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Get title of race.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get place of race.
     *
     * @return place
     */
    public String getPlace() {
        return place;
    }

    /**
     * Get min rate.
     *
     * @return min rate
     */
    public BigDecimal getMinRate() {
        return minRate;
    }

    /**
     * Get bet end date.
     *
     * @return end date
     */
    public Timestamp getBetEndDate() {
        return betEndDate;
    }

    /**
     * Get started date.
     *
     * @return started date
     */
    public Timestamp getStartedAt() {
        return startedAt;
    }

    /**
     * Get created date.
     *
     * @return created date
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Set id of race.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set track length.
     *
     * @param trackLength
     */
    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    /**
     * Set finished flag.
     *
     * @param finished
     */
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    /**
     * Set title of race.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set place of race.
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Set min rate of race.
     *
     * @param minRate
     */
    public void setMinRate(BigDecimal minRate) {
        this.minRate = minRate;
    }

    /**
     * Set bet end date of race.
     *
     * @param betEndDate
     */
    public void setBetEndDate(Timestamp betEndDate) {
        this.betEndDate = betEndDate;
    }

    /**
     * Set started date of race.
     *
     * @param startedAt
     */
    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * Set created date of race.
     *
     * @param createdAt
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
