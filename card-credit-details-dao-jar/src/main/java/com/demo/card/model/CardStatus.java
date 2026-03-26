package com.demo.card.model;

import java.time.LocalDateTime;

public class CardStatus {
    private CardState state;
    private LocalDateTime updatedAt;

    public CardStatus() {}

    public CardStatus(CardState state, LocalDateTime updatedAt) {
        this.state = state;
        this.updatedAt = updatedAt;
    }

    public CardState getState() { return state; }
    public void setState(CardState state) { this.state = state; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
