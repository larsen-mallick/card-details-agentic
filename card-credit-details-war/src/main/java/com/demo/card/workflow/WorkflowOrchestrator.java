package com.demo.card.workflow;

import com.demo.card.workflow.advisors.ChatAdvisor;
import com.demo.card.workflow.advisors.GuardrailsAdvisor;
import com.demo.card.workflow.advisors.ValidatingAdvisor;
import com.demo.card.workflow.nodes.IntakeNode;
import com.demo.card.workflow.nodes.AnalystNode;
import com.demo.card.workflow.nodes.ResolverNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

/**
 * WorkflowOrchestrator runs a multi-step card support workflow.
 * Coordinates nodes and advisors to process customer queries end-to-end.
 */
@Service
public class WorkflowOrchestrator {
    
    @Autowired 
    private IntakeNode intakeNode;
    
    @Autowired 
    private AnalystNode analystNode;
    
    @Autowired 
    private ResolverNode resolverNode;

    private final List<ChatAdvisor> advisors = Arrays.asList(
        new GuardrailsAdvisor(),
        new ValidatingAdvisor()
    );

    /**
     * Execute the full card support workflow for a customer query.
     * 
     * @param cardNo the card number
     * @param userRequest the customer's natural language request
     * @return the synthesized response
     * @throws Exception if any step fails
     */
    public String executeCardSupportWorkflow(String cardNo, String userRequest) throws Exception {
        WorkflowContext context = new WorkflowContext(cardNo, userRequest);
        
        List<WorkflowNode> nodes = Arrays.asList(intakeNode, analystNode, resolverNode);
        
        for (WorkflowNode node : nodes) {
            System.out.println("[Workflow] Executing: " + node.getName());
            
            try {
                // Before advisor chain
                for (ChatAdvisor advisor : advisors) {
                    advisor.beforeExecute(context);
                }
                
                // Execute node
                node.execute(context);
                context.setStatus(WorkflowContext.WorkflowStatus.IN_PROGRESS);
                
                // After advisor chain
                for (ChatAdvisor advisor : advisors) {
                    advisor.afterExecute(context);
                }
                
            } catch (Exception e) {
                context.setStatus(WorkflowContext.WorkflowStatus.FAILED);
                throw new RuntimeException("Workflow failed at " + node.getName() + ": " + e.getMessage(), e);
            }
        }
        
        context.setStatus(WorkflowContext.WorkflowStatus.COMPLETED);
        return context.getResult();
    }
}
