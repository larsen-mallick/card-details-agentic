package com.demo.card.dto;

import com.demo.card.model.*;

public class CardSummary {
    private String cardNo;
    private CardStatus status;
    private CreditLimit creditLimit;
    private Balance balance;
    private RewardsSummary rewards;
    private double utilizationPercent;
    private long nextDueInDays;

    public CardSummary() {}

    public CardSummary(String cardNo, CardStatus status, CreditLimit creditLimit, Balance balance,
                       RewardsSummary rewards, double utilizationPercent, long nextDueInDays) {
        this.cardNo = cardNo;
        this.status = status;
        this.creditLimit = creditLimit;
        this.balance = balance;
        this.rewards = rewards;
        this.utilizationPercent = utilizationPercent;
        this.nextDueInDays = nextDueInDays;
    }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    public CardStatus getStatus() { return status; }
    public void setStatus(CardStatus status) { this.status = status; }
    public CreditLimit getCreditLimit() { return creditLimit; }
    public void setCreditLimit(CreditLimit creditLimit) { this.creditLimit = creditLimit; }
    public Balance getBalance() { return balance; }
    public void setBalance(Balance balance) { this.balance = balance; }
    public RewardsSummary getRewards() { return rewards; }
    public void setRewards(RewardsSummary rewards) { this.rewards = rewards; }
    public double getUtilizationPercent() { return utilizationPercent; }
    public void setUtilizationPercent(double utilizationPercent) { this.utilizationPercent = utilizationPercent; }
    public long getNextDueInDays() { return nextDueInDays; }
    public void setNextDueInDays(long nextDueInDays) { this.nextDueInDays = nextDueInDays; }
}
