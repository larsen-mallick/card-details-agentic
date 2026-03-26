package com.demo.card.model;

import java.time.LocalDateTime;

public class RewardsSummary {
    private long pointsAvailable;
    private long pointsRedeemed;
    private LocalDateTime lastUpdated;

    public RewardsSummary() {}

    public RewardsSummary(long pointsAvailable, long pointsRedeemed, LocalDateTime lastUpdated) {
        this.pointsAvailable = pointsAvailable;
        this.pointsRedeemed = pointsRedeemed;
        this.lastUpdated = lastUpdated;
    }

    public long getPointsAvailable() { return pointsAvailable; }
    public void setPointsAvailable(long pointsAvailable) { this.pointsAvailable = pointsAvailable; }
    public long getPointsRedeemed() { return pointsRedeemed; }
    public void setPointsRedeemed(long pointsRedeemed) { this.pointsRedeemed = pointsRedeemed; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
