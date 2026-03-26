package com.demo.card.dao;

import com.demo.card.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CardCreditDao {

    private final Map<String, CardStatus> statusStore = new HashMap<>();
    private final Map<String, CreditLimit> limitStore = new HashMap<>();
    private final Map<String, Balance> balanceStore = new HashMap<>();
    private final Map<String, RewardsSummary> rewardsStore = new HashMap<>();
    private final Map<String, List<Transaction>> txStore = new HashMap<>();
    private final Map<String, List<Statement>> stmtStore = new HashMap<>();

    public CardCreditDao() {
        System.out.println("Loading Card Data");
        seed();
    }

    private void seed() {
        System.out.println("CardCreditDao :: seed() :: Card Data");
        String card = "123";
        statusStore.put(card, new CardStatus(CardState.ACTIVE, LocalDateTime.now().minusDays(1)));
        limitStore.put(card, new CreditLimit(50000.0, 32000.0));
        balanceStore.put(card, new Balance(18000.0, 1800.0, LocalDate.now().plusDays(10)));
        rewardsStore.put(card, new RewardsSummary(12500, 3500, LocalDateTime.now().minusHours(3)));

        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction("TXN-1001", LocalDateTime.now().minusDays(1), "Grocery Store", 2450.75, TransactionType.DEBIT));
        txs.add(new Transaction("TXN-1002", LocalDateTime.now().minusDays(2), "Online Refund", 500.00, TransactionType.CREDIT));
        txs.add(new Transaction("TXN-1003", LocalDateTime.now().minusDays(3), "Fuel", 1800.00, TransactionType.DEBIT));
        txs.add(new Transaction("TXN-1004", LocalDateTime.now().minusDays(5), "Restaurant", 1200.50, TransactionType.DEBIT));
        txStore.put(card, txs);

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new Statement("STMT-2025-12", LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 31), LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10), 14500.00, 1450.00));
        stmts.add(new Statement("STMT-2026-01", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 10), 17200.00, 1720.00));
        stmtStore.put(card, stmts);
    }

    public CardStatus getCardStatus(String cardNo) {
        return statusStore.getOrDefault(cardNo, new CardStatus(CardState.BLOCKED, LocalDateTime.now()));
    }

    public CreditLimit getCreditLimit(String cardNo) {
        return limitStore.getOrDefault(cardNo, new CreditLimit(0, 0));
    }

    public Balance getBalance(String cardNo) {
        System.out.println("CardCreditDao :: getBalance() :: Getting Balance");
        return balanceStore.getOrDefault(cardNo, new Balance(0, 0, LocalDate.now()));
    }

    public RewardsSummary getRewards(String cardNo) {
        return rewardsStore.getOrDefault(cardNo, new RewardsSummary(0, 0, LocalDateTime.now()));
    }

    public List<Transaction> listTransactions(String cardNo, int lastN) {
        List<Transaction> list = txStore.getOrDefault(cardNo, Collections.emptyList());
        return list.stream()
                .sorted(Comparator.comparing(Transaction::getDateTime).reversed())
                .limit(lastN)
                .collect(Collectors.toList());
    }

    public List<Statement> listStatements(String cardNo, int lastN) {
        List<Statement> list = stmtStore.getOrDefault(cardNo, Collections.emptyList());
        return list.stream()
                .sorted(Comparator.comparing(Statement::getGeneratedOn).reversed())
                .limit(lastN)
                .collect(Collectors.toList());
    }
}
