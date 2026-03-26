package com.demo.card.web;

import com.demo.card.dto.AccountHealthReport;
import com.demo.card.dto.CardSummary;
import com.demo.card.dto.NextBestAction;
import com.demo.card.model.*;
import com.demo.card.service.CardCreditService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/card-details")
public class CardController {

    @Autowired
    private final CardCreditService service;

    public CardController(CardCreditService service) {
        this.service = service;
    }

    @GetMapping("/appStatus")
    @McpTool(description = "Get the status of the service")
    @ResponseStatus(HttpStatus.OK)
    public String appStatus() {
        return "OK";
    }

    @GetMapping("/{cardNo}/summary")
    @McpTool(description = "Fetch summary details by card number")
    public CardSummary summary(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for") String cardNo) {
        return service.getCardSummary(cardNo);
    }

    @GetMapping("/{cardNo}/status")
    @McpTool(description = "Get status of a specific card based on card number")
    public CardStatus status(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo) {
        return service.getCardStatus(cardNo);
    }

    @GetMapping("/{cardNo}/limits")
    @McpTool(description = "Get limits of a specific card based on card number")
    public CreditLimit limits(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo) {
        return service.getCreditLimit(cardNo);
    }

    @GetMapping("/{cardNo}/balance")
    @McpTool(description = "Get balance of a specific card based on card number")
    public Balance balance(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo) {
        return service.getBalance(cardNo);
    }

    @GetMapping("/{cardNo}/rewards")
    @McpTool(description = "Get rewards of a specific card based on card number")
    public RewardsSummary rewards(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo) {
        return service.getRewards(cardNo);
    }

    @GetMapping("/{cardNo}/transactions")
    @McpTool(description = "Get transactions of a specific card based on card number")
    public List<Transaction> transactions(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo, @RequestParam(defaultValue = "10") int lastN) {
        return service.listTransactions(cardNo, lastN);
    }

    @GetMapping("/{cardNo}/statements")
    @McpTool(description = "Get statements of a specific card based on card number")
    public List<Statement> statements(@PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch details for")  String cardNo, @RequestParam(defaultValue = "6") int lastN) {
        return service.listStatements(cardNo, lastN);
    }

    @GetMapping("/{cardNo}/health")
    @McpTool(description = "Assess the overall health of a card account: returns health status (GOOD/WATCH/RISK), reasons, utilization, and days until payment due")
    public AccountHealthReport health(@PathVariable("cardNo") @McpToolParam(description = "cardNo to assess") String cardNo) {
        return service.assessAccountHealth(cardNo);
    }

    @GetMapping("/{cardNo}/high-value-transactions")
    @McpTool(description = "Find recent high-value debit transactions above a given amount threshold for a card")
    public List<Transaction> highValueTransactions(
            @PathVariable("cardNo") @McpToolParam(description = "cardNo to fetch transactions for") String cardNo,
            @RequestParam(defaultValue = "1000") @McpToolParam(description = "Minimum amount threshold in currency units") double threshold,
            @RequestParam(defaultValue = "5") @McpToolParam(description = "Maximum number of results to return") int lastN) {
        return service.findHighValueTransactions(cardNo, threshold, lastN);
    }

    @GetMapping("/{cardNo}/next-best-action")
    @McpTool(description = "Get the recommended next best action for a card account: returns action type, urgency, reason, and additional guidance")
    public NextBestAction nextBestAction(@PathVariable("cardNo") @McpToolParam(description = "cardNo to evaluate") String cardNo) {
        return service.recommendNextBestAction(cardNo);
    }
}
