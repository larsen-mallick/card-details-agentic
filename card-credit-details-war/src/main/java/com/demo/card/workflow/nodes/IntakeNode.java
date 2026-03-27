package com.demo.card.workflow.nodes;

import com.demo.card.workflow.WorkflowNode;
import com.demo.card.workflow.WorkflowContext;
import org.springframework.stereotype.Component;

/**
 * IntakeNode classifies the user's intent from their natural language request.
 */
@Component
public class IntakeNode implements WorkflowNode {
    
    @Override
    public void execute(WorkflowContext context) throws Exception {
        String request = context.getUserRequest();
        
        String intent = "GENERAL";
        if (request.toLowerCase().contains("credit")) {
            intent = "CREDIT_EXPLANATION";
        } else if (request.toLowerCase().contains("transaction")) {
            intent = "TRANSACTION_REVIEW";
        } else if (request.toLowerCase().contains("balance")) {
            intent = "BALANCE_QUERY";
        } else if (request.toLowerCase().contains("reward")) {
            intent = "REWARDS_QUERY";
        } else if (request.toLowerCase().contains("payment") || request.toLowerCase().contains("due")) {
            intent = "PAYMENT_INFO";
        }
        
        context.setPrimaryIntent(intent);
        context.setResult("Request classified as: " + intent);
    }

    @Override
    public String getName() { 
        return "IntakeNode"; 
    }
}
