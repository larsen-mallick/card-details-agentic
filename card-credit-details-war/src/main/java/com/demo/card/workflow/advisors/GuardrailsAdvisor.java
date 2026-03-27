package com.demo.card.workflow.advisors;

import com.demo.card.workflow.WorkflowContext;

/**
 * GuardrailsAdvisor enforces safety and policy checks on workflow steps.
 * Examples: block blocked cards, mask PII, prevent fraud scenarios.
 */
public class GuardrailsAdvisor implements ChatAdvisor {

    @Override
    public void beforeExecute(WorkflowContext context) throws Exception {
        // Pre-execution checks
        if (context.getCardNo() == null || context.getCardNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (context.getUserRequest() == null || context.getUserRequest().trim().isEmpty()) {
            throw new IllegalArgumentException("User request is required");
        }
    }

    @Override
    public void afterExecute(WorkflowContext context) throws Exception {
        // Post-execution checks
        // Example: ensure no sensitive data in public response
        String result = context.getResult();
        if (result != null && result.contains("password")) {
            throw new IllegalStateException("PII (password) detected in response. Blocked.");
        }
    }

    @Override
    public String getName() {
        return "GuardrailsAdvisor";
    }
}
