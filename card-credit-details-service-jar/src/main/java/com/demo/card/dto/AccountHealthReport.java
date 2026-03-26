package com.demo.card.dto;

import java.util.List;

public class AccountHealthReport {

    public enum HealthStatus { GOOD, WATCH, RISK }

    private String cardNo;
    private HealthStatus healthStatus;
    private List<String> reasons;
    private double creditUtilizationPercent;
    private long daysUntilDue;
    private double outstandingBalance;
    private double availableCredit;
    private int recentTransactionCount;

    public AccountHealthReport() {}

    public AccountHealthReport(String cardNo, HealthStatus healthStatus, List<String> reasons,
                                double creditUtilizationPercent, long daysUntilDue,
                                double outstandingBalance, double availableCredit,
                                int recentTransactionCount) {
        this.cardNo = cardNo;
        this.healthStatus = healthStatus;
        this.reasons = reasons;
        this.creditUtilizationPercent = creditUtilizationPercent;
        this.daysUntilDue = daysUntilDue;
        this.outstandingBalance = outstandingBalance;
        this.availableCredit = availableCredit;
        this.recentTransactionCount = recentTransactionCount;
    }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public double getCreditUtilizationPercent() { return creditUtilizationPercent; }
    public void setCreditUtilizationPercent(double creditUtilizationPercent) { this.creditUtilizationPercent = creditUtilizationPercent; }

    public long getDaysUntilDue() { return daysUntilDue; }
    public void setDaysUntilDue(long daysUntilDue) { this.daysUntilDue = daysUntilDue; }

    public double getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(double outstandingBalance) { this.outstandingBalance = outstandingBalance; }

    public double getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(double availableCredit) { this.availableCredit = availableCredit; }

    public int getRecentTransactionCount() { return recentTransactionCount; }
    public void setRecentTransactionCount(int recentTransactionCount) { this.recentTransactionCount = recentTransactionCount; }
}
