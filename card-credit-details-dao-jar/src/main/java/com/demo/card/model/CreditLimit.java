package com.demo.card.model;

public class CreditLimit {
    private double totalLimit;
    private double availableCredit;

    public CreditLimit() {}

    public CreditLimit(double totalLimit, double availableCredit) {
        this.totalLimit = totalLimit;
        this.availableCredit = availableCredit;
    }

    public double getTotalLimit() { return totalLimit; }
    public void setTotalLimit(double totalLimit) { this.totalLimit = totalLimit; }
    public double getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(double availableCredit) { this.availableCredit = availableCredit; }
}
