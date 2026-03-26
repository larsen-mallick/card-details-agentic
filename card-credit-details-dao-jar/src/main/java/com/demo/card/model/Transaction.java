package com.demo.card.model;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private LocalDateTime dateTime;
    private String description;
    private double amount;
    private TransactionType type;

    public Transaction() {}

    public Transaction(String id, LocalDateTime dateTime, String description, double amount, TransactionType type) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
