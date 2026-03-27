package com.demo.card.workflow.nodes;

import com.demo.card.service.CardCreditService;
import com.demo.card.dto.AccountHealthReport;
import com.demo.card.model.Transaction;
import com.demo.card.workflow.WorkflowNode;
import com.demo.card.workflow.WorkflowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * AnalystNode gathers account data relevant to the classified intent.
 */
@Component
public class AnalystNode implements WorkflowNode {
    
    @Autowired
    private CardCreditService cardCreditService;

    @Override
    public void execute(WorkflowContext context) throws Exception {
        String cardNo = context.getCardNo();
        String intent = context.getPrimaryIntent();

        // Always get health assessment
        AccountHealthReport health = cardCreditService.assessAccountHealth(cardNo);
        context.put("health", health);

        // Get additional data based on intent
        if (intent.equals("CREDIT_EXPLANATION") || intent.equals("GENERAL")) {
            List<Transaction> highValueTxs = cardCreditService
                .findHighValueTransactions(cardNo, 1000.0, 5);
            context.put("highValueTransactions", highValueTxs);
        }

        // Build summary
        StringBuilder sb = new StringBuilder();
        sb.append("Account Analysis:\n");
        sb.append("- Health Status: ").append(health.getHealthStatus()).append("\n");
        sb.append("- Credit Utilization: ").append(String.format("%.2f", health.getCreditUtilizationPercent())).append("%\n");
        sb.append("- Outstanding Balance: $").append(String.format("%.2f", health.getOutstandingBalance())).append("\n");
        sb.append("- Available Credit: $").append(String.format("%.2f", health.getAvailableCredit())).append("\n");
        sb.append("- Payment Due In: ").append(health.getDaysUntilDue()).append(" days\n");
        
        context.setResult(sb.toString());
    }

    @Override
    public String getName() { 
        return "AnalystNode"; 
    }
}
