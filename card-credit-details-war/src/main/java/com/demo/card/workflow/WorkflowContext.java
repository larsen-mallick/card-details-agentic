package com.demo.card.workflow;

import java.util.HashMap;
import java.util.Map;

/**
 * WorkflowContext holds state across workflow steps.
 * Each node reads and updates this context as it executes.
 */
public class WorkflowContext {
    private String cardNo;
    private String userRequest;
    private String primaryIntent;
    private Map<String, Object> data = new HashMap<>();
    private String result;
    private WorkflowStatus status = WorkflowStatus.STARTED;

    public enum WorkflowStatus {
        STARTED, IN_PROGRESS, COMPLETED, FAILED
    }

    public WorkflowContext(String cardNo, String userRequest) {
        this.cardNo = cardNo;
        this.userRequest = userRequest;
    }

    // Data map for passing values between nodes
    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        Object val = data.get(key);
        return val != null ? type.cast(val) : null;
    }

    // Getters/setters
    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }
    public String getUserRequest() { return userRequest; }
    public void setUserRequest(String userRequest) { this.userRequest = userRequest; }
    public String getPrimaryIntent() { return primaryIntent; }
    public void setPrimaryIntent(String primaryIntent) { this.primaryIntent = primaryIntent; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public WorkflowStatus getStatus() { return status; }
    public void setStatus(WorkflowStatus status) { this.status = status; }
}
