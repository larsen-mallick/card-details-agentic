package com.demo.card.workflow.advisors;

import com.demo.card.workflow.WorkflowContext;

/**
 * ValidatingAdvisor ensures workflow steps produce valid, well-formed output.
 */
public class ValidatingAdvisor implements ChatAdvisor {

    @Override
    public void beforeExecute(WorkflowContext context) throws Exception {
        // Validate context before each node runs
    }

    @Override
    public void afterExecute(WorkflowContext context) throws Exception {
        // Verify result is not null/empty after each node
        String result = context.getResult();
        if (result == null || result.trim().isEmpty()) {
            throw new IllegalStateException("Node produced empty result");
        }
    }

    @Override
    public String getName() {
        return "ValidatingAdvisor";
    }
}
