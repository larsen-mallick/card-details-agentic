package com.demo.card.dto;

public class NextBestAction {

    public enum Action {
        NO_ACTION,
        PAY_MINIMUM_DUE,
        PAY_FULL_BALANCE,
        REVIEW_TRANSACTIONS,
        RAISE_DISPUTE,
        CONTACT_SUPPORT,
        CARD_BLOCKED_CONTACT_SUPPORT
    }

    public enum Urgency { LOW, MEDIUM, HIGH }

    private String cardNo;
    private Action recommendedAction;
    private Urgency urgency;
    private String reason;
    private String additionalInfo;

    public NextBestAction() {}

    public NextBestAction(String cardNo, Action recommendedAction, Urgency urgency,
                          String reason, String additionalInfo) {
        this.cardNo = cardNo;
        this.recommendedAction = recommendedAction;
        this.urgency = urgency;
        this.reason = reason;
        this.additionalInfo = additionalInfo;
    }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public Action getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(Action recommendedAction) { this.recommendedAction = recommendedAction; }

    public Urgency getUrgency() { return urgency; }
    public void setUrgency(Urgency urgency) { this.urgency = urgency; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
