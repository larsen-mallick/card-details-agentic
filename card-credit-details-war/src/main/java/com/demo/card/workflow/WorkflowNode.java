package com.demo.card.workflow;

/**
 * WorkflowNode is a step in the workflow.
 * Each node has a clear responsibility and can call tools/services.
 */
public interface WorkflowNode {
    /**
     * Execute this node given the current workflow context.
     * Node should read relevant data from context, perform work, and update context.
     */
    void execute(WorkflowContext context) throws Exception;

    /**
     * Human-readable name for logging/debugging.
     */
    String getName();
}
