package com.demo.card.service;

import com.demo.card.dao.CardCreditDao;
import com.demo.card.dto.AccountHealthReport;
import com.demo.card.dto.CardSummary;
import com.demo.card.dto.NextBestAction;
import com.demo.card.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardCreditService {

    @Autowired
    private final CardCreditDao dao;

    public CardCreditService(CardCreditDao dao) {
        this.dao = dao;
    }

    public CardSummary getCardSummary(String cardNo) {
        CardStatus status = dao.getCardStatus(cardNo);
        CreditLimit limit = dao.getCreditLimit(cardNo);
        Balance balance = dao.getBalance(cardNo);
        RewardsSummary rewards = dao.getRewards(cardNo);
        double utilization = 0.0;
        if (limit.getTotalLimit() > 0) {
            utilization = (balance.getOutstanding() / limit.getTotalLimit()) * 100.0;
        }
        long days = 0;
        LocalDate due = balance.getDueDate();
        if (due != null) {
            days = ChronoUnit.DAYS.between(LocalDate.now(), due);
        }
        return new CardSummary(cardNo, status, limit, balance, rewards,
                Math.round(utilization * 100.0) / 100.0, days);
    }

    public CardStatus getCardStatus(String cardNo) {
        return dao.getCardStatus(cardNo);
    }

    public CreditLimit getCreditLimit(String cardNo) {
        return dao.getCreditLimit(cardNo);
    }

    public Balance getBalance(String cardNo) {
        return dao.getBalance(cardNo);
    }

    public RewardsSummary getRewards(String cardNo) {
        return dao.getRewards(cardNo);
    }

    public List<Transaction> listTransactions(String cardNo, int lastN) {
        return dao.listTransactions(cardNo, lastN);
    }

    public List<Statement> listStatements(String cardNo, int lastN) {
        return dao.listStatements(cardNo, lastN);
    }

    public AccountHealthReport assessAccountHealth(String cardNo) {
        CardStatus status = dao.getCardStatus(cardNo);
        CreditLimit limit = dao.getCreditLimit(cardNo);
        Balance balance = dao.getBalance(cardNo);
        List<Transaction> recentTxs = dao.listTransactions(cardNo, 10);

        List<String> reasons = new ArrayList<>();
        double utilization = 0.0;
        if (limit.getTotalLimit() > 0) {
            utilization = (balance.getOutstanding() / limit.getTotalLimit()) * 100.0;
            utilization = Math.round(utilization * 100.0) / 100.0;
        }
        long daysUntilDue = 0;
        LocalDate due = balance.getDueDate();
        if (due != null) {
            daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), due);
        }

        AccountHealthReport.HealthStatus healthStatus = AccountHealthReport.HealthStatus.GOOD;

        if (status.getState() == CardState.BLOCKED || status.getState() == CardState.CLOSED) {
            healthStatus = AccountHealthReport.HealthStatus.RISK;
            reasons.add("Card is " + status.getState().name().toLowerCase());
        }
        if (utilization >= 80.0) {
            healthStatus = AccountHealthReport.HealthStatus.RISK;
            reasons.add("Credit utilization is critically high at " + utilization + "%");
        } else if (utilization >= 50.0) {
            if (healthStatus == AccountHealthReport.HealthStatus.GOOD) {
                healthStatus = AccountHealthReport.HealthStatus.WATCH;
            }
            reasons.add("Credit utilization is elevated at " + utilization + "%");
        }
        if (daysUntilDue >= 0 && daysUntilDue <= 3) {
            if (healthStatus == AccountHealthReport.HealthStatus.GOOD) {
                healthStatus = AccountHealthReport.HealthStatus.WATCH;
            }
            reasons.add("Payment due in " + daysUntilDue + " day(s)");
        }
        if (reasons.isEmpty()) {
            reasons.add("Account is in good standing");
        }

        return new AccountHealthReport(cardNo, healthStatus, reasons, utilization,
                daysUntilDue, balance.getOutstanding(), limit.getAvailableCredit(),
                recentTxs.size());
    }

    public List<Transaction> findHighValueTransactions(String cardNo, double threshold, int lastN) {
        return dao.listTransactions(cardNo, 50).stream()
                .filter(tx -> tx.getType() == TransactionType.DEBIT && tx.getAmount() >= threshold)
                .sorted((a, b) -> Double.compare(b.getAmount(), a.getAmount()))
                .limit(lastN)
                .collect(Collectors.toList());
    }

    public NextBestAction recommendNextBestAction(String cardNo) {
        CardStatus status = dao.getCardStatus(cardNo);
        CreditLimit limit = dao.getCreditLimit(cardNo);
        Balance balance = dao.getBalance(cardNo);

        if (status.getState() == CardState.BLOCKED || status.getState() == CardState.CLOSED) {
            return new NextBestAction(cardNo, NextBestAction.Action.CARD_BLOCKED_CONTACT_SUPPORT,
                    NextBestAction.Urgency.HIGH,
                    "Card is " + status.getState().name().toLowerCase(),
                    "Please contact customer support immediately to resolve the card status.");
        }

        long daysUntilDue = 0;
        LocalDate due = balance.getDueDate();
        if (due != null) {
            daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), due);
        }

        double utilization = 0.0;
        if (limit.getTotalLimit() > 0) {
            utilization = (balance.getOutstanding() / limit.getTotalLimit()) * 100.0;
        }

        if (daysUntilDue <= 3 && balance.getOutstanding() > 0) {
            return new NextBestAction(cardNo, NextBestAction.Action.PAY_MINIMUM_DUE,
                    NextBestAction.Urgency.HIGH,
                    "Payment due in " + daysUntilDue + " day(s)",
                    "Minimum due: " + balance.getMinimumDue() + ". Pay in full to avoid interest: " + balance.getOutstanding());
        }

        if (utilization >= 80.0) {
            return new NextBestAction(cardNo, NextBestAction.Action.PAY_FULL_BALANCE,
                    NextBestAction.Urgency.HIGH,
                    "Credit utilization is critically high at " + Math.round(utilization) + "%",
                    "Available credit is low. Paying down the balance will restore your credit headroom.");
        }

        if (utilization >= 50.0) {
            return new NextBestAction(cardNo, NextBestAction.Action.PAY_MINIMUM_DUE,
                    NextBestAction.Urgency.MEDIUM,
                    "Credit utilization is elevated at " + Math.round(utilization) + "%",
                    "Consider paying more than the minimum to reduce utilization.");
        }

        return new NextBestAction(cardNo, NextBestAction.Action.NO_ACTION,
                NextBestAction.Urgency.LOW,
                "Account is in good standing",
                "No immediate action required. Review your statements regularly.");
    }
}
