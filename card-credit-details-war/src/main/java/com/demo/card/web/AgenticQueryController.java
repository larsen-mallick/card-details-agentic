package com.demo.card.web;

import com.demo.card.workflow.WorkflowOrchestrator;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * AgenticQueryController exposes the multi-step workflow as REST + MCP endpoints.
 */
@RestController
@RequestMapping("/card-details")
public class AgenticQueryController {
    
    @Autowired
    private WorkflowOrchestrator orchestrator;

    /**
     * Process a natural-language card support query using the agentic workflow.
     * This endpoint is available both as REST and as an MCP tool.
     */
    @PostMapping("/agentic-query")
    @McpTool(description = "Process a card support query with multi-step workflow: intake classification, account analysis, and recommendation")
    @ResponseStatus(HttpStatus.OK)
    public String agenticQuery(
            @RequestParam("cardNo") @McpToolParam(description = "Card number to query") String cardNo,
            @RequestParam("userRequest") @McpToolParam(description = "Customer's natural language request") String userRequest) {
        try {
            return orchestrator.executeCardSupportWorkflow(cardNo, userRequest);
        } catch (Exception e) {
            return "Error processing query: " + e.getMessage();
        }
    }
}
