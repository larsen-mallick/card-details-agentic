package com.demo.card.workflow.advisors;

import com.demo.card.workflow.WorkflowContext;

/**
 * ChatAdvisor is middleware that can intercept and modify workflow behavior.
 * Similar to servlet filters or AOP advice.
 */
public interface ChatAdvisor {
    /**
     * Called before a node executes.
     * Can validate input, inject data, enforce policy, etc.
     */
    void beforeExecute(WorkflowContext context) throws Exception;

    /**
     * Called after a node executes.
     * Can validate output, apply guardrails, transform result, etc.
     */
    void afterExecute(WorkflowContext context) throws Exception;

    String getName();
}
