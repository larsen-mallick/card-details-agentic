package com.demo.card.workflow.nodes;

import com.demo.card.dto.AccountHealthReport;
import com.demo.card.service.CardCreditService;
import com.demo.card.dto.NextBestAction;
import com.demo.card.workflow.WorkflowNode;
import com.demo.card.workflow.WorkflowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ResolverNode synthesizes the analysis into a customer-facing recommendation.
 */
@Component
public class ResolverNode implements WorkflowNode {
    
    @Autowired
    private CardCreditService cardCreditService;

    @Override
    public void execute(WorkflowContext context) throws Exception {
        String cardNo = context.getCardNo();
        String intent = context.getPrimaryIntent();
        AccountHealthReport health = context.get("health", AccountHealthReport.class);
        NextBestAction action = cardCreditService.recommendNextBestAction(cardNo);
        context.put("nextAction", action);

        StringBuilder response = new StringBuilder();
        response.append("Account Summary & Recommendation:\n\n");

        if ("CREDIT_EXPLANATION".equals(intent) && health != null) {
            double utilization = health.getCreditUtilizationPercent();
            double availableCredit = health.getAvailableCredit();
            if (utilization < 50.0) {
                response.append("Direct Answer: Your available credit does not appear low right now. ")
                        .append("You still have $")
                        .append(String.format("%.2f", availableCredit))
                        .append(" available, and your utilization is ")
                        .append(String.format("%.2f", utilization))
                        .append("%.\n\n");
            } else {
                response.append("Direct Answer: Your available credit is lower because your utilization is ")
                        .append(String.format("%.2f", utilization))
                        .append("%, which reduces remaining credit headroom.\n\n");
            }
        }

        response.append(context.getResult()).append("\n");
        response.append("Recommended Action: ").append(action.getRecommendedAction()).append("\n");
        response.append("Urgency Level: ").append(action.getUrgency()).append("\n");
        response.append("Reason: ").append(action.getReason()).append("\n");
        if (action.getAdditionalInfo() != null && !action.getAdditionalInfo().isEmpty()) {
            response.append("Details: ").append(action.getAdditionalInfo()).append("\n");
        }

        context.setResult(response.toString());
    }

    @Override
    public String getName() { 
        return "ResolverNode"; 
    }
}
