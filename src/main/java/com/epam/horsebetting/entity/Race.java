package com.epam.horsebetting.entity;

import com.epam.horsebetting.type.RaceStatusType;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Race extends Entity {
    private int id;
    private int trackLength;

    private String status;

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
     * Get status of a race.
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get status title of race.
     *
     * @return status title
     */
    public String getStatusTitle() {
        return RaceStatusType.findTitleByAttribute(status);
    }

    /**
     * Is available to take a bet
     * or to edit race.
     *
     * @return boolean
     */
    public boolean isAvailable() {
        return status == null;
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
     * Set status of race.
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", trackLength=" + trackLength +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", minRate=" + minRate +
                ", betEndDate=" + betEndDate +
                ", startedAt=" + startedAt +
                ", createdAt=" + createdAt +
                "}";
    }
}
